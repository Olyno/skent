package com.olyno.skent.skript.conditions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;

import java.nio.file.Files;
import java.nio.file.Path;

import com.olyno.skent.util.PropertyPathCondition;

@Name("File is readable?")
@Description("Checks if the file is readable or not.")
@Examples({
    "command readable:\n" +
        "\ttrigger:\n" +
        "\t\tif file path \"plugins/Skript/scripts/myAwesomeScript.sk\" is readable file:\n" +
        "\t\t\tbroadcast \"Sure!\""
})
@Since("1.0")

public class CondFileIsReadable extends PropertyPathCondition<Path> {

    static {
        registerCondition(CondFileIsReadable.class,
            "readable", "path"
        );
    }

    @Override
    public boolean check(Path path) {
        return Files.isReadable(path);
    }

    @Override
    protected String getPropertyName() {
        return "readable";
    }
    
}
