package com.olyno.skent.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.Date;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Name("Last modified of File/Directory")
@Description("Returns the date of the last modified date of a file or directory.")
@Examples({
    "command last modified:\n" +
        "\ttrigger:\n" +
        "\t\tset {_date} to last modified time of file path \"plugins/Skript\"\n" +
        "\t\tbroadcast \"Oh, I recently coded in Skript: %{_date}%\""
})
@Since("2.0")

public class ExprLastModified extends SimplePropertyExpression<Path, Date> {

    static {
        register(ExprLastModified.class, Date.class,
            "last modified (date|time)", "path"
        );
    }

    @Override
    public Date convert(Path path) {
        if (Files.exists(path)) {
            try {
                return new Date( Files.getLastModifiedTime(path).toMillis() );
            } catch (IOException ex) {
                Skript.exception(ex);
            }
        }
        return new Date();
    }

    @Override
    protected String getPropertyName() {
        return "last modified time";
    }

    @Override
    public Class<? extends Date> getReturnType() {
        return Date.class;
    }
    
}
