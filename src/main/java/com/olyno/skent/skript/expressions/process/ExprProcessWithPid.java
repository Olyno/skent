package com.olyno.skent.skript.expressions.process;

import java.util.ArrayList;
import java.util.Optional;

import com.olyno.skent.skript.effects.process.EffExecuteFile;

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

@Name("Process")
@Description("Returns the process from its PID or the last started one.")
@Examples({
    "set {_process} to process with pid \"65632\"\n" +
    "broadcast \"Here is the pid of the running process: %pid of {_process}%\""
})
@Since("2.2.0")

public class ExprProcessWithPid extends SimpleExpression<Process> {

    static {
        Skript.registerExpression(ExprProcessWithPid.class, Process.class, ExpressionType.SIMPLE,
            "process with pid[s] %strings%",
            "last [started] process"
        );
    }

    private Expression<String> pid;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        if (matchedPattern == 0) {
            pid = (Expression<String>) exprs[0];
        }
        return true;
    }

    @Override
    protected Process[] get(Event e) {
        if (pid == null) {
            return new Process[] { EffExecuteFile.lastProcess };
        }
        String[] pids = pid.getArray(e);
        ArrayList<Process> processes = new ArrayList<>();
        for (String pid : pids) {
            Optional<ProcessHandle> process = ProcessHandle.of(Long.parseLong(pid));
            processes.add((Process) process.orElse(null));
        }
        return processes.toArray(new Process[0]);
    }

    @Override
    public boolean isSingle() {
        return pid == null || pid.isSingle();
    }

    @Override
    public Class<? extends Process> getReturnType() {
        return Process.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return pid == null ? "last process" : "process with pid " + pid.toString(e, debug);
    }
    
}
