package com.olyno.skent.skript.expressions.process;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;

@Name("Process PID")
@Description("Returns the pid of a process.")
@Examples({
    "on file executed:\n" +
        "\tset {_pid} to pid of event-process\n" +
        "\tbroadcast \"Here is the pid of the running process: %{_pid}%\""
})
@Since("2.2.0")

public class ExprProcessPid extends SimplePropertyExpression<Process, String> {

    static {
        register(ExprProcessPid.class, String.class,
            "pid", "processes"
        );
    }

    @Override
    public String convert(Process process) {
        return "" + process.pid();
    }

    @Override
    protected String getPropertyName() {
        return "pid";
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
    
}
