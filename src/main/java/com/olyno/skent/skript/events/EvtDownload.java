package com.olyno.skent.skript.events;

import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.DownloadEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;

public class EvtDownload {

    static {
        Skript.registerEvent("File Downloaded", SimpleEvent.class, DownloadEvent.class,
            "file download[(ing|ed)]"
        )
            .description("When a file or directory is downloaded.")
            .examples(
                "on file downloaded:\n" +
                    "\tbroadcast \"My file %event-path% has been downloaded!\""
            )
            .since("1.0");

        EventValues.registerEventValue(DownloadEvent.class, String.class, e -> e.getUrl());
        EventValues.registerEventValue(DownloadEvent.class, Path.class, e -> e.getPath());

    }
}
