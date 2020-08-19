package com.olyno.skent.util.watch;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class AsyncWatch extends Thread {

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final WatchService service;

    private Path defaultPath;
    private List<String> keysName;
    private ArrayList<WatchListener> listeners;
    private Boolean isDir;

    public AsyncWatch(WatchService service) {
        this.service = service;
        this.listeners = new ArrayList<>();
    }

    public void stopWatching() {
        running.set(false);
        try {
            service.close();
        } catch (IOException | ClosedWatchServiceException ignore) { }
    }

    @Override
    public void run() {
        running.set(true);
        while (running.get() && Files.exists(defaultPath)) {
            try {
                WatchKey key = service.take();
                if (key != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        Path filePath = (Path) event.context();
                        Path comparingPath = isDir ? defaultPath.getParent() : defaultPath.getParent().resolve(filePath);
                        Path allPath = defaultPath.getParent().resolve(filePath);
                        if (allPath.toFile().length() > 0) { // Eliminate double calls
                            if (keysName.contains(event.kind().name().toLowerCase())) {
                                if (comparingPath.toString().equals(defaultPath.toString())) {
                                    for (WatchListener listener : listeners) {
                                        listener.onChange(defaultPath);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    key.reset();
                }
            } catch (InterruptedException | ClosedWatchServiceException ignore) {
                continue;
            }
        }
    }

    public AsyncWatch setDefaultPath(Path path) {
        this.defaultPath = path;
        return this;
    }

    public Path getDefaultPath() {
        return defaultPath;
    }

    public AsyncWatch addListener(WatchListener listener) {
        listeners.add(listener);
        return this;
    }

    public AsyncWatch setListenerKeys(ArrayList<WatchEvent.Kind<?>> keys) {
        this.keysName = keys.stream().map(key -> key.name().toLowerCase()).collect(Collectors.toList());
        return this;
    }

    public List<String> getListenerKeysName() {
        return this.keysName;
    }

    public AsyncWatch IsDir(Boolean isDir) {
        this.isDir = isDir;
        return this;
    }

    public Boolean IsDir() {
        return isDir;
    }
    
}