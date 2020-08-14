package com.olyno.skent.skript.expressions;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;

@Name("Total Space")
@Description("Returns the total space of a directory in bytes.")
@Examples({
    "command space:\n"
        + "\ttrigger:\n"
        + "\t\tset {_space} to total space of directory path \"plugins/Skript\"\n"
        + "\t\tbroadcast \"Wow I should work more on my script, I have enought space available: %{_space}%\"" })
@Since("2.0")

public class ExprTotalSpace extends SimplePropertyExpression<Path, Number> {

    static {
        register(ExprTotalSpace.class, Number.class,
            "total space", "path"
        );
    }

    @Override
    public Number convert(Path path) {
        if (Files.exists(path)) {
            try {
                FileStore store = Files.getFileStore(path);
                return store.getTotalSpace();
            } catch (IOException ex) {
                Skript.exception(ex);
            }
        }
        return 0;
    }

    @Override
    protected String getPropertyName() {
        return "total space";
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }
    
}
