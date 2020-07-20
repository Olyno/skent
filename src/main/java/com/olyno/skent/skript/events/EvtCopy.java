package com.olyno.skent.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.olyno.skent.skript.events.bukkit.CopyEvent;

import java.nio.file.Path;

@Name("On File/Directory Copied")
@Description("When a file or directory is copied.")
@Examples({
    "on file copied:\n" +
    "\tbroadcast \"My file %event-path% has been copied!\""
})
@Since("1.0")

public class EvtCopy {

    static {
        Skript.registerEvent("Path Copied", SimpleEvent.class, CopyEvent.class,
            "(file|dir[ector(ies|y)]|path) cop(y|ied)"
        );

        EventValues.registerEventValue(CopyEvent.class, Path.class, new Getter<Path, CopyEvent>() {
            @Override
            public Path get(CopyEvent e) {
                return e.getSource();
            }
        }, 0);

        EventValues.registerEventValue(CopyEvent.class, Path.class, new Getter<Path, CopyEvent>() {
            @Override
            public Path get(CopyEvent e) {
                return e.getTarget();
            }
        }, 0);

    }
}
