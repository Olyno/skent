package com.olyno.skent.skript.events.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.nio.file.Path;

import com.olyno.skent.Skent;

public class UnzipEvent extends Event implements Listener {

    public static final HandlerList handlers = new HandlerList();

    private Path[] files;
    private Path source;
    private Path target;
    private String password;

    public UnzipEvent() { }

    public UnzipEvent(Path[] files, Path source, Path target, String password) {
        this.files = files;
        this.source = source;
        this.target = target;
        this.password = password;
        Bukkit.getScheduler().runTask(Skent.instance, () -> Bukkit.getPluginManager().callEvent(this));
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Path getSource() {
        return source;
    }

    public Path getTarget() {
        return target;
    }

    public String getPassword() {
        return password;
    }

    public Path[] getFiles() {
        return files;
    }
}
