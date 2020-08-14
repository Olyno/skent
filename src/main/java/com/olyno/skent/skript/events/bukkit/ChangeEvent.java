package com.olyno.skent.skript.events.bukkit;

import java.nio.file.Path;

import com.olyno.skent.Skent;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class ChangeEvent extends Event implements Listener {

    public static final HandlerList handlers = new HandlerList();

    private Path path;

    public ChangeEvent() { }

    public ChangeEvent(Path path) {
        this.path = path;
        Bukkit.getScheduler().runTask(Skent.instance, () -> Bukkit.getPluginManager().callEvent(this));
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Path getPath() {
        return path;
    }

}
