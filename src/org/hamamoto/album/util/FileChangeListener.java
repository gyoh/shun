package org.hamamoto.album.util;

public interface FileChangeListener {
    /**
     * Invoked when a file changes.
     * 
     * @param fileName name of changed file.
     */
    public void fileChanged(String fileName);
}