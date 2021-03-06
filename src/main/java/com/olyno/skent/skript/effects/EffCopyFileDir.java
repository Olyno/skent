package com.olyno.skent.skript.effects;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import com.olyno.skent.skript.events.bukkit.ChangeEvent;
import com.olyno.skent.skript.events.bukkit.CopyEvent;
import com.olyno.skent.util.skript.AsyncEffect;

import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;

@Name("Copy File or directory")
@Description("Copy a file or a directory to another.")
@Examples({
    "# /!\\ if the copied file already exist, it will be replaced /!\\ \n\n" +

    "command copy:\n" +
        "\ttrigger:\n" +
        "\t\tcopy file path \"plugins/Skript/scrips/MyAwesomeScript.sk\" to file path \"plugins/Skript/scrips/MyAwesomeScriptCopy.sk\"\n" +
        "\t\tbroadcast \"Copied!\"",

    "# If you need to wait the end of the effect before execute a part of your code, you can\n" +
    "# use this effect as a section effect.\n" + 
    "# The code after this effect section will be executed when the effect section has finished to be executed.\n\n" +
    "command copy:\n" +
        "\ttrigger:\n" +
        "\t\tcopy file path \"plugins/Skript/scrips/MyAwesomeScript.sk\" to file path \"plugins/Skript/scrips/MyAwesomeScriptCopy.sk\":\n" +
        "\t\t\tbroadcast \"Copied!\""
})
@Since("1.0")

public class EffCopyFileDir extends AsyncEffect {

    static {
        registerAsyncEffect(EffCopyFileDir.class,
            "copy %paths% to %path%"
        );
    }

    private Expression<Path> sources;
    private Expression<Path> target;

    @Override
    @SuppressWarnings("unchecked")
    protected boolean initAsync(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        sources = (Expression<Path>) expr[0];
        target = (Expression<Path>) expr[1];
        return true;
    }

    @Override
    protected void executeAsync(Event e) {
        Path[] sourceFiles = sources.getArray(e);
        Path targetFile = target.getSingle(e);
        try {
            for (Path sourceFile : sourceFiles) {
                if (Files.exists(sourceFile)) {
                    if (Files.isDirectory(sourceFile)) {
                        copyDir(sourceFile, targetFile);
                    } else {
                        if (Files.isDirectory(targetFile)) {
                            targetFile = Paths.get(targetFile.toAbsolutePath() + File.separator + sourceFile.getFileName());
                        }
                        Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    }
                    new ChangeEvent(targetFile);
                    new CopyEvent(sourceFile, targetFile);
                } else {
                    throw new IOException();
                }
            }
        } catch (IOException ex) {
            Skript.exception(ex);
        }
    }

    // Source: https://stackoverflow.com/a/60621544/8845770
    private void copyDir(Path source, Path target) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, target.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Files.createDirectories(target.resolve(source.relativize(dir)));
                return FileVisitResult.CONTINUE;
            }

        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "copy " + sources.toString(e, debug) + " to " + target.toString(e, debug);
    }

}
