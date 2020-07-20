package com.olyno.skent.skript.events.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.nio.file.Path;

import com.olyno.skent.Skent;

public class CreateEvent extends Event implements Listener {

    public static final HandlerList handlers = new HandlerList();

    private Path file;
    private String[] content;

    public CreateEvent() { }

    public CreateEvent(Path file, String[] content) {
        this.file = file;
        this.content = content;
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

    public String[] getContent() {
        return content;
    }
}
