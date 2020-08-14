package com.olyno.skent.skript.events;

import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.DownloadEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;

@Name("On File/Directory Downloaded")
@Description("When a file or directory is downloaded.")
@Examples({
    "on file downloaded:\n" +
    "\tbroadcast \"My file %event-path% has been downloaded!\""
})
@Since("1.0")

public class EvtDownload {

    static {
        Skript.registerEvent("Path Downloaded", SimpleEvent.class, DownloadEvent.class,
            "(file|dir[ector(ies|y)]) download[(ing|ed)]"
        );

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
