package com.olyno.skent.skript.conditions;

import java.io.IOException;
import java.nio.file.Path;

import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.lingala.zip4j.ZipFile;

@Name("File Is Encrypted?")
@Description("Checks if the file is encrypted.")
@Examples({
    "command exists:\n" +
        "\ttrigger:\n" +
        "\t\tif file path \"plugins/Skript/scripts/all_my_secret_assets.zip\" is encrypted:\n" +
        "\t\t\tbroadcast \"No one will have access to them!!!\""
})
@Since("3.3.0")

public class CondZipIsEncrypted extends Condition {

    static {
        Skript.registerCondition(CondZipIsEncrypted.class,
            "%path% is encrypted",
            "%path% is(n't| not) encrypted"
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
        final ZipFile zipeFile = new ZipFile(path.getSingle(e).toFile());
        boolean isEncrypted = isNegated();
        try {
        isEncrypted = isNegated() != zipeFile.isEncrypted();
        zipeFile.close();
        } catch (IOException e1) {}
        return isEncrypted;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return path.toString(e, debug) + " is " + (isNegated() ? " not" : " ") + "encrypted";
    }

}
