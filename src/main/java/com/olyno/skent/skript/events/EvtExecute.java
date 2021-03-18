package com.olyno.skent.skript.events;

import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.ExecuteEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;

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

        EventValues.registerEventValue(ExecuteEvent.class, Path.class, new Getter<Path, ExecuteEvent>() {
            @Override
            public Path get(ExecuteEvent e) {
                return e.getPath();
            }
        }, 0);

        EventValues.registerEventValue(ExecuteEvent.class, Process.class, new Getter<Process, ExecuteEvent>() {
            @Override
            public Process get(ExecuteEvent e) {
                return e.getProcess();
            }
        }, 0);

    }
}
