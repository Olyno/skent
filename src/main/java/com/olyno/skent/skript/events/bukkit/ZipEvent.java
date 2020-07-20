package com.olyno.skent.skript.events.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.nio.file.Path;

import com.olyno.skent.Skent;

public class ZipEvent extends Event implements Listener {

    public static final HandlerList handlers = new HandlerList();

    private Path[] source;
    private Path target;
    private String password;

    public ZipEvent() { }

    public ZipEvent(Path[] source, Path target, String password) {
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

    public Path[] getSource() {
        return source;
    }

    public Path getTarget() {
        return target;
    }

    public String getPassword() {
        return password;
    }
}
