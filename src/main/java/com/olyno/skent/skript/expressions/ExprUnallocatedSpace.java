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

@Name("Unallocated Space")
@Description("Returns the unallocated space of a directory in bytes.")
@Examples({
    "command space:\n"
        + "\ttrigger:\n"
        + "\t\tset {_space} to unallocated space of directory path \"plugins/Skript\"\n"
        + "\t\tbroadcast \"I should allocate more resource: %{_space}%\"" })
@Since("2.0")

public class ExprUnallocatedSpace extends SimplePropertyExpression<Path, Number> {

    static {
        register(ExprUnallocatedSpace.class, Number.class,
            "unallocated space", "path"
        );
    }

    @Override
    public Number convert(Path path) {
        if (Files.exists(path)) {
            try {
                FileStore store = Files.getFileStore(path);
                return store.getUnallocatedSpace();
            } catch (IOException ex) {
                Skript.exception(ex);
            }
        }
        return 0;
    }

    @Override
    protected String getPropertyName() {
        return "unallocated space";
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }
    
}
