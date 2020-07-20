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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

@Name("File/Directory")
@Description("Returns a file or directory from its path. Can return a list of file.")
@Examples({
    "command create:\n" +
        "\ttrigger:\n" +
        "\t\tcreate file path \"plugins/Skript/scripts/test1.txt\" with \"hey\", \"just\", \"a\" and \"test\"\n" +
        "\t\tremove \"hey\" from file path \"plugins/Skript/scripts/test1.txt\"\n" +
        "\t\tadd \"with Skent\" to file path \"plugins/Skript/scripts/test1.txt\"\n" +
        "\t\tbroadcast \"Finished!\""
})
@Since("1.0")

public class ExprFileDirectory extends SimpleExpression<Path> {

    static {
        Skript.registerExpression(ExprFileDirectory.class, Path.class, ExpressionType.SIMPLE,
            "[the] (file[s]|dir[ector(y|ies)]) path %strings%"
        );
    }

    private Expression<String> paths;
    private boolean isSingle;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        paths = (Expression<String>) expr[0];
        isSingle = paths.getSource().isSingle();
        return true;
    }

    @Override
    protected Path[] get(Event e) {
        LinkedList<Path> finalPaths = new LinkedList<>();
        String[] pathsList = paths.getArray(e);
        for (String path : pathsList) {
            finalPaths.add(Paths.get(path));
        }
        return finalPaths.toArray(new Path[finalPaths.size()]);
    }

    @Override
    public boolean isSingle() {
        return isSingle;
    }

    @Override
    public Class<? extends Path> getReturnType() {
        return Path.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "file/directory " + paths.toString(e, debug);
    }
    
}
