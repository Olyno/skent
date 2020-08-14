package com.olyno.skent.skript.events;

import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.DeleteEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;

@Name("On File/Directory Deleted")
@Description("When a file or directory is deleted.")
@Examples({
    "on file deleted:\n" +
    "\tbroadcast \"My file %event-path% has been deleted :/\""
})
@Since("1.0")

public class EvtDelete {

    static {
        Skript.registerEvent("Path Deleted", SimpleEvent.class, DeleteEvent.class,
            "(file|dir[ector(ies|y)]|path) delet(e[d]|ion)"
        );

        EventValues.registerEventValue(DeleteEvent.class, Path.class, new Getter<Path, DeleteEvent>() {
            @Override
            public Path get(DeleteEvent e) {
                return e.getPath();
            }
        }, 0);

    }
}
