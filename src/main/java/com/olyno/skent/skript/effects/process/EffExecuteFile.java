package com.olyno.skent.skript.effects.process;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.event.Event;

import com.olyno.skent.skript.events.bukkit.ExecuteCompletedEvent;
import com.olyno.skent.skript.events.bukkit.ExecuteEvent;
import com.olyno.skent.util.skript.AsyncEffect;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

@Name("Run/Execute File")
@Description("Runs/Executes a file.")
@Examples({
    "command execute:\n" +
        "\ttrigger:\n" +
        "\t\texecute file path \"plugins/myAwesomeBat.bat\"\n" +
        "\t\tbroadcast \"File executed! I'm now a hacker!\"",

    "# If you need to wait the end of the effect before execute a part of your code, you can\n" +
    "# use the keyword \"sync\" before.\n" +
    "# The code after this effect section will be executed when the effect has finished to be executed.\n\n" +
    "command execute:\n" +
        "\ttrigger:\n" +
        "\t\tsync execute file path \"plugins/myAwesomeBat.bat\"\n" +
        "\t\tbroadcast \"File executed! I'm now a hacker!\""
})
@Since("1.0")

public class EffExecuteFile extends AsyncEffect {

    static {
        registerAsyncEffect(EffExecuteFile.class,
            "(execute|run|start) %paths% [with arg[ument][s] %-strings% [and]] [with env[ironment file[s]] %-paths% [and]] [(1¦with (logs|output))]"
        );
    }

    public static Process lastProcess;

    private Expression<Path> paths;
    private Expression<String> argsExpression;
    private Expression<Path> envFileExpression;
    private boolean withLogs;

    @Override
    @SuppressWarnings("unchecked")
    protected boolean initAsync(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        paths = (Expression<Path>) expr[0];
        argsExpression = (Expression<String>) expr[1];
        envFileExpression = (Expression<Path>) expr[2];
        withLogs = parseResult.mark == 1;
        return true;
    }

    @Override
    protected void execute(Event e) {
        Path[] pathsList = paths.getArray(e);
        String[] arguments = argsExpression != null ? argsExpression.getArray(e) : new String[0];
        Path[] envFiles = envFileExpression != null ? envFileExpression.getArray(e) : new Path[0];
        for (Path path : pathsList) {
            if (Files.exists(path)) {
                try {
                    ArrayList<String> command = new ArrayList<String>();
                    command.add(path.toString());
                    if (arguments.length > 0) {
                        command.addAll(Arrays.asList(arguments));
                    }
                    ProcessBuilder process = new ProcessBuilder(command);
                    if (envFiles.length > 0) {
                        for (Path envFile : envFiles) {
                            if (Files.exists(envFile)) {
                                HashMap<String, String> envMap = new HashMap<String, String>();
                                Dotenv dotenv = Dotenv.configure()
                                    .directory(envFile.getParent().toAbsolutePath().toString())
                                    .filename(envFile.getFileName().toString())
                                    .load();
                                for (DotenvEntry entry : dotenv.entries()) {
                                    envMap.put(entry.getKey(), entry.getValue());
                                }
                                process.environment().putAll(envMap);
                            }
                        }
                    }
                    if (withLogs) {
                        process.redirectErrorStream(true);
                        process.redirectOutput(Redirect.INHERIT);
                    }
                    lastProcess = process.start();
                    lastProcess.onExit().thenRun(() -> {
                        new ExecuteCompletedEvent(path, lastProcess);
                    });
                    new ExecuteEvent(path, lastProcess);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                Skript.error("[Skent] Can't execute '" + path.toString() + "': file not found");
            }
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "execute " + paths.toString(e, debug)
            + (argsExpression != null ? " with arguments " + argsExpression.toString(e, debug) : "")
            + (envFileExpression != null ? " with environment file " + envFileExpression.toString(e, debug) : "")
            + (withLogs ? " with logs" : "");
    }

}
