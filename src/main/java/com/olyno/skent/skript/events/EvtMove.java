package com.olyno.skent.skript.events;

import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.MoveEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;

public class EvtMove {

    static {
        Skript.registerEvent("File/Directory Moved", SimpleEvent.class, MoveEvent.class,
            "(file|dir[ector(ies|y)]|path) mov(ed|ing|e)"
        )
            .description("When a file or directory is moved.")
            .examples(
                "on file moved:\n" +
                    "\tbroadcast \"My file %event-path% has been moved!\""
            )
            .since("1.0");

        EventValues.registerEventValue(MoveEvent.class, Path.class, e -> e.getSource());
        EventValues.registerEventValue(MoveEvent.class, Path.class, e -> e.getTarget());

    }
}
