/*
 * Created on 2005/08/13
 */
package org.hamamoto.album.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.hamamoto.album.vo.Files;

/**
 * @author gyo
 */
public interface AlbumService {
    public abstract Files getFiles(String path, String sort) throws IOException;

    public abstract Files getFile(String path, String sort) throws IOException;

    public abstract String encodeURL(String s, String enc)
            throws UnsupportedEncodingException;

    public abstract BufferedImage getTransformedImage(String path, double size, int degrees)
            throws IOException;

    public abstract BufferedImage getBufferedImage(String path)
            throws IOException;
    
    public BufferedImage generateThumbnail(String path) throws IOException;
    
    public void addFileMonitorTask(String path);
}
