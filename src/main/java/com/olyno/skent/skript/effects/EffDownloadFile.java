package com.olyno.skent.skript.effects;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;

import com.olyno.skent.skript.events.bukkit.ChangeEvent;
import com.olyno.skent.skript.events.bukkit.DownloadEvent;
import com.olyno.skent.util.AsyncEffect;
import org.bukkit.event.Event;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.util.concurrent.CompletionException;

@Name("Download File")
@Description("Downloads a file from a url.")
@Examples({
    "command download:\n" +
        "\ttrigger:\n" +
        "\t\tdownload from url \"https://github.com/Olyno/skent/releases/latest/download/skent-all.jar\" to file path \"plugins/Skent.jar\"\n" +
        "\t\tbroadcast \"Skent downloaded!\"",

    "# If you need to wait the end of the effect before execute a part of your code, you can\n" +
    "# use this effect as a section effect.\n" + 
    "# The code after this effect section will be executed when the effect section has finished to be executed.\n\n" +
    "command download:\n" +
        "\ttrigger:\n" +
        "\t\tdownload from url \"https://github.com/Olyno/skent/releases/latest/download/skent-all.jar\" to file path \"plugins/Skent.jar\":\n" +
        "\t\t\tbroadcast \"Skent downloaded!\""
})
@Since("1.0")

public class EffDownloadFile extends AsyncEffect {

    static {
        registerAsyncEffect(EffDownloadFile.class,
            "download [file] from [url] %string% to %path%"
        );
    }

    private Expression<String> url;
    private Expression<Path> file;

    @Override
    @SuppressWarnings("unchecked")
    protected boolean initAsync(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        url = (Expression<String>) expr[0];
        file = (Expression<Path>) expr[1];
        return true;
    }

    @Override
    protected void executeAsync(Event e) {
        String currentUrl = url.getSingle(e);
        Path currentFile = file.getSingle(e);
        try {
            ReadableByteChannel rbc = null;
            rbc = Channels.newChannel(new URL(currentUrl).openStream());
            FileOutputStream fos = new FileOutputStream(currentFile.toString());
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            new DownloadEvent(currentUrl, currentFile);
            new ChangeEvent(currentFile.getParent());
        } catch (IOException ex) {
            throw new CompletionException(ex);
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "download file from " + url.toString(e, debug) + " to file " + file.toString(e, debug);
    }

}
