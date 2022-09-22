package com.olyno.skent.skript.effects.process;

import org.bukkit.event.Event;

import com.olyno.skent.util.skript.AsyncEffect;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;

@Name("Destroy/Kill process")
@Description("Kills a process.")
@Examples({
    "command killProcess:\n" +
        "\ttrigger:\n" +
        "\t\tkill process with pid \"55412\"\n" +
        "\t\tbroadcast \"Killed!\"",

    "# If you need to wait the end of the effect before execute a part of your code, you can\n" +
    "# use the keyword \"sync\" before.\n" +
    "# The code after this effect section will be executed when the effect has finished to be executed.\n\n" +
    "command killProcess:\n" +
        "\ttrigger:\n" +
        "\t\tsync kill process with pid \"55412\"\n" +
        "\t\tbroadcast \"Killed!\""
})
@Since("1.0")

public class EffProcessDestroy extends AsyncEffect {

    static {
        registerAsyncEffect(EffProcessDestroy.class,
            "(kill|destroy|finish|stop) %processes%",
            "force [to] (kill|destroy|finish|stop) %processes%"
        );
    }

    private Expression<Process> process;
    private boolean force;

    @Override
    @SuppressWarnings("unchecked")
    protected boolean initAsync(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        process = (Expression<Process>) expr[0];
        force = matchedPattern == 1;
        return true;
    }

    @Override
    protected void executeAsync(Event e) {
        Process[] processList = process.getArray(e);
        for (Process process : processList) {
            process.destroy();
            if (process.isAlive() && force) {
                process.destroyForcibly();
            }
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return (force ? "force to " : "") + "kill " + process.toString(e, debug);
    }

}
