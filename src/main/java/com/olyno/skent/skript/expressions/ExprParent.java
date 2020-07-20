package com.olyno.skent.skript.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;

import java.nio.file.Path;

@Name("Parent of File")
@Description("Returns the parent of a file.")
@Examples({
    "command parent:\n" +
        "\ttrigger:\n" +
        "\t\tset {_parent} to parent of file path \"plugins/Skript/config.sk\"\n" +
        "\t\tbroadcast \"The parent of the config file is %{_parent}%\""
})
@Since("1.0")

public class ExprParent extends SimplePropertyExpression<Path, Path> {

    static {
        register(ExprParent.class, Path.class,
            "parent [path]", "path"
        );
    }

    @Override
    public Path convert(Path path) {
        return path.getParent();
    }

    @Override
    protected String getPropertyName() {
        return "parent";
    }

    @Override
    public Class<? extends Path> getReturnType() {
        return Path.class;
    }
    
}
