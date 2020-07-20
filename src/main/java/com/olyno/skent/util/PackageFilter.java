package com.olyno.skent.util;

/*

    Source: https://github.com/SkriptLang/Skript/blob/master/src/main/java/ch/njol/skript/SkriptAddon.java

 */

import ch.njol.util.coll.iterator.EnumerationIterable;
import com.olyno.skent.Skent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PackageFilter<T> {

    File file;
    Skent plugin = Skent.instance;
    String basePackage;
    String[] subPackages;
    List<Class<T>> classes = new LinkedList<>();

    public PackageFilter(String basePackage, final String... subPackages) {
        this.basePackage = basePackage;
        this.subPackages = subPackages;
    }

    public List<Class<T>> getClasses() throws IOException {
        classes.clear();
        final JarFile jar = new JarFile(getPath());
        for (int i = 0; i < subPackages.length; i++)
            subPackages[i] = subPackages[i].replace('.', '/') + "/";
        basePackage = basePackage.replace('.', '/') + "/";
        try {
            for (final JarEntry e : new EnumerationIterable<>(jar.entries())) {
                if (e.getName().startsWith(basePackage) && e.getName().endsWith(".class")) {
                    boolean load = subPackages.length == 0;
                    for (final String sub : subPackages) {
                        if (e.getName().startsWith(sub, basePackage.length())) {
                            load = true;
                            break;
                        }
                    }
                    if (load) {
                        final String c = e.getName().replace('/', '.').substring(0, e.getName().length() - ".class".length());
                        try {
                            classes.add((Class<T>) Class.forName(c, true, plugin.getClass().getClassLoader()));
                        } catch (final ClassNotFoundException | ExceptionInInitializerError ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        } finally {
            try {
                jar.close();
            } catch (final IOException ignored) {}
        }
        return classes;
    }

    public File getPath() {
        if (this.file != null)
            return file;
        try {
            final Method getFile = JavaPlugin.class.getDeclaredMethod("getFile");
            getFile.setAccessible(true);
            file = (File) getFile.invoke(this.plugin);
            return file;
        } catch (final NoSuchMethodException | IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            assert false;
        } catch (final SecurityException e) {
            throw new RuntimeException(e);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
        return null;
    }

}
