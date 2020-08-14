package com.olyno.skent.skript.conditions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.olyno.skent.util.PropertyPathCondition;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;

@Name("File is hidden?")
@Description("Checks if the file is hidden or not.")
@Examples({
    "command hidden:\n" +
        "\ttrigger:\n" +
        "\t\tif file path \"plugins/twitch.txt\" is hidden file:\n" +
        "\t\t\tbroadcast \"I'm sneeeaaaakyy!\""
})
@Since("1.0")

public class CondFileIsHidden extends PropertyPathCondition<Path> {

    static {
        registerCondition(CondFileIsHidden.class,
            "hidden", "path"
        );
    }

    @Override
    public boolean check(Path path) {
        if (Files.exists(path)) {
            try {
                return Files.isHidden(path);
            } catch (IOException ex) {
                Skript.exception(ex);
            }
        }
        return false;
    }

    @Override
    protected String getPropertyName() {
        return "hidden";
    }
    
}
