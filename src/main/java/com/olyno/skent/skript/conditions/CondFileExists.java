package com.olyno.skent.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

import java.nio.file.Files;
import java.nio.file.Path;

@Name("File or directory exists?")
@Description("Checks if the file or directory exists or not.")
@Examples({
    "command exists:\n" +
        "\ttrigger:\n" +
        "\t\tif file path \"plugins/Skript/scripts/myAwesomeScript.sk\" is not missing:\n" +
        "\t\t\tbroadcast \"Awesome!\""
})
@Since("1.0")

public class CondFileExists extends Condition {

    static {
        Skript.registerCondition(CondFileExists.class,
            "%path% (is available|is(n't| not) (missing|non[(-| )]existent))",
            "%path% (is (missing|non[(-| )]existent)|is(n't| not) available)"
        );

    }

    private Expression<Path> path;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        path = (Expression<Path>) expr[0];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(Event e) {
        return isNegated() != Files.exists(path.getSingle(e));
    }

    @Override
    public String toString(Event e, boolean debug) {
        return path.toString(e, debug) + " is " + (isNegated() ? " missing" : " available");
    }

}
