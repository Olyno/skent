package com.olyno.skent.skript.events.bukkit;

import java.nio.file.Path;

import com.olyno.skent.util.watch.WatchListener;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import ch.njol.skript.lang.Trigger;

public class WatchingEvent extends Event implements WatchListener, Listener {

    public static final HandlerList handlers = new HandlerList();

    private Path path;
    private boolean isExecuted;
    private Trigger trigger;

    public WatchingEvent() { }

    public WatchingEvent(Trigger trigger) {
        this.trigger = trigger;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public void onChange(Path path) {
        this.path = path;
        this.isExecuted = true;
        trigger.execute(this);
    }

    public Path getPath() {
        return path;
    }

    public boolean IsExecuted() {
        return isExecuted;
    }

}
