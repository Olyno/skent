package com.olyno.skent.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.olyno.skent.skript.events.bukkit.ExecuteEvent;
import com.olyno.skent.util.AsyncEffect;
import org.bukkit.event.Event;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Name("Run/Execute File")
@Description("Runs/Executes a file.")
@Examples({
    "command execute:\n" +
        "\ttrigger:\n" +
        "\t\texecute file path \"plugins/myAwesomeBat.bat\"\n" +
        "\t\tbroadcast \"File executed! I'm now a hacker!\"",

    "# If you need to wait the end of the effect before execute a part of your code, you can\n" +
    "# use this effect as a section effect.\n" +
    "# The code after this effect section will be executed when the effect section has finished to be executed.\n\n" +
    "command execute:\n" +
        "\ttrigger:\n" +
        "\t\texecute file path \"plugins/myAwesomeBat.bat\":\n" +
        "\t\t\tbroadcast \"File executed! I'm now a hacker!\""
})
@Since("1.0")

public class EffExecuteFile extends AsyncEffect {

    static {
        registerAsyncEffect(EffExecuteFile.class,
            "(execute|run) %paths%"
        );
    }

    private Expression<Path> paths;
    private boolean isSingle;

    @Override
    @SuppressWarnings("unchecked")
    protected boolean initAsync(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        paths = (Expression<Path>) expr[0];
        isSingle = paths.getSource().isSingle();
        return true;
    }

    @Override
    protected void executeAsync(Event e) {
        Path[] pathsList = isSingle ? new Path[]{paths.getSingle(e)} : paths.getArray(e);
        for (Path path : pathsList) {
            if (Files.exists(path)) {
                if (Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                    try {
                        Desktop.getDesktop().open(path.toFile());
                        new ExecuteEvent(path);
                    } catch (IOException ex) {
                        if (Files.exists(path)) {
                            Skript.exception(ex);
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "execute " + paths.toString(e, debug);
    }

}
