package com.olyno.skent.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.olyno.skent.skript.events.bukkit.MoveEvent;

import java.nio.file.Path;

@Name("On File/Directory Moved")
@Description("When a file or directory is moved.")
@Examples({
    "on file moved:\n" +
    "\tbroadcast \"My file %event-path% has been moved!\""
})
@Since("1.0")

public class EvtMove {

    static {
        Skript.registerEvent("Path Moved", SimpleEvent.class, MoveEvent.class,
            "(file|dir[ector(ies|y)]|path) mov(ed|ing|e)"
        );

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
