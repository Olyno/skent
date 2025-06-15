package com.olyno.skent.skript.events;

import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.CreateEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;

public class EvtCreate {

    static {
        Skript.registerEvent("File/Directory Created", SimpleEvent.class, CreateEvent.class,
            "(file|dir[ector(ies|y)]|path) creat(e[d]|ion)"
        )
            .description("When a file or directory is created.")
            .examples(
                "on file created:\n" +
                    "\tbroadcast \"My file %event-path% has been created!\""
            )
            .since("1.0");

        EventValues.registerEventValue(CreateEvent.class, Path.class, e -> e.getPath());
        EventValues.registerEventValue(CreateEvent.class, String.class, e -> String.join("\n", e.getContent()));

    }
}
