package com.olyno.skent.skript.events;

import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.ZipEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;

public class EvtZip {

    static {
        Skript.registerEvent("File/Directory Ziped", SimpleEvent.class, ZipEvent.class,
            "(file|dir[ector(ies|y)]|path) zip[ed]"
        )
            .description("When files or directories are zipped.")
            .examples(
                "on file ziped:\n" +
                    "\tbroadcast \"My file %event-path% has been ziped!\""
            )
            .since("1.0");

        EventValues.registerEventValue(ZipEvent.class, Path.class, e -> e.getTarget());
        EventValues.registerEventValue(ZipEvent.class, String.class, e -> e.getPassword());

    }
}
