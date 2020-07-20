package com.olyno.skent.util;

import java.nio.file.Path;
import java.util.regex.Pattern;

public class Utils {

    public static Boolean isDirectory(Path path) {
        return !Pattern.compile("\\.\\w+$").matcher(path.toString()).find();
    }

}