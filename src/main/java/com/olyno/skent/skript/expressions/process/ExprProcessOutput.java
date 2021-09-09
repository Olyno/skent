package com.olyno.skent.skript.expressions.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;

@Name("Process Output")
@Description("Returns the output of a process.")
@Examples({
    "on file executed:\n" +
        "\tset {_output} to output of event-process\n" +
        "\tbroadcast \"Hello %{_output}%\" # Hello World"
})
@Since("2.2.0")

public class ExprProcessOutput extends SimplePropertyExpression<Process, String> {

    static {
        register(ExprProcessOutput.class, String.class,
            "(output|log)[s]", "processes"
        );
    }

    @Override
    public String convert(Process process) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line = null;
        try {
            while ( (line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
        } catch (IOException ex) {
            Skript.exception(ex);
        }
        return builder.toString();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "output";
    }
  
}
