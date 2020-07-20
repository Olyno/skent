package com.olyno.skent.skript;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;

import java.nio.file.Path;

public class Types {

	static {
		Classes.registerClass(new ClassInfo<>(Path.class, "path")
			.defaultExpression(new EventValueExpression<>(Path.class))
			.user("path|file|dir(ectory)?")
			.name("Path")
			.description("The path type. Can be a file or a directory.")
			.since("1.0")
			.parser(new Parser<Path>() {

				@Override
				public String getVariableNamePattern() {
					return ".+";
				}

				@Override
				public Path parse(String path, ParseContext arg1) {
					// Can't parse, else will make an error (parse << file "x" >> instead of just << "x" >>)
					return null;
				}

				@Override
				public String toString(Path path, int arg1) {
					return path.toString();
				}

				@Override
				public String toVariableNameString(Path path) {
					return path.toString();
				}

			}));
	}

}
