package com.olyno.skent.skript.expressions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;

@Name("Owner of File/Directory")
@Description("Returns the owner of a file or directory.")
@Examples({
    "command owner:\n" +
        "\ttrigger:\n" +
        "\t\tset {_owner} to owner of file path \"plugins/Skript/config.sk\"\n" +
        "\t\tbroadcast \"Hey boi, look I'm my computer is the owner: %{_owner}%\""
})
@Since("1.0")

public class ExprOwner extends SimplePropertyExpression<Path, String> {

    static {
        register(ExprOwner.class, String.class,
            "[file] owner", "path"
        );
    }

    @Override
    public String convert(Path path) {
        if (Files.exists(path)) {
            try {
                return Files.getOwner(path).getName();
            } catch (IOException ex) {
                Skript.exception(ex);
            }
        }
        return null;
    }

    @Override
    protected String getPropertyName() {
        return "owner";
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
    
}
