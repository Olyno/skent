package com.olyno.skent.skript.effects;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import org.bukkit.event.Event;

import com.olyno.skent.skript.events.bukkit.ChangeEvent;
import com.olyno.skent.skript.events.bukkit.ZipEvent;
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
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

@Name("Zip File or Directory")
@Description("Zips files and/or directories.")
@Examples({
    "command zip:\n" +
        "\ttrigger:\n" +
        "\t\tzip dir path \"plugins/Skript/scripts\" to dir path \"plugins/Skript/scripts.zip\"\n" +
        "\t\tbroadcast \"Nice I did a backup!\"",

    "# If you need to wait the end of the effect before execute a part of your code, you can\n" +
    "# use the keyword \"sync\" before." + 
    "# The code after this effect section will be executed when the effect section has finished to be executed.\n\n" +
    "command zip:\n" +
        "\ttrigger:\n" +
        "\t\tzip dir path \"plugins/Skript/scripts\" to dir path \"plugins/Skript/scripts.zip\"\n" +
        "\t\tbroadcast \"Nice I did a backup!\""
})
@Since("1.0")

public class EffZipFileDir extends AsyncEffect {

    static {
        registerAsyncEffect(EffZipFileDir.class,
            "zip %paths% to %path% with [password] %string%",
            "zip %paths% to %path%"
        );
    }

    private Expression<Path> paths;
    private Expression<Path> target;
    private Expression<String> password;
    private boolean isSingle;

    @Override
    @SuppressWarnings("unchecked")
    protected boolean initAsync(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        paths = (Expression<Path>) expr[0];
        target = (Expression<Path>) expr[1];
        if (matchedPattern == 0) {
            password = (Expression<String>) expr[2];
        }
        isSingle = paths.getSource().isSingle();
        return true;
    }

    @Override
    protected void executeAsync(Event e) {
        Path[] pathsList = isSingle ? new Path[]{paths.getSingle(e)} : paths.getArray(e);
        Path targetFile = target.getSingle(e);
        String pass = password != null ? password.getSingle(e) : null;
        if (Pattern.compile("\\.zip$").matcher(targetFile.toString()).find()) {
            if (password != null && pass != null) {
                zip(pathsList, targetFile, pass);
            } else {
                zip(pathsList, targetFile, null);
            }
        }
    }

    private void zip(Path[] src, Path dest, String pass) {
        try {
            ZipFile zipFile = new ZipFile(dest.toString());
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);

            if (pass != null) {
                parameters.setEncryptFiles(true);
                parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
                parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_128);
                parameters.setPassword(pass);
            }

            for (Path file : src) {
                if (Files.exists(file)) {
                    if (Files.isDirectory(file)) {
                        zipFile.addFolder(file.toString(), parameters);
                    } else {
                        zipFile.addFile(file.toFile(), parameters);
                    }
                }
            }
            new ZipEvent(src, dest, pass);
            new ChangeEvent(dest.getParent());
        } catch (ZipException ex) {
            Skript.exception(ex);
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "zip " + paths.toString(e, debug) + " to " + target.toString(e, debug);
    }

}
