package com.olyno.skent.skript.effects;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

import org.bukkit.event.Event;

import com.olyno.skent.skript.events.bukkit.ChangeEvent;
import com.olyno.skent.skript.events.bukkit.CreateEvent;
import com.olyno.skent.util.Utils;
import com.olyno.skent.util.skript.AsyncEffect;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;

@Name("Create File or directory")
@Description("Creates a file or a directory. If the file exists, then it will be replaced by the new one.")
@Examples({
    "command create:\n" +
        "\ttrigger:\n" +
        "\t\tcreate file path \"plugins/Skript/scripts/myAwesomeScript.sk\" with text \"command awesome:\", \"\ttrigger:\" and \"\t\tbroadcast \"\"Awesome!!!\"\"\"\n" +
        "\t\tbroadcast \"Created!\"",

    "# If you need to wait the end of the effect before execute a part of your code, you can\n" +
    "# use this effect as a section effect.\n" + 
    "# The code after this effect section will be executed when the effect section has finished to be executed.\n\n" +
    "command create:\n" +
        "\ttrigger:\n" +
        "\t\tcreate file path \"plugins/Skript/scripts/myAwesomeScript.sk\" with text \"command awesome:\", \"\ttrigger:\" and \"\t\tbroadcast \"\"Awesome!!!\"\"\":\n" +
        "\t\t\tbroadcast \"Created!\""
})
@Since("1.0")

public class EffCreateFileDir extends AsyncEffect {

    static {
        registerAsyncEffect(EffCreateFileDir.class,
            "create %paths%",
            "create %paths% with [(text|string|content)] %strings%"
        );
    }

    private Expression<Path> paths;
    private Expression<String> content;

    @Override
    @SuppressWarnings("unchecked")
    protected boolean initAsync(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        paths = (Expression<Path>) expr[0];
        if (matchedPattern == 1) {
            content = (Expression<String>) expr[1];
        }
        return true;
    }

    @Override
    protected void executeAsync(Event e) {
        Path[] pathsList = paths.getArray(e);
        String[] currentContent = content != null ? content.getArray(e) : new String[]{""};
        for (Path path : pathsList) {
            try {
                if (!Utils.isDirectory(path)) {
                    Files.createDirectories(path.getParent());
                    if (content != null) {
                        List<String> lines = Arrays.asList(currentContent);
                        Files.write(path, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
                    } else if (!Files.exists(path)) {
                        Files.createFile(path);
                    }
                } else if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }
                new CreateEvent(path, currentContent);
                new ChangeEvent(path.getParent());
            } catch (IOException ex) {
                if (!Files.exists(path)) {
                    Skript.exception(ex);
                }
            }
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "create " + paths.toString(e, debug);
    }

}
