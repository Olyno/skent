package com.olyno.skent.skript.expressions;

import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;

@Name("Fetched Url Content")
@Description("Returns the content from the latest fetched url. Can only be used in a fetch scope.")
@Examples({
    "command content:\n" +
        "\ttrigger:\n" +
        "\t\tset {_content} to fetched url content\n" +
        "\t\tbroadcast \"Yep, it's the README of Skript's github:\"\n" +
        "\t\tbroadcast {_content}"
})
@Since("1.0")

public class ExprFetchedUrlContent extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprFetchedUrlContent.class, String.class, ExpressionType.SIMPLE,
            "[the] fetched url content"
        );
    }

    public static String content;

    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected String[] get(Event e) {
        return new String[]{ content };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "fetched content url";
    }
    
}
