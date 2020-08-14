package com.olyno.skent.skript.events;

import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.ExecuteEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;

@Name("On File Executed")
@Description("When a file is executed.")
@Examples({
    "on file executed:\n" +
    "\tbroadcast \"My file %event-path% has been executed!\""
})
@Since("1.0")

public class EvtExecute {

    static {
        Skript.registerEvent("Path Executed", SimpleEvent.class, ExecuteEvent.class,
            "(file|dir[ector(ies|y)]|path) execute[d]"
        );

        EventValues.registerEventValue(ExecuteEvent.class, Path.class, new Getter<Path, ExecuteEvent>() {
            @Override
            public Path get(ExecuteEvent e) {
                return e.getPath();
            }
        }, 0);

    }
}
