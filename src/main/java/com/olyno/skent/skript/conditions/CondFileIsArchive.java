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

@Name("File is archive?")
@Description("Checks if the file is an archive or not.")
@Examples({
    "command archive:\n"
    + "\ttrigger:\n"
    + "\t\tif file path \"plugins/Skript/scripts/myAwesomeGame.exe\" is archive file:\n"
    + "\t\t\tbroadcast \"Maybe is it the trash?\"" })
@Since("2.0")

public class CondFileIsArchive extends PropertyPathCondition<Path> {

    static {
        registerCondition(CondFileIsArchive.class,
            "archive", "path"
        );
    }

    @Override
    public boolean check(Path path) {
        if (Files.exists(path)) {
            try {
                DosFileAttributes attributes = Files.readAttributes(path, DosFileAttributes.class);
                return attributes.isArchive();
            } catch (IOException ex) {
                Skript.exception(ex);
            }
        }
        return false;
    }

    @Override
    protected String getPropertyName() {
        return "archive";
    }
    
}
