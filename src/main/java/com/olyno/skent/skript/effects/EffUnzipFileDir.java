package com.olyno.skent.skript.effects;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import org.bukkit.event.Event;

import com.olyno.skent.skript.events.bukkit.ChangeEvent;
import com.olyno.skent.skript.events.bukkit.UnzipEvent;
import com.olyno.skent.util.skript.AsyncEffect;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

@Name("Unzip File or Directory")
@Description("Unzips files and/or directories.")
@Examples({
    "command unzip:\n" +
        "\ttrigger:\n" +
        "\t\tunzip all files in file path \"plugins/Skript/scripts.zip\" to dir path \"plugins/Skript\"\n" +
        "\t\tbroadcast \"Nice I retrieved my backup!\"",

    "# If you need to wait the end of the effect before execute a part of your code, you can\n" +
    "# use the keyword \"sync\" before." + 
    "# The code after this effect section will be executed when the effect section has finished to be executed.\n\n" +
    "command unzip:\n" +
        "\ttrigger:\n" +
        "\t\tunzip all files in file path \"plugins/Skript/scripts.zip\" to dir path \"plugins/Skript\"\n" +
        "\t\tbroadcast \"Nice I retrieved my backup!\""
})
@Since("1.0")

public class EffUnzipFileDir extends AsyncEffect {

    static {
        registerAsyncEffect(EffUnzipFileDir.class,
            "(unzip|extract) all files (in|of|from) %path% to %path% with [password] %string%",
            "(unzip|extract) all files (in|of|from) %path% to %path%",
            "(unzip|extract) %path% (in|of|from) %path% to %path% with [password] %string%",
            "(unzip|extract) %path% (in|of|from) %path% to %path%"
        );
    }

    private Expression<Path> files;
    private Expression<Path> source;
    private Expression<Path> target;
    private Expression<String> password;
    private Boolean allFiles = false;

    @Override
    @SuppressWarnings("unchecked")
    protected boolean initAsync(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        int index = 0;
        if (matchedPattern == 0 || matchedPattern == 1) {
            allFiles = true;
            index ++;
        } else {
            files = (Expression<Path>) expr[0];
        }
        source = (Expression<Path>) expr[1 - index];
        target = (Expression<Path>) expr[2 - index];
        if (matchedPattern == 0 || matchedPattern == 2) {
            password = (Expression<String>) expr[3 - index];
        }
        return true;
    }

    @Override
    protected void execute(Event e) {
        Path sourceFile = source.getSingle(e);
        Path targetFile = target.getSingle(e);
        String pass = password != null ? password.getSingle(e) : null;
        if (!Pattern.compile("\\.zip$").matcher(sourceFile.toString()).find()) return;
        unzip(
            files != null ? files.getArray(e) : null,
            sourceFile,
            targetFile,
            pass
        );
    }

    private void unzip(Path[] allFiles, Path src, Path dest, String pass) {
        try {
            if (!Files.exists(dest)) Files.createDirectory(dest);
            ZipFile zipFile = new ZipFile(src.toString());
            if (zipFile.isEncrypted()) {
                if (pass != null) {
                    zipFile.setPassword(pass);
                }
            }

            if (allFiles == null) {
                zipFile.extractAll(dest.toString());
            } else {
                for (Path file : allFiles) {
                    zipFile.extractFile(file.toString(), dest.toString());
                }
            }

            new UnzipEvent(allFiles, src, dest, pass);
            new ChangeEvent(dest.getParent());

        } catch (ZipException | IOException ex) {
            Skript.exception(ex);
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "unzip " + (allFiles ? " all files " : files.toString(e, debug)) + "in " + source.toString(e, debug) + " to " + target.toString(e, debug) + (password != null ? " with password " + password.toString(e, debug) : "");
    }

}
