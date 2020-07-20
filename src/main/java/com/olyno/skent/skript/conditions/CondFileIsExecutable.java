package com.olyno.skent.skript.conditions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;

import java.nio.file.Files;
import java.nio.file.Path;

import com.olyno.skent.util.PropertyPathCondition;

@Name("File is executable?")
@Description("Checks if the file is executable or not.")
@Examples({
    "command executable:\n" +
        "\ttrigger:\n" +
        "\t\tif file path \"plugins/Skript/scripts/myAwesomeGame.exe\" is executable file:\n" +
        "\t\t\tbroadcast \"Oh, probably a game!\""
})
@Since("1.0")

public class CondFileIsExecutable extends PropertyPathCondition<Path> {

    static {
        registerCondition(CondFileIsExecutable.class,
            "exe[cutable]", "path"
        );
    }

    @Override
    public boolean check(Path path) {
        return Files.isExecutable(path);
    }

    @Override
    protected String getPropertyName() {
        return "executable";
    }
    
}
