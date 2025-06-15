package com.olyno.skent.skript.events;

import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.DeleteEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;

public class EvtDelete {

    static {
        Skript.registerEvent("File/Directory Deleted", SimpleEvent.class, DeleteEvent.class,
            "(file|dir[ector(ies|y)]|path) delet(e[d]|ion)"
        )
            .description("When a file or directory is deleted.")
            .examples(
                "on file deleted:\n" +
                    "\tbroadcast \"My file %event-path% has been deleted :/\""
            )
            .since("1.0");

        EventValues.registerEventValue(DeleteEvent.class, Path.class, e -> e.getPath());

    }
}
