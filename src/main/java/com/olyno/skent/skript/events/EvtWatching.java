package com.olyno.skent.skript.events;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.event.Event;

import com.olyno.skent.skript.events.bukkit.WatchingEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import io.methvin.watcher.DirectoryChangeEvent.EventType;
import io.methvin.watcher.DirectoryWatcher;

public class EvtWatching extends SkriptEvent {

    static {
        Skript.registerEvent("Watch File/Directory", EvtWatching.class, WatchingEvent.class,
            "[watch[ing] [for]] (file|dir[ectory]) creation[s] (at|for) [[the] (file[s]|dir[ector(y|ies)]|paths)] %strings%",
            "[watch[ing] [for]] (file|dir[ectory]) change[s] (at|for) [[the] (file[s]|dir[ector(y|ies)]|paths)] %strings%",
            "[watch[ing] [for]] (file|dir[ectory]) deletion[s] (at|for) [[the] (file[s]|dir[ector(y|ies)]|paths)] %strings%",
            "[watch[ing] [for]] (file|dir[ectory]) overflow[s] (at|for) [[the] (file[s]|dir[ector(y|ies)]|paths)] %strings%",
            "[watch[ing] [for]] (any|every) (file|dir[ectory]) change[s] (at|for) [[the] (file[s]|dir[ector(y|ies)]|paths)] %strings%"
        )
            .description("When something changed in a path")
            .examples(
                "watching file creation at \"plugins/Skript/scripts\":\n"
                    + "\tbroadcast \"I added a new script!\""
            )
            .since("2.1");

        EventValues.registerEventValue(WatchingEvent.class, Path.class, e -> e.getPath());
    }

    private String[] paths;
    private boolean watchAll;
    private EventType type;

    private DirectoryWatcher watcher;
    private WatchingEvent event;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
        paths = ((Literal<String>) args[0]).getArray();
        if (matchedPattern < 4) {
            type = EventType.values()[matchedPattern];
        } else {
            watchAll = true;
        }
        return true;
    }

    private void registerListener() {
        Set<Path> allPaths = Arrays.stream(paths)
            .map(Paths::get)
            .map(Path::toAbsolutePath)
            .collect(Collectors.toSet());
        Set<Path> directories = allPaths.stream()
            .map(path -> Files.isDirectory(path) ? path : path.getParent())
            .collect(Collectors.toSet());
        try {
            this.watcher = DirectoryWatcher.builder()
                .paths(new ArrayList<>(directories))
                .listener(event -> {
                    Path eventPath = event.path().toAbsolutePath();
                    EventType eventType = event.eventType();
                    
                    if (allPaths.contains(eventPath) || allPaths.contains(eventPath.getParent())) {
                        if (watchAll || type == eventType) {
                            this.event.run(event.path());
                        }
                    }
                })
                .build();
            watcher.watchAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean check(Event event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean postLoad() {
        this.event = new WatchingEvent();
        registerListener();
        return true;
    }

    @Override
    public void unload() {
        if (this.watcher != null) {
            try {
                this.watcher.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        String pathList = Arrays.toString(paths);
        String eventType = (watchAll ? "any" : type.name().toLowerCase());
        return "watch for " + eventType + " for paths " + pathList;
    }
}
