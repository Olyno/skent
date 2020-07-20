package com.olyno.skent.skript.expressions;

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
import org.bukkit.event.Event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Name("Content from url")
@Description("Returns the content of a url.")
@Examples({
    "command content:\n" +
        "\ttrigger:\n" +
        "\t\tset {_content} to content from url \"https://raw.githubusercontent.com/SkriptLang/Skript/master/README.md\"\n" +
        "\t\tbroadcast \"Yep, it's the README of Skript's github:\"\n" +
        "\t\tbroadcast {_content}"
})
@Since("1.0")

public class ExprContentFromURL extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprContentFromURL.class, String.class, ExpressionType.SIMPLE,
            "[the] content from url %string%"
        );
    }

    private Expression<String> url;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        url = (Expression<String>) expr[0];
        return true;
    }

    @Override
    protected String[] get(Event e) {
        try {
            return new String[]{readToString(url.getSingle(e))};
        } catch (IOException ex) {
            Skript.exception(ex);
        }
        return new String[0];
    }

    private String readToString(String targetURL) throws IOException {
        URL url = new URL(targetURL);
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(url.openStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null) {
            stringBuilder.append(inputLine);
            stringBuilder.append(System.lineSeparator());
        }
        bufferedReader.close();
        return stringBuilder.toString().trim();
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
        return "content from " + url.toString(e, debug);
    }
    
}
