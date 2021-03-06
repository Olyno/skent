package com.olyno.skent.skript.effects;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import com.olyno.skent.skript.events.bukkit.ChangeEvent;
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

@Name("Appends Text File")
@Description("Appends any text to a file.")
@Examples({
	"command append:\n" +
		"\ttrigger:\n" +
		"\t\tcreate file path \"plugins/Skript/scripts/test1.txt\" with \"My name is\"\n" +
		"\t\tappend \"secret\" to file path \"plugins/Skript/scripts/test1.txt\"\n" +
		"\t\tbroadcast \"The text has been added!\"",

	"# If you need to wait the end of the effect before execute a part of your code, you can\n" +
	"# use this effect as a section effect.\n" +
	"# The code after this effect section will be executed when the effect section has finished to be executed.\n\n" +
	"command append:\n" +
		"\ttrigger:\n" +
		"\t\tcreate file path \"plugins/Skript/scripts/test1.txt\" with \"My name is\":\n" +
		"\t\t\tappend \"secret\" to file path \"plugins/Skript/scripts/test1.txt\"\n" +
		"\t\t\tbroadcast \"The text has been added!\""
})
@Since("1.0")

public class EffAppendFile extends AsyncEffect {

	static {
		registerAsyncEffect(EffAppendFile.class,
			"append %strings% to %paths%"
		);
	}

	private Expression<String> content;
	private Expression<Path> paths;

	@SuppressWarnings("unchecked")
	protected boolean initAsync(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		content = (Expression<String>) expr[0];
		paths = (Expression<Path>) expr[1];
		return true;
	}

	@Override
	protected void executeAsync(Event e) {
		Path[] pathsList = paths.getArray(e);
		for (Path path : pathsList) {
			if (Files.exists(path)) {
				try {
					Files.write(
						path,
						Arrays.asList(content.getArray(e)),
						StandardCharsets.UTF_8, StandardOpenOption.APPEND
					);
					new ChangeEvent(path);
				} catch (IOException ex) {
					Skript.exception(ex);
				}
			}
		}
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "append " + content.toString(e, debug) + " to " + paths.toString(e, debug);
	}

}
