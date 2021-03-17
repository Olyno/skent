package com.olyno.skent.skript.events.bukkit;

import java.nio.file.Path;

import com.olyno.skent.Skent;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class ExecuteCompletedEvent extends Event implements Listener {

    public static final HandlerList handlers = new HandlerList();

    private Path file;
    private Process process;

    public ExecuteCompletedEvent() { }

    public ExecuteCompletedEvent(Path file, Process process) {
        this.file = file;
        this.process = process;
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

    public Process getProcess() {
        return process;
    }
}
