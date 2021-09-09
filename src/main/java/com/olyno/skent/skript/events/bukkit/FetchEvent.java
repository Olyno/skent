package com.olyno.skent.skript.events.bukkit;

import com.olyno.skent.Skent;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class FetchEvent extends Event implements Listener {

    public static final HandlerList handlers = new HandlerList();

    private String link;

    public FetchEvent() { }

    public FetchEvent(String link) {
        this.link = link;
        Bukkit.getScheduler().runTask(Skent.instance, () -> Bukkit.getPluginManager().callEvent(this));
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public String getLink() {
        return link;
    }
}
