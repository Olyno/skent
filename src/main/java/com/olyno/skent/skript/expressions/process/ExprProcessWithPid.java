package com.olyno.skent.skript.expressions.process;

import java.util.Optional;

import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;

@Name("Process PID")
@Description("Returns the pid of a process.")
@Examples({
    "on file executed:\n" +
        "\tset {_pid} to pid of event-process\n" +
        "\tbroadcast \"Here is the pid of the running process: %{_pid}%\""
})
@Since("2.2.0")

public class ExprProcessWithPid extends SimpleExpression<Process> {

    static {
        Skript.registerExpression(ExprProcessWithPid.class, Process.class, ExpressionType.SIMPLE,
            "process with pid %string%"
        );
    }

    private Expression<String> pid;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        pid = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected Process[] get(Event e) {
        Optional<ProcessHandle> process = ProcessHandle.of(Long.parseLong(pid.getSingle(e)));
        return new Process[]{ (Process) process.map(p -> p).orElse(null) };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Process> getReturnType() {
        return Process.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "process with pid " + pid.toString(e, debug);
    }
    
}
