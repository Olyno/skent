package com.olyno.skent.skript.events.bukkit;

import java.nio.file.Path;

import com.olyno.skent.Skent;
import com.olyno.skent.util.watch.WatchListener;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class WatchingEvent extends Event implements WatchListener, Listener {

    public static final HandlerList handlers = new HandlerList();

    private Path path;
    private boolean isExecuted;

    public WatchingEvent() {
        Bukkit.getScheduler().runTask(Skent.instance, () -> Bukkit.getPluginManager().callEvent(this));
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
        Bukkit.getScheduler().runTask(Skent.instance, () -> {
            System.out.println("Calling bukkit event...");
            Bukkit.getPluginManager().callEvent(this);
            this.isExecuted = false;
        });
    }

    public Path getPath() {
        return path;
    }

    public boolean IsExecuted() {
        return isExecuted;
    }

}
