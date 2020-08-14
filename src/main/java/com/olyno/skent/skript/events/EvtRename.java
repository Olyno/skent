package com.olyno.skent.skript.events;

import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.RenameEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;

@Name("On File/Directory Renamed")
@Description("When a file or directory is renamed.")
@Examples({
    "on file renamed:\n" +
    "\tbroadcast \"My file %event-path% has been renamed!\""
})
@Since("1.0")

public class EvtRename {

    static {
        Skript.registerEvent("Path Renamed", SimpleEvent.class, RenameEvent.class,
            "(file|dir[ector(ies|y)]|path) rename[d]"
        );

        EventValues.registerEventValue(RenameEvent.class, String.class, new Getter<String, RenameEvent>() {
            @Override
            public String get(RenameEvent e) {
                return e.getNewName();
            }
        }, 0);

        EventValues.registerEventValue(RenameEvent.class, Path.class, new Getter<Path, RenameEvent>() {
            @Override
            public Path get(RenameEvent e) {
                return e.getPath();
            }
        }, 0);

    }
}
