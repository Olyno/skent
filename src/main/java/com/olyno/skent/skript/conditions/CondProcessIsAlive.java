package com.olyno.skent.skript.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;

@Name("Process is alive?")
@Description("Checks if the process is still alive.")
@Examples({
    "command alive:\n" +
        "\ttrigger:\n" +
        "\t\tif process with pid \"54621\" is still running:\n" +
        "\t\t\tbroadcast \"It still works mwahahah!\""
})
@Since("1.0")

public class CondProcessIsAlive extends PropertyCondition<Process> {

    static {
        register(CondProcessIsAlive.class,
            "(alive|[still] running)", "processes"
        );
    }

    @Override
    public boolean check(Process process) {
        return process.isAlive();
    }

    @Override
    protected String getPropertyName() {
        return "alive";
    }
    
}
