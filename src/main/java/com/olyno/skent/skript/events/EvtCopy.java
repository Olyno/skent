package com.olyno.skent.skript.events;

import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.CopyEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;

public class EvtCopy {

    static {
        Skript.registerEvent("File/Directory Copied", SimpleEvent.class, CopyEvent.class,
            "(file|dir[ector(ies|y)]|path) cop(y|ied)"
        )
            .description("When a file or directory is copied.")
            .examples(
                "on file copied:\n" +
                    "\tbroadcast \"My file %event-path% has been copied!\""
            )
            .since("1.0");

        EventValues.registerEventValue(CopyEvent.class, Path.class, e -> e.getSource());
        EventValues.registerEventValue(CopyEvent.class, Path.class, e -> e.getTarget());

    }
}
