package com.olyno.skent.skript.events;

import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.ChangeEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;

public class EvtChange {

    static {
        Skript.registerEvent("File/Directory Changed", SimpleEvent.class, ChangeEvent.class,
            "(file|dir[ector(ies|y)]|path) change[d]"
        )
            .description("When a file or directory change.")
            .examples(
                "on file changed:\n" +
                    "\tbroadcast \"My file %event-path% has been changed!\""
            )
            .since("1.0");

        EventValues.registerEventValue(ChangeEvent.class, Path.class, e -> e.getPath());

    }
}
