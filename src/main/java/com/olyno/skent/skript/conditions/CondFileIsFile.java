package com.olyno.skent.skript.conditions;

import java.nio.file.Files;
import java.nio.file.Path;

import com.olyno.skent.util.PriorityPropertyCondition;
import com.olyno.skent.util.Utils;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;

@Name("File is a file?")
@Description("Checks if the file is a file or not.")
@Examples({
    "command file:\n" +
        "\ttrigger:\n" +
        "\t\tif file path \"plugins/Skript/scripts/myAwesomeScript.sk\" is a file:\n" +
        "\t\t\tbroadcast \"Sure!\""
})
@Since("1.0")

public class CondFileIsFile extends PriorityPropertyCondition<Path> {

    static {
        registerCondition(CondFileIsFile.class,
            "[a] file", "path"
        );
    }

    @Override
    public boolean check(Path path) {
        return Files.exists(path) ? Files.isRegularFile(path) : !Utils.isDirectory(path);
    }

    @Override
    protected String getPropertyName() {
        return "file";
    }
    
}
