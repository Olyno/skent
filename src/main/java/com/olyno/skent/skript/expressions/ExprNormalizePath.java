package com.olyno.skent.skript.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;

import java.nio.file.Path;

@Name("Normalize Path")
@Description("Returns normalized path of file.")
@Examples({
    "command normalize:\n" +
        "\ttrigger:\n" +
        "\t\tset {_normalize} to normalize path of file path \"plugins/Skript/./config.sk\"\n" +
        "\t\tbroadcast \"The normalize path of the config file is %{_normalize}%\""
})
@Since("1.8")

public class ExprNormalizePath extends SimplePropertyExpression<Path, Path> {

    static {
        register(ExprNormalizePath.class, Path.class,
            "normalize[d] [path]", "path"
        );
    }

    @Override
    public Path convert(Path path) {
        return path.normalize();
    }

    @Override
    protected String getPropertyName() {
        return "normalize";
    }

    @Override
    public Class<? extends Path> getReturnType() {
        return Path.class;
    }
    
}
