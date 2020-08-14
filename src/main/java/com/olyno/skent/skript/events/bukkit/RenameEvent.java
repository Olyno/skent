package com.olyno.skent.skript.events.bukkit;

import java.nio.file.Path;

import com.olyno.skent.Skent;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class RenameEvent extends Event implements Listener {

    public static final HandlerList handlers = new HandlerList();

    private String newName;
    private Path file;

    public RenameEvent() { }

    public RenameEvent(Path file, String newname) {
        this.newName = newname;
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

    public String getNewName() {
        return newName;
    }

    public Path getPath() {
        return file;
    }
}
