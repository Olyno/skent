package com.olyno.skent.skript.events;

import com.olyno.skent.skript.events.bukkit.FetchEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;

public class EvtFetch {

    static {
        Skript.registerEvent("File Fetched", SimpleEvent.class, FetchEvent.class,
            "file fetch[ed]"
        )
            .description("When a file is fetched.")
            .examples(
                "on file fetched:\n" +
                    "\tbroadcast \"My file %event-path% has been fetched!\""
            )
            .since("1.0");

        EventValues.registerEventValue(FetchEvent.class, String.class, e -> e.getLink());

    }
}
