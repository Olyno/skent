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

@Name("Last Access date of File/Directory")
@Description("Returns the date of the last access of a file or directory.")
@Examples({
    "command last access:\n" +
        "\ttrigger:\n" +
        "\t\tset {_date} to last access time of file path \"passwords.txt\"\n" +
        "\t\tbroadcast \"An hacker saw my file recently!: %{_date}%\""
})
@Since("2.0")

public class ExprLastAccess extends SimplePropertyExpression<Path, Date> {

    static {
        register(ExprLastAccess.class, Date.class,
            "last access (date|time)", "path"
        );
    }

    @Override
    public Date convert(Path path) {
        if (Files.exists(path)) {
            try {
                FileTime lastAccessTime = (FileTime) Files.getAttribute(path, "lastAccessTime");
                return new Date( lastAccessTime.toMillis() );
            } catch (IOException ex) {
                Skript.exception(ex);
            }
        }
        return new Date();
    }

    @Override
    protected String getPropertyName() {
        return "last access time";
    }

    @Override
    public Class<? extends Date> getReturnType() {
        return Date.class;
    }
    
}
