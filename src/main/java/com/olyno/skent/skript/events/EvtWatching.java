package com.olyno.skent.skript.events;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.event.Event;

import com.olyno.skent.skript.events.bukkit.WatchingEvent;
import com.olyno.skent.util.skript.WatchType;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SelfRegisteringSkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import io.methvin.watcher.DirectoryWatcher;

public class EvtWatching extends SelfRegisteringSkriptEvent {

    static {
        Skript.registerEvent("Watch File/Directory", EvtWatching.class, WatchingEvent.class,
            "[watch[ing] [for]] (file|dir[ectory]) creation[s] (at|for) [[the] (file[s]|dir[ector(y|ies)]|paths)] %strings%",
            "[watch[ing] [for]] (file|dir[ectory]) change[s] (at|for) [[the] (file[s]|dir[ector(y|ies)]|paths)] %strings%",
            "[watch[ing] [for]] (file|dir[ectory]) deletion[s] (at|for) [[the] (file[s]|dir[ector(y|ies)]|paths)] %strings%",
            "[watch[ing] [for]] (any|every) (file|dir[ectory]) change[s] (at|for) [[the] (file[s]|dir[ector(y|ies)]|paths)] %strings%"
        )
            .description("When something changed in a path")
            .examples(
                "watching file creation at \"plugins/Skript/scripts\":\n"
                    + "\tbroadcast \"I added a new script!\""
            )
            .since("2.1");

        EventValues.registerEventValue(WatchingEvent.class, Path.class, new Getter<Path, WatchingEvent>() {
            @Override
            public Path get(WatchingEvent e) {
                return e.getPath();
            }
        }, 0);

    }

    private String[] paths;
    private WatchType type;

    private DirectoryWatcher watcher;
    private Trigger trigger;
    private WatchingEvent event;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
        paths = ((Literal<String>) args[0]).getArray();
        if (matchedPattern < 3) {
            type = WatchType.values()[matchedPattern];
        }
        return true;
    }

    private void registerListener() {
        List<Path> allPaths = Arrays.asList(paths)
            .stream()
            .map(path -> Paths.get(path))
            .collect(Collectors.toList());
        List<Path> directories = allPaths
            .stream()
            .map(path -> Files.isDirectory(path) ? path : path.getParent())
            .collect(Collectors.toList());
        try {
            this.watcher = DirectoryWatcher.builder()
                .paths(directories)
                .listener(event -> {
                    if (allPaths.contains(event.path()) || allPaths.contains(event.path().getParent())) {
                        switch (event.eventType()) {
                            case CREATE:
                                if (type == WatchType.CREATION || type == WatchType.ANY) {
                                    this.event.run(event.path());
                                }
                                break;
                            case MODIFY:
                                if (type == WatchType.EDITION || type == WatchType.ANY) {
                                    this.event.run(event.path());
                                }
                                break;
                            case DELETE:
                                if (type == WatchType.DELETION || type == WatchType.ANY) {
                                    this.event.run(event.path());
                                }
                                break;
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
    public void register(Trigger trigger) {
        this.trigger = trigger;
        this.event = new WatchingEvent(trigger);
        registerListener();
    }

    @Override
    public void unregister(Trigger t) {
        this.trigger = null;
        if (this.watcher != null) {
            try {
                this.watcher.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void unregisterAll() {
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "watch for " + type.name().toLowerCase() + " for paths " + paths.toString();
    }
    
}