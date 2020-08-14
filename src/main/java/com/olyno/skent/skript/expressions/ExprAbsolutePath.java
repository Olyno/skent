package com.olyno.skent.skript.expressions;

import java.nio.file.Path;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;

@Name("Absolute path")
@Description("Returns the absolute path of a file.")
@Examples({
    "command absolute:\n" +
        "\ttrigger:\n" +
        "\t\tset {_path} to absolute path of file path \"plugins/Skript/config.sk\"\n" +
        "\t\tbroadcast \"The path of the config file is %{_path}%\""
})
@Since("1.0")

public class ExprAbsolutePath extends SimplePropertyExpression<Path, Path> {

    static {
        register(ExprAbsolutePath.class, Path.class,
            "absolute path", "path"
        );
    }

    @Override
    public Path convert(Path path) {
        return path.toAbsolutePath();
    }

    @Override
    protected String getPropertyName() {
        return "absolute path";
    }

    @Override
    public Class<? extends Path> getReturnType() {
        return Path.class;
    }
    
}
