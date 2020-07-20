package com.olyno.skent.skript.events.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.nio.file.Path;

import com.olyno.skent.Skent;

public class ExecuteEvent extends Event implements Listener {

    public static final HandlerList handlers = new HandlerList();

    private Path file;

    public ExecuteEvent() { }

    public ExecuteEvent(Path file) {
        this.file = file;
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
        return file;
    }
}
