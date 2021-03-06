package com.olyno.skent.skript.conditions;

import java.nio.file.Files;
import java.nio.file.Path;

import com.olyno.skent.util.skript.PropertyPathCondition;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;

@Name("File is writable?")
@Description("Checks if the file is writable or not.")
@Examples({
    "command readable:\n" +
        "\ttrigger:\n" +
        "\t\tif file path \"plugins/Skript/scripts/myAwesomeScript.sk\" is writable file:\n" +
        "\t\t\tbroadcast \"Sure!\""
})
@Since("1.0")

public class CondFileIsWritable extends PropertyPathCondition<Path> {

    static {
        register(CondFileIsWritable.class, PropertyType.BE,
            "writable", "path"
        );
        register(CondFileIsWritable.class, PropertyType.CAN,
            "written", "path"
        );
    }

    @Override
    public boolean check(Path path) {
        return Files.isWritable(path);
    }

    @Override
    protected String getPropertyName() {
        return "writable";
    }
    
}
