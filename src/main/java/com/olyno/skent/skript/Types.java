package com.olyno.skent.skript;

import java.nio.file.Path;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;

public class Types {

	static {
		Classes.registerClass(new ClassInfo<>(Path.class, "path")
			.defaultExpression(new EventValueExpression<>(Path.class))
			.user("path")
			.name("Path")
			.description("The path type. Can be a file or a directory.")
			.since("1.0")
			.parser(new Parser<Path>() {

				@Override
				public Path parse(String path, ParseContext arg1) {
					// Can't parse, else will make an error (parse << file "x" >> instead of just << "x" >>)
					return null;
				}

				@Override
				public boolean canParse(final ParseContext context) {
					return false;
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

		Classes.registerClass(new ClassInfo<>(Process.class, "processe")
			.defaultExpression(new EventValueExpression<>(Process.class))
			.user("process")
			.name("Process")
			.description("A process running. Can be get from execute effect.")
			.since("2.2.0")
			.parser(new Parser<Process>() {

				@Override
				public Process parse(String process, ParseContext arg1) {
					return null;
				}

				@Override
				public boolean canParse(final ParseContext context) {
					return false;
				}

				@Override
				public String toString(Process process, int arg1) {
					return "" + process.pid();
				}

				@Override
				public String toVariableNameString(Process process) {
					return "" + process.pid();
				}

			}));
	}

}
