package com.olyno.skent.skript.effects;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.bukkit.event.Event;

import com.olyno.skent.skript.events.bukkit.ChangeEvent;
import com.olyno.skent.util.skript.AsyncEffect;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;

@Name("Insert Line")
@Description("Inserts a line in a file.")
@Examples({
    "command insert:\n" +
        "\ttrigger:\n" +
        "\t\tcreate file path \"plugins/Skript/scripts/test1.txt\" with text \"Hey\", \"I'm\" and \"nice to meet you!\"\n" +
        "\t\tinsert \"the creator\" at line 2 of file path \"plugins/Skript/scripts/test1.txt\"\n" +
        "\t\tbroadcast \"Of course I'm the creator!\"",

    "# If you need to wait the end of the effect before execute a part of your code, you can\n" +
    "# use this effect as a section effect.\n" + 
    "# The code after this effect section will be executed when the effect section has finished to be executed.\n\n" +
    "command insert:\n" +
        "\ttrigger:\n" +
        "\t\tcreate file path \"plugins/Skript/scripts/test1.txt\" with text \"Hey\", \"I'm\" and \"nice to meet you!\":\n" +
        "\t\t\tinsert \"the creator\" at line 2 of file path \"plugins/Skript/scripts/test1.txt\"\n" +
        "\t\t\tbroadcast \"Of course I'm the creator!\""
})
@Since("1.0")

public class EffInsertLine extends AsyncEffect {

    static {
        registerAsyncEffect(EffInsertLine.class,
            "insert %strings% at line %number% of %path%"
        );
    }

    private Expression<String> content;
    private Expression<Number> line;
    private Expression<Path> paths;

    @Override
    @SuppressWarnings("unchecked")
    protected boolean initAsync(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        content = (Expression<String>) expr[0];
        line = (Expression<Number>) expr[1];
        paths = (Expression<Path>) expr[2];
        return true;
    }

    @Override
    protected void executeAsync(Event e) {
        Path[] pathsList = paths.getArray(e);
        int theLine = line.getSingle(e).intValue();
        String currentContent = content.getSingle(e);
        for (Path path : pathsList) {
            try {
                List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                if (lines.size() < theLine) {
                    for (int i = lines.size(); i < theLine; i++) {
                        lines.add("");
                    }
                }
                lines.add(theLine - 1, currentContent);
                Files.write(path, lines, StandardCharsets.UTF_8);
                new ChangeEvent(path);
            } catch (IOException ex) {
                if (!Files.exists(path)) {
                    Skript.exception(ex, "This file doesn't exist: " + path);
                } else {
                    Skript.exception(ex);
                }
            }
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "insert \"" + content.toString(e, debug) + "\" at line " + line.toString(e, debug) + " of " + paths.toString(e, debug);
    }
    
}
