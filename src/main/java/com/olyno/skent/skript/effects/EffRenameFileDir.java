package com.olyno.skent.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;

import com.olyno.skent.skript.events.bukkit.ChangeEvent;
import com.olyno.skent.skript.events.bukkit.RenameEvent;
import com.olyno.skent.util.AsyncEffect;
import org.bukkit.event.Event;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Name("Rename File or directory")
@Description("Renames a file or a directory.")
@Examples({
    "command rename:\n" +
        "\ttrigger:\n" +
        "\t\trename file path \"plugins/Skript/scripts/test1.txt\" to \"secret.txt\"\n" +
        "\t\tbroadcast \"Now my file is secret!\"",

    "# If you need to wait the end of the effect before execute a part of your code, you can\n" +
    "# use this effect as a section effect." + 
    "# The code after this effect section will be executed when the effect section has finished to be executed.\n\n" +
    "command rename:\n" +
        "\ttrigger:\n" +
        "\t\trename file path \"plugins/Skript/scripts/test1.txt\" to \"secret.txt\":\n" +
        "\t\t\tbroadcast \"Now my file is secret!\"",
})
@Since("1.0")

public class EffRenameFileDir extends AsyncEffect {

    static {
        registerAsyncEffect(EffRenameFileDir.class,
            "rename %paths% to %string%",
            "rename %paths% to %string% with replace"
        );
    }

    private Expression<Path> paths;
    private Expression<String> name;
    private Boolean withReplace;
    private boolean isSingle;

    @Override
    @SuppressWarnings("unchecked")
    protected boolean initAsync(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        paths = (Expression<Path>) expr[0];
        name = (Expression<String>) expr[1];
        withReplace = matchedPattern == 1;
        isSingle = paths.getSource().isSingle();
        return true;
    }

    @Override
    protected void executeAsync(Event e) {
        Path[] pathsList = isSingle ? new Path[]{paths.getSingle(e)} : paths.getArray(e);
        String currentName = name.getSingle(e);
        for (Path path : pathsList) {
            if (Files.exists(path)) {
                try {
                    if (withReplace) {
                        Files.move(path, path.resolveSibling(currentName), StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        Files.move(path, path.resolveSibling(currentName));
                    }
                    new RenameEvent(path, currentName);
                    new ChangeEvent(path);
                } catch (IOException ex) {
                    if (Files.exists(path)) {
                        Skript.exception(ex);
                    }
                }
            }
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "rename " + paths.toString(e, debug) + " to " + name.toString(e, debug) + (withReplace ? " with" : " without") + " replace";
    }

}
