package com.olyno.skent.skript.events;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.olyno.skent.skript.events.bukkit.WatchingEvent;
import com.olyno.skent.util.WatchType;
import com.olyno.skent.util.watch.AsyncWatch;

import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SelfRegisteringSkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;

@Name("On Watch File/Directory")
@Description("When something changed in a path")
@Examples({ "watching file creation at \"plugins/Skript/scripts\":\n" + "\tbroadcast \"I added a new script!\"" })
@Since("2.1")

public class EvtWatching extends SelfRegisteringSkriptEvent {

    private static HashMap<WatchType, WatchEvent.Kind<?>> watchTypes = new HashMap<>();

    static {

        watchTypes.put(WatchType.CREATION, StandardWatchEventKinds.ENTRY_CREATE);
        watchTypes.put(WatchType.EDITION, StandardWatchEventKinds.ENTRY_MODIFY);
        watchTypes.put(WatchType.DELETION, StandardWatchEventKinds.ENTRY_DELETE);

        Skript.registerEvent("Watch Path", EvtWatching.class, WatchingEvent.class,
                "[watch[ing] [for]] (file|dir[ectory]) creation[s] (at|for) [[the] (file[s]|dir[ector(y|ies)]|paths)] %strings%",
                "[watch[ing] [for]] (file|dir[ectory]) change[s] (at|for) [[the] (file[s]|dir[ector(y|ies)]|paths)] %strings%",
                "[watch[ing] [for]] (file|dir[ectory]) deletion[s]) (at|for) [[the] (file[s]|dir[ector(y|ies)]|paths)] %strings%",
                "[watch[ing] [for]] (any|every) (file|dir[ectory]) change[s] (at|for) [[the] (file[s]|dir[ector(y|ies)]|paths)] %strings%");

        EventValues.registerEventValue(WatchingEvent.class, Path.class, new Getter<Path, WatchingEvent>() {
            @Override
            public Path get(WatchingEvent e) {
                return e.getPath();
            }
        }, 0);

    }

    private String[] paths;
    private WatchType type;
    private boolean watchAllChanges;

    private Trigger trigger;

    private AsyncWatch watcher;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
        paths = ((Literal<String>) args[0]).getArray();
        if (matchedPattern < 3) {
            type = WatchType.values()[matchedPattern];
        }
        watchAllChanges = matchedPattern == 3;
        return true;
    }

    private void registerListener() {
        try {
            for (String watchingPath : paths) {
                Path defaultPath = Paths.get(watchingPath);
                Path pathWatcher = defaultPath;
                WatchService watchService = FileSystems.getDefault().newWatchService();
                ArrayList<WatchEvent.Kind<?>> keys = new ArrayList<>();
                if (Files.isRegularFile(pathWatcher))
                    pathWatcher = pathWatcher.getParent();
                if (!Files.isDirectory(pathWatcher))
                    return;
                if (watchAllChanges) {
                    keys.addAll(Arrays.asList(WatchType.values())
                        .stream()
                        .map(watchType -> watchTypes.get(watchType))
                        .collect(Collectors.toList()));
                } else {
                    keys.add(watchTypes.get(type));
                }
                pathWatcher.register(watchService, keys.toArray(new WatchEvent.Kind<?>[keys.size()]));
                watcher = new AsyncWatch(watchService)
                    .IsDir(Files.isDirectory(defaultPath))
                    .setDefaultPath(defaultPath)
                    .setListenerKeys(keys)
                    .addListener(new WatchingEvent(trigger));
                watcher.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void register(Trigger trigger) {
        this.trigger = trigger;
        registerListener();
    }

    @Override
    public void unregister(Trigger t) {
        this.trigger = null;
        watcher.stopWatching();
    }

    @Override
    public void unregisterAll() {
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "watch for " + type.name().toLowerCase() + " for paths " + paths.toString();
    }
    
}