package com.olyno.skent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.olyno.skent.util.PackageFilter;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;

public class Skent extends JavaPlugin {

    public static Skent instance;
	SkriptAddon addon;

	public void onEnable() {

		// Register Addon
		instance = this;
		addon = Skript.registerAddon(this);
		try {
			addon.loadClasses("com.olyno.skent.skript");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Register Metrics
		Metrics metrics = new Metrics(this);
		metrics.addCustomChart(new Metrics.SimplePie("used_language", () ->
				getConfig().getString("language", "en")));
		metrics.addCustomChart(new Metrics.SimplePie("skript_version", () ->
				Bukkit.getServer().getPluginManager().getPlugin("Skript").getDescription().getVersion()));
		metrics.addCustomChart(new Metrics.SimplePie("skent_version", () ->
				this.getDescription().getVersion()));
		metrics.addCustomChart(new Metrics.DrilldownPie("java_version", () -> {
			Map<String, Map<String, Integer>> map = new HashMap<>();
			String javaVersion = System.getProperty("java.version");
			Map<String, Integer> entry = new HashMap<>();
			entry.put(javaVersion, 1);
			if (javaVersion.startsWith("1.7")) {
				map.put("Java 1.7", entry);
			} else if (javaVersion.startsWith("1.8")) {
				map.put("Java 1.8", entry);
			} else if (javaVersion.startsWith("1.9")) {
				map.put("Java 1.9", entry);
			} else {
				map.put("Other", entry);
			}
			return map;
		}));

		// Register events
		try {
			List<Class<Listener>> classes = new PackageFilter<Listener>("com.olyno.skent.skript.events.bukkit").getClasses();
			for (Class<Listener> event : classes) {
				Listener evt = event.getDeclaredConstructor().newInstance();
				getServer().getPluginManager().registerEvents(evt, this);
			}
		} catch (IOException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

	}

}