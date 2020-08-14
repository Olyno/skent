package com.olyno.skent.skript.events;

import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.ChangeEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;

@Name("On File/Directory Changed")
@Description("When a file or directory change.")
@Examples({
    "on file changed:\n" +
    "\tbroadcast \"My file %event-path% has been changed!\""
})
@Since("1.0")

public class EvtChange {

    static {
        Skript.registerEvent("Path Changed", SimpleEvent.class, ChangeEvent.class,
            "(file|dir[ector(ies|y)]|path) change[d]"
        );

        EventValues.registerEventValue(ChangeEvent.class, Path.class, new Getter<Path, ChangeEvent>() {
            @Override
            public Path get(ChangeEvent e) {
                return e.getPath();
            }
        }, 0);

    }
}
