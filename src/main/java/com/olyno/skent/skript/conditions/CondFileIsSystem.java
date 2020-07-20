package com.olyno.skent.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;

import com.olyno.skent.util.PropertyPathCondition;

@Name("File is System?")
@Description("Checks if the file is a system or not.")
@Examples({
    "command system:\n"
        + "\ttrigger:\n"
        + "\t\tif file path \"plugins/Skript/scripts/myAwesomeGame.exe\" is system file:\n"
        + "\t\t\tbroadcast \"Oh, probably the bios!\"" })
@Since("2.0")

public class CondFileIsSystem extends PropertyPathCondition<Path> {

    static {
        registerCondition(CondFileIsSystem.class,
            "system", "path"
        );
    }

    @Override
    public boolean check(Path path) {
        if (Files.exists(path)) {
            try {
                DosFileAttributes attributes = Files.readAttributes(path, DosFileAttributes.class);
                return attributes.isSystem();
            } catch (IOException ex) {
                Skript.exception(ex);
            }
        }
        return false;
    }

    @Override
    protected String getPropertyName() {
        return "system";
    }
    
}
