package com.olyno.skent.util.watch;

import java.nio.file.Path;

public interface WatchListener {
    
    public void onChange(Path path);

}