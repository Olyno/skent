package com.olyno.skent.skript.events;

import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.RenameEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;

public class EvtRename {

    static {
        Skript.registerEvent("File/Directory Renamed", SimpleEvent.class, RenameEvent.class,
            "(file|dir[ector(ies|y)]|path) rename[d]"
        )
            .description("When a file or directory is renamed.")
            .examples(
                "on file renamed:\n" +
                    "\tbroadcast \"My file %event-path% has been renamed!\""
            )
            .since("1.0");

        EventValues.registerEventValue(RenameEvent.class, String.class, e -> e.getNewName());
        EventValues.registerEventValue(RenameEvent.class, Path.class, e -> e.getPath());

    }
}
