package com.olyno.skent.skript.conditions;

import java.nio.file.Files;
import java.nio.file.Path;

import com.olyno.skent.util.Utils;
import com.olyno.skent.util.skript.PriorityPropertyCondition;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;

@Name("File is a directory?")
@Description("Checks if the file is a directory or not.")
@Examples({
    "command dir:\n" +
        "\ttrigger:\n" +
        "\t\tif file path \"plugins/Skript/scripts/myAwesomeScript.sk\" is a dir:\n" +
        "\t\t\tbroadcast \"Nah!\""
})
@Since("1.0")

public class CondFileIsDir extends PriorityPropertyCondition<Path> {

    static {
        registerCondition(CondFileIsDir.class,
            "[a] dir[ectory]", "path"
        );
    }

    @Override
    public boolean check(Path path) {
        return Files.exists(path) ? Files.isDirectory(path) : Utils.isDirectory(path);
    }

    @Override
    protected String getPropertyName() {
        return "directory";
    }

}
