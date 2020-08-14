package com.olyno.skent.skript.expressions;

import java.nio.file.Path;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;

@Name("File/Directory name")
@Description("Returns the name path of a file or directory.")
@Examples({
    "command name:\n" +
        "\ttrigger:\n" +
        "\t\tset {_name} to file name of file path \"plugins/Skript/config.sk\"\n" +
        "\t\tbroadcast \"The name of the config file is %{_path}%\" (of course lol)"
})
@Since("1.0")

public class ExprFileName extends SimplePropertyExpression<Path, String> {

    static {
        register(ExprFileName.class, String.class,
            "(file|dir[ectory]) name", "path"
        );
    }

    @Override
    public String convert(Path file) {
        return file.getFileName().toString();
    }

    @Override
    protected String getPropertyName() {
        return "file name";
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
    
}
