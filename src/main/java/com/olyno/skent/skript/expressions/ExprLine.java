package com.olyno.skent.skript.expressions;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.olyno.skent.skript.events.bukkit.ChangeEvent;

import org.bukkit.event.Event;

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

@Name("Line of file")
@Description(
    "Returns a specific line of a file.\n"
    + "Be carefull: when you remove a content from a line, it uses regex! So if you're using special chars as \":\" or \"()\" it will convert it in regex.\n"
    + "Uses \"\\\" before the character to avoid that."
)
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
            case DELETE:
            case REMOVE:
                return new Class[]{String.class};
            default:
                break;
        }
        return new Class[0];
    }

    @Override
    public void change(Event e, Object[] delta, ChangeMode mode) {
        Path currentPath = path.getSingle(e);
        int theLine = line != null ? line.getSingle(e).intValue() : 0;
        if (Files.isRegularFile(currentPath)) {
            try {
                List<String> lines = Files.readAllLines(currentPath, StandardCharsets.UTF_8);
                int lineToChange = theLine - 1;
                if (lineToChange > lines.size() || lineToChange < 0) return;

                ArrayList<String> changes = new ArrayList<String>();
                if (delta != null) {
                    for (Object o : delta) {
                        changes.add((String) o);
                    }
                }

                if (lineToChange == 0 && lines.size() == 0 && (mode == ChangeMode.ADD || mode == ChangeMode.SET)) {
                    lines.add(String.join("", changes));
                } else {
                    switch (mode) {

                        case SET:
                            if (lineToChange < 0 && line == null) {
                                lines.replaceAll(value -> String.join("", changes));
                            } else {
                                lines.set(lineToChange, String.join("", changes));
                            }
                            break;
        
                        case ADD:
                            if (lineToChange < 0 && line == null) {
                                lines.replaceAll(value -> value + String.join("", changes));
                            } else {
                                lines.set(lineToChange, lines.get(lineToChange) + String.join("", changes));
                            }
                            break;
        
                        case REMOVE:
                            if (lineToChange < 0 && line == null) {
                                lines.replaceAll(value -> value.replaceAll(String.join("|", changes), ""));
                            } else {
                                lines.set(lineToChange, lines.get(lineToChange).replaceAll(String.join("|", changes), ""));
                            }
                            break;

                        case DELETE:
                            if (lineToChange < 0 && line == null) {
                                lines.clear();
                            } else {
                                lines.remove(lineToChange);
                            }
                            break;
                        
                        default:
                            break;
                    }
                }
    
                Files.write(currentPath, lines, StandardCharsets.UTF_8);
                new ChangeEvent(currentPath);
    
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
