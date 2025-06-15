package com.olyno.skent.skript.events;

import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.ExecuteEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;

public class EvtExecuteCompleted {

    static {
        Skript.registerEvent("File Execution Completed", SimpleEvent.class, ExecuteEvent.class,
            "(file|dir[ector(ies|y)]|path) execution (end[ed]|complete[d]|finish[ed])"
        )
            .description("When a file finished to be executed.")
            .examples(
                "on file execution completed:\n" +
                    "\tbroadcast \"My file %event-path% has been finished!\""
            )
            .since("2.2.0");

        EventValues.registerEventValue(ExecuteEvent.class, Path.class, e -> e.getPath());
        EventValues.registerEventValue(ExecuteEvent.class, Process.class, e -> e.getProcess());

    }
}
