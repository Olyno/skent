package com.olyno.skent.skript.events;

import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.ExecuteEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;

public class EvtExecute {

    static {
        Skript.registerEvent("File Executed", SimpleEvent.class, ExecuteEvent.class,
            "file (execute[d]|start[ed]|running)"
        )
            .description("When a file is executed.")
            .examples(
                "on file executed:\n" +
                    "\tbroadcast \"My file %event-path% has been executed!\""
            )
            .since("1.0");

        EventValues.registerEventValue(ExecuteEvent.class, Path.class, e -> e.getPath());
        EventValues.registerEventValue(ExecuteEvent.class, Process.class, e -> e.getProcess());

    }
}
