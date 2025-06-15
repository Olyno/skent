package com.olyno.skent.skript.events;

import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.UnzipEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;

public class EvtUnzip {

    static {
        Skript.registerEvent("File/Directory Unziped", SimpleEvent.class, UnzipEvent.class,
            "(file|dir[ector(ies|y)]|path) unzip[ed]"
        )
            .description("When files are unzipped.")
            .examples(
                "on file unziped:\n" +
                    "\tbroadcast \"My file %event-path% has been unziped!\""
            )
            .since("1.0");

        EventValues.registerEventValue(UnzipEvent.class, Path.class, e -> e.getTarget());
        EventValues.registerEventValue(UnzipEvent.class, Path.class, e -> e.getSource());
        EventValues.registerEventValue(UnzipEvent.class, String.class, e -> e.getPassword());

    }
}
