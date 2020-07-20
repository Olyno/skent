package com.olyno.skent.skript.events.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.nio.file.Path;

import com.olyno.skent.Skent;

public class WatchingFileEvent extends Event implements Listener {

    public static final HandlerList handlers = new HandlerList();

    private String kind;
    private Path file;

    public WatchingFileEvent() { }

    public WatchingFileEvent(Path file, String kind) {
        this.file = file;
        this.kind = kind;
        Bukkit.getScheduler().runTask(Skent.instance, () -> Bukkit.getPluginManager().callEvent(this));
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public String getKind() {
        return kind;
    }

    public Path getPath() {
        return file;
    }
}
