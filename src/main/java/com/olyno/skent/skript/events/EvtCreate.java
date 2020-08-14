package com.olyno.skent.skript.events;

import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.CreateEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;

@Name("On File/Directory Created")
@Description("When a file or directory is created.")
@Examples({
    "on file created:\n" +
    "\tbroadcast \"My file %event-path% has been created!\""
})
@Since("1.0")

public class EvtCreate {

    static {
        Skript.registerEvent("Path Created", SimpleEvent.class, CreateEvent.class,
            "(file|dir[ector(ies|y)]|path) creat(e[d]|ion)"
        );

        EventValues.registerEventValue(CreateEvent.class, Path.class, new Getter<Path, CreateEvent>() {
            @Override
            public Path get(CreateEvent e) {
                return e.getPath();
            }
        }, 0);

        EventValues.registerEventValue(CreateEvent.class, String.class, new Getter<String, CreateEvent>() {
            @Override
            public String get(CreateEvent e) {
                return String.join("\n", e.getContent());
            }
        }, 0);

    }
}
