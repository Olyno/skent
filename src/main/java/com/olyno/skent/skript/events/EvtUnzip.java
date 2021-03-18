package com.olyno.skent.skript.events;

import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.UnzipEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;

public class EvtUnzip {

    static {
        Skript.registerEvent("On File/Directory Unziped", SimpleEvent.class, UnzipEvent.class,
            "(file|dir[ector(ies|y)]|path) unzip[ed]"
        )
            .description("When files are unzipped.")
            .examples(
                "on file unziped:\n" +
                    "\tbroadcast \"My file %event-path% has been unziped!\""
            )
            .since("1.0");

        EventValues.registerEventValue(UnzipEvent.class, Path.class, new Getter<Path, UnzipEvent>() {
            @Override
            public Path get(UnzipEvent e) {
                return e.getTarget();
            }
        }, 0);

        EventValues.registerEventValue(UnzipEvent.class, Path.class, new Getter<Path, UnzipEvent>() {
            @Override
            public Path get(UnzipEvent e) {
                return e.getSource();
            }
        }, 0);

        EventValues.registerEventValue(UnzipEvent.class, String.class, new Getter<String, UnzipEvent>() {
            @Override
            public String get(UnzipEvent e) {
                return e.getPassword();
            }
        }, 0);

    }
}
