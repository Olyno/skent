package com.olyno.skent.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.olyno.skent.skript.events.bukkit.ZipEvent;

import java.nio.file.Path;

@Name("On File/Directory Ziped")
@Description("When files or directories are zipped.")
@Examples({
    "on file ziped:\n" +
    "\tbroadcast \"My file %event-path% has been ziped!\""
})
@Since("1.0")

public class EvtZip {

    static {
        Skript.registerEvent("Path Ziped", SimpleEvent.class, ZipEvent.class,
            "(file|dir[ector(ies|y)]|path) zip[ed]"
        );

        EventValues.registerEventValue(ZipEvent.class, Path.class, new Getter<Path, ZipEvent>() {
            @Override
            public Path get(ZipEvent e) {
                return e.getTarget();
            }
        }, 0);

        EventValues.registerEventValue(ZipEvent.class, String.class, new Getter<String, ZipEvent>() {
            @Override
            public String get(ZipEvent e) {
                return e.getPassword();
            }
        }, 0);

    }
}
