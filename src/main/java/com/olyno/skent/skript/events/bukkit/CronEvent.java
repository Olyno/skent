package com.olyno.skent.skript.events.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import com.olyno.skent.Skent;

public class CronEvent extends Event implements Listener {

    public static final HandlerList handlers = new HandlerList();

    public CronEvent() {
        Bukkit.getScheduler().runTask(Skent.instance, () -> Bukkit.getPluginManager().callEvent(this));
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
