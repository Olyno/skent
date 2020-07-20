package com.olyno.skent.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.olyno.skent.skript.events.bukkit.UnzipEvent;

import java.nio.file.Path;

@Name("On File/Directory Unziped")
@Description("When files are unzipped.")
@Examples({
    "on file unziped:\n" +
    "\tbroadcast \"My file %event-path% has been unziped!\""
})
@Since("1.0")

public class EvtUnzip {

    static {
        Skript.registerEvent("Path Unziped", SimpleEvent.class, UnzipEvent.class,
            "(file|dir[ector(ies|y)]|path) unzip[ed]"
        );

        EventValues.registerEventValue(UnzipEvent.class, Path.class, new Getter<Path, UnzipEvent>() {
            @Override
            public Path get(UnzipEvent e) {
                return e.getTarget();
            }
        }, 0);

        EventValues.registerEventValue(UnzipEvent.class, Path.class, new Getter<Path, UnzipEvent>() {
            @Override
            public Path get(UnzipEvent e) {
                return e.getSource();
            }
        }, 0);

        EventValues.registerEventValue(UnzipEvent.class, String.class, new Getter<String, UnzipEvent>() {
            @Override
            public String get(UnzipEvent e) {
                return e.getPassword();
            }
        }, 0);

    }
}
