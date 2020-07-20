package com.olyno.skent.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.olyno.skent.skript.events.bukkit.ChangeEvent;

@Name("Line of file")
@Description("Returns a specific line of a file.")
@Examples({
    "command line:\n" +
        "\ttrigger:\n" +
        "\t\tset line 1 of file path \"plugins/Skript/scripts/myAwesomeScript.sk\" to \"command awesometest\"\n" +
        "\t\tadd \":\" to line 1 of file path \"plugins/Skript/scripts/myAwesomeScript.sk\"\n" +
        "\t\tremove \"test\" from line 1 of file path \"plugins/Skript/scripts/myAwesomeScript.sk\"\n" +
        "\t\tbroadcast line 1 of file path \"plugins/Skript/scripts/myAwesomeScript.sk\""
})
@Since("1.0")

public class ExprLine extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprLine.class, String.class, ExpressionType.SIMPLE,
            "[the] line %number% (from|of|in) %path%",
            "[all] [the] lines (from|of|in) %path%"
        );
    }

    private Expression<Number> line;
    private Expression<Path> path;
    private Boolean allLines = false;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (matchedPattern == 0) {
            line = (Expression<Number>) expr[0];
            path = (Expression<Path>) expr[1];
        } else {
            path = (Expression<Path>) expr[0];
            allLines = true;
        }
        return true;
    }

    @Override
    protected String[] get(Event e) {
        Path currentPath = path.getSingle(e);
        long theLine = line != null ? line.getSingle(e).longValue() : 0;
        if (Files.isRegularFile(currentPath)) {
            try {
                if (allLines) return Files.readAllLines(currentPath).toArray(new String[0]);
                return new String[]{Files.lines(currentPath).skip(theLine - 1).findFirst().get()};
            } catch (IOException ex) {
                Skript.exception(ex);
            }
        }
        return new String[0];
    }

    @Override
    public Class<?>[] acceptChange(final ChangeMode mode) {
        switch (mode) {
            case SET:
            case ADD:
            case REMOVE:
                return new Class[]{String.class};
            default:
                break;
        }
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, ChangeMode mode) {
        Path currentPath = path.getSingle(e);
        int theLine = line != null ? line.getSingle(e).intValue() : 0;
        if (Files.isRegularFile(currentPath)) {
            try {
                List<String> lines = Files.readAllLines(currentPath, StandardCharsets.UTF_8);
                if (lines.size() < theLine) {
                    for (int i = lines.size(); i < theLine; i++) {
                        lines.add("");
                    }
                }
    
                switch (mode) {
    
                    case SET:
                        lines.set(theLine - 1, (String) delta[0]);
                        new ChangeEvent(currentPath);
                        break;
    
                    case ADD:
                        lines.set(theLine - 1, lines.get(theLine - 1) + delta[0]);
                        new ChangeEvent(currentPath);
                        break;
    
                    case REMOVE:
                        lines.set(theLine - 1, lines.get(theLine - 1).replaceAll((String) delta[0], ""));
                        new ChangeEvent(currentPath);
                        break;
                    
                    default:
                        break;
                }
    
                Files.write(currentPath, lines, StandardCharsets.UTF_8);
    
            } catch (IOException ex) {
                Skript.exception(ex);
            }
        }

    }

    @Override
    public boolean isSingle() {
        return !allLines;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return (allLines ? "all lines" : "line " + line.toString(e, debug)) + " of " + path.toString(e, debug);
    }

}
