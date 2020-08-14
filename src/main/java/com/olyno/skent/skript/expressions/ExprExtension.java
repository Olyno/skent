package com.olyno.skent.skript.expressions;

import java.nio.file.Path;

import com.google.common.io.Files;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;

@Name("Extension of File")
@Description("Returns the extension of a file.")
@Examples({
    "command extension:\n" +
        "\ttrigger:\n" +
        "\t\tset {_extension} to extension of file path \"plugins/Skript/config.sk\"\n" +
        "\t\tbroadcast \"The extension of the config file is %{_extension}%\""
})
@Since("1.0")

public class ExprExtension extends SimplePropertyExpression<Path, String> {

    static {
        register(ExprExtension.class, String.class,
            "extension", "path"
        );
    }

    @Override
    public String convert(Path path) {
        return Files.getFileExtension(path.toString());
    }

    @Override
    protected String getPropertyName() {
        return "extension";
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

}
