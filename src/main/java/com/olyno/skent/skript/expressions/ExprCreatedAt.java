package com.olyno.skent.skript.expressions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.Date;

@Name("Creation date of File/Directory")
@Description("Returns the date of the creation of a file or directory.")
@Examples({
    "command created at:\n" +
        "\ttrigger:\n" +
        "\t\tset {_date} to creation time of file path \"plugins/Skript/scripts/MyAwesomeScript.sk\"\n" +
        "\t\tbroadcast \"Oh, I recently created this script, awesome!: %{_date}%\""
})
@Since("2.0")

public class ExprCreatedAt extends SimplePropertyExpression<Path, Date> {

    static {
        register(ExprCreatedAt.class, Date.class,
            "creat(ed|ion) (date|time)", "path"
        );
    }

    @Override
    public Date convert(Path path) {
        if (Files.exists(path)) {
            try {
                FileTime creationTime = (FileTime) Files.getAttribute(path, "creationTime");
                return new Date( creationTime.toMillis() );
            } catch (IOException ex) {
                Skript.exception(ex);
            }
        }
        return new Date();
    }

    @Override
    protected String getPropertyName() {
        return "creation time";
    }

    @Override
    public Class<? extends Date> getReturnType() {
        return Date.class;
    }
    
}
