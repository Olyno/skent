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
import com.olyno.skent.skript.events.bukkit.MoveEvent;
import com.olyno.skent.util.AsyncEffect;

import org.bukkit.event.Event;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Name("Move File or directory")
@Description("Moves a file or a directory.")
@Examples({
    "command move:\n" +
        "\ttrigger:\n" +
        "\t\tmove file path \"plugins/Skript/config.sk\" to file path \"plugins/config.sk\"\n" +
        "\t\tbroadcast \"Got a new config!\"",

    "# If you need to wait the end of the effect before execute a part of your code, you can\n" +
    "# use this effect as a section effect." + 
    "# The code after this effect section will be executed when the effect section has finished to be executed.\n\n" +
    "command move:\n" +
        "\ttrigger:\n" +
        "\t\tmove file path \"plugins/Skript/config.sk\" to file path \"plugins/config.sk\":\n" +
        "\t\t\tbroadcast \"Got a new config!\""  
})
@Since("1.0")

public class EffMoveFileDir extends AsyncEffect {

    static {
        registerAsyncEffect(EffMoveFileDir.class,
            "move %paths% to %path%",
            "move %paths% to %path% with replace"
        );
    }

    private Expression<Path> paths;
    private Expression<Path> target;
    private Boolean withReplace = false;
    private boolean isSingle;

    @Override
    @SuppressWarnings("unchecked")
    protected boolean initAsync(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        paths = (Expression<Path>) expr[0];
        target = (Expression<Path>) expr[1];
        withReplace = matchedPattern == 1;
        isSingle = paths.getSource().isSingle();
        return true;
    }

    @Override
    protected void executeAsync(Event e) {
        Path[] pathsList = isSingle ? new Path[]{paths.getSingle(e)} : paths.getArray(e);
        Path targetFile = target.getSingle(e);
        for (Path path : pathsList) {
            if (Files.isDirectory(targetFile)) {
                targetFile = Paths.get(targetFile.toString() + File.pathSeparator + path.getFileName()).normalize();
            }
            Path destFile = targetFile;
            try {
                if (withReplace) {
                    Files.move(path, destFile, StandardCopyOption.REPLACE_EXISTING);
                } else {
                    Files.move(path, destFile);
                }
                new MoveEvent(path, destFile);
                new ChangeEvent(destFile.getParent());
                new ChangeEvent(path);
            } catch (IOException ex) {
                Skript.exception(ex);
            }
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "move " + paths.toString(e, debug) + " to " + target.toString(e, debug);
    }

}
