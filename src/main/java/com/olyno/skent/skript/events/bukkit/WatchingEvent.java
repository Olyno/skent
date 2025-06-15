package com.olyno.skent.skript.events.bukkit;

import java.nio.file.Path;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import ch.njol.skript.lang.Trigger;

public class WatchingEvent extends Event implements Listener {

    public static final HandlerList handlers = new HandlerList();

    private Path path;
    private Trigger trigger;

    public WatchingEvent() { }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public void run(Path path) {
        this.path = path;
        trigger.execute(this);
    }

    public Path getPath() {
        return path;
    }

}
