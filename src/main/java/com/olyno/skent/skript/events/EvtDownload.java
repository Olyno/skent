package com.olyno.skent.skript.events;

import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.DownloadEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;

public class EvtDownload {

    static {
        Skript.registerEvent("On File Downloaded", SimpleEvent.class, DownloadEvent.class,
            "file download[(ing|ed)]"
        )
            .description("When a file or directory is downloaded.")
            .examples(
                "on file downloaded:\n" +
                    "\tbroadcast \"My file %event-path% has been downloaded!\""
            )
            .since("1.0");

        EventValues.registerEventValue(DownloadEvent.class, String.class, new Getter<String, DownloadEvent>() {
            @Override
            public String get(DownloadEvent e) {
                return e.getUrl();
            }
        }, 0);

        EventValues.registerEventValue(DownloadEvent.class, Path.class, new Getter<Path, DownloadEvent>() {
            @Override
            public Path get(DownloadEvent e) {
                return e.getPath();
            }
        }, 0);

    }
}
