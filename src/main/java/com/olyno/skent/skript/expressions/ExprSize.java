package com.olyno.skent.skript.expressions;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicLong;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;

@Name("Size of File/Directory")
@Description("Returns the size of a file or a directory in bytes.")
@Examples({
    "command size:\n" +
        "\ttrigger:\n" +
        "\t\tset {_size} to size of file path \"plugins/Skript/config.sk\"\n" +
        "\t\tbroadcast \"The size of the config file is %{_extension}%\""
})
@Since("1.7")

public class ExprSize extends SimplePropertyExpression<Path, Number> {                                             // The number of bytes in a yottabyte

    static {
        register(ExprSize.class, Number.class,
            "(file|dir[ectory]) size", "path"
        );
    }

    @Override
    public Number convert(Path path) {
        if (Files.exists(path)) {
            try {
                AtomicLong size = new AtomicLong(0);
                if (Files.isRegularFile(path)) {
                    size.addAndGet(FileChannel.open(path).size());
                } else {
                    Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                                throws IOException {
                            size.addAndGet(attrs.size());
                            return FileVisitResult.CONTINUE;
                        }
                    });
                }
                return (Number) size.intValue();
            } catch (IOException ex) {
               Skript.exception(ex);
            }
        }
        return 0;
    }

    @Override
    protected String getPropertyName() {
        return "size";
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }
    
}
