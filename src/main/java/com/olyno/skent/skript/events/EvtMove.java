package com.olyno.skent.skript.events;

import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.MoveEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;

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

        EventValues.registerEventValue(MoveEvent.class, Path.class, new Getter<Path, MoveEvent>() {
            @Override
            public Path get(MoveEvent e) {
                return e.getSource();
            }
        }, 0);

        EventValues.registerEventValue(MoveEvent.class, Path.class, new Getter<Path, MoveEvent>() {
            @Override
            public Path get(MoveEvent e) {
                return e.getTarget();
            }
        }, 0);

    }
}
