package com.olyno.skent.skript.effects.process;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.olyno.skent.skript.events.bukkit.ExecuteCompletedEvent;
import com.olyno.skent.skript.events.bukkit.ExecuteEvent;
import com.olyno.skent.util.skript.AsyncEffect;

import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;

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
            "(execute|run|start) %paths%"
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
        Path[] pathsList = paths.getArray(e);
        for (Path path : pathsList) {
            if (Files.exists(path)) {
                try {
                    Runtime runTime = Runtime.getRuntime();
                    Process process = runTime.exec(path.toString());
                    new ExecuteEvent(path, process);
                    if (process.waitFor() >= 0) {
                        new ExecuteCompletedEvent(path, process);
                    }
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            } else {
                Skript.error("[Skent] Can't execute '" + path.toString() + "': file not found");
            }
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "execute " + paths.toString(e, debug);
    }

}
