package com.olyno.skent.skript.events.bukkit;

import java.nio.file.Path;

import com.olyno.skent.Skent;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class DownloadEvent extends Event implements Listener {

    public static final HandlerList handlers = new HandlerList();

    private String url;
    private Path file;

    public DownloadEvent() { }

    public DownloadEvent(String url, Path file) {
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public Path getPath() {
        return file;
    }
}
