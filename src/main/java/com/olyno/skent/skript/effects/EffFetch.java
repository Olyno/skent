package com.olyno.skent.skript.effects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.event.Event;

import com.olyno.skent.skript.expressions.ExprFetchedUrlContent;
import com.olyno.skent.util.skript.AsyncEffect;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;

@Name("Fetch Url")
@Description("Fetch an url asynchronously. Able you to get an huge content and store it in a file later.")
@Examples({
    "command fetch:\n" +
        "\ttrigger:\n" +
        "\t\tfetch url \"https://raw.githubusercontent.com/SkriptLang/Skript/master/README.md\"\n" +
        "\t\tbroadcast fetched url content"
})
@Since("1.0")

public class EffFetch extends AsyncEffect {

    static {
        registerAsyncEffect(EffFetch.class,
            "fetch (url|link) %string%"
        );
    }

    private Expression<String> link;

    @Override
    @SuppressWarnings("unchecked")
    protected boolean initAsync(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        link = (Expression<String>) expr[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String url = link.getSingle(e);
        try {
            ExprFetchedUrlContent.content = readToString(url);
        } catch (IOException ex) {
            Skript.exception(ex);
        }
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
    public String toString(Event e, boolean debug) {
        return "fetch url " + link.toString(e, debug);
    }

}
