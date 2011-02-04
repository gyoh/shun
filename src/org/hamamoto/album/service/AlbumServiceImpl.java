package org.hamamoto.album.service;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import org.hamamoto.album.util.Constants;
import org.hamamoto.album.util.FileChangeListener;
import org.hamamoto.album.util.FileMonitor;
import org.hamamoto.album.util.ImageInfo;
import org.hamamoto.album.vo.Directory;
import org.hamamoto.album.vo.Files;
import org.hamamoto.album.vo.Image;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectory;

public class AlbumServiceImpl implements AlbumService, FileChangeListener {
    
    private static final Log log = LogFactory.getLog(AlbumServiceImpl.class);

    public Files getFiles(String path, String sort)
            throws IOException {
        java.io.File file = new java.io.File(Constants.IMAGE_ROOT_PATH + path);

        Files files = new Files();
        
        // set breadcrumb navigation
        files.setBreadcrumb(getBreadcrumb(path));
        
        List fileList = new ArrayList();
        files.setList(fileList);

        if (!file.getCanonicalPath().startsWith(
                Constants.IMAGE_ROOT_PATH.substring(
                        0, Constants.IMAGE_ROOT_PATH.length() - 1))) {
            log.debug(file.getCanonicalPath() + " not in image root path.");
            return files;
        }

        if (!file.exists()) {
            log.debug(file.getCanonicalPath() + " not found.");
            return files;
        }

        File[] fileArray = file.listFiles(
                new FileFilter() {
                    public boolean accept(File pathname) {
                        return !pathname.isHidden() && pathname.canRead();
                    }
                });

        if (fileArray == null) {
            log.debug(file.getCanonicalPath() + " not found.");
            return files;
        }

        for (int i = 0; i < fileArray.length; i++) {
            String relativePath = fileArray[i].getCanonicalPath()
                    .replaceAll(Constants.IMAGE_ROOT_PATH, "");
            if (fileArray[i].isFile()) {
                if (fileArray[i].getName().toLowerCase().endsWith("jpg")
                        || fileArray[i].getName().toLowerCase().endsWith("jpeg")) {
                    Image image = new Image();
                    image.setName(fileArray[i].getName());
                    image.setPath(relativePath);
                    image.setSize(new Integer(Math.round(fileArray[i].length() / 1024)));
                    image.setLastModified(new Date(fileArray[i].lastModified()));
                    java.io.File thumbnail =
                        new java.io.File(Constants.THUMBNAIL_ROOT_PATH + relativePath);
                    if (thumbnail.exists() && thumbnail.lastModified() > fileArray[i].lastModified()) {
                        image.setThumbnailExists(Boolean.TRUE);
                    } else {
                        image.setThumbnailExists(Boolean.FALSE);
                    }
                    // use ImageInfo utility class.
                    ImageInfo ii = getImageInfo(fileArray[i]);
                    image.setWidth(new Integer(ii.getWidth()));
                    image.setHeight(new Integer(ii.getHeight()));
                    // use exif metadata extractor library
                    try {
                        Metadata metadata = JpegMetadataReader.readMetadata(fileArray[i]);
                        com.drew.metadata.Directory exifDirectory = metadata.getDirectory(ExifDirectory.class);
                        Date dateTimeOriginal = exifDirectory.getDate(ExifDirectory.TAG_DATETIME_ORIGINAL);
                        image.setDateTimeOriginal(dateTimeOriginal);
                    } catch (JpegProcessingException e) {
                        log.debug("failed to read jpeg metadata.");
                    } catch (MetadataException e) {
                        log.debug("failed to get DateTimeOriginal.");
                    }
                    fileList.add(image);
                }
            } else if (fileArray[i].isDirectory()) {
                Directory directory = new Directory();
                directory.setName(fileArray[i].getName());
                directory.setPath(relativePath);
                directory.setLastModified(new Date(fileArray[i].lastModified()));                
                fileList.add(directory);
            }
        }

        Collections.sort(fileList, getComparator(sort));
        
        return files;
    }

    public Files getFile(String path, String sort) throws IOException {
        java.io.File file = new java.io.File(Constants.IMAGE_ROOT_PATH + path);
    
        Files files = new Files();
        
        // set breadcrumb navigation
        files.setBreadcrumb(getBreadcrumb(path));
        
        List fileList = new ArrayList();
        files.setList(fileList);

        if (!file.getCanonicalPath().startsWith(
                Constants.IMAGE_ROOT_PATH.substring(
                        0, Constants.IMAGE_ROOT_PATH.length() - 1))) {
            log.debug(file.getCanonicalPath() + " not in image root path.");
            return files;
        }

        if (!file.exists()) {
            log.debug(file.getCanonicalPath() + " not found.");
            return files;
        }
    
        File[] fileArray = file.getParentFile().listFiles(
                new FileFilter() {
                    public boolean accept(File pathname) {
                        return !pathname.isHidden() && pathname.canRead();
                    }
                });


        if (fileArray == null) {
            log.debug(file.getCanonicalPath() + " not found.");
            return files;
        }

        for (int i = 0; i < fileArray.length; i++) {
            String relativePath = fileArray[i].getCanonicalPath()
                    .replaceAll(Constants.IMAGE_ROOT_PATH, "");
            if (fileArray[i].isFile()) {
                if (fileArray[i].getName().toLowerCase().endsWith("jpg")
                        || fileArray[i].getName().toLowerCase().endsWith("jpeg")) {
                    Image image = new Image();
                    image.setName(fileArray[i].getName());
                    image.setPath(relativePath);
                    image.setSize(new Integer(Math.round(fileArray[i].length() / 1024)));
                    image.setLastModified(new Date(fileArray[i].lastModified()));
                    // use ImageInfo utility class.
                    ImageInfo ii = getImageInfo(fileArray[i]);
                    image.setWidth(new Integer(ii.getWidth()));
                    image.setHeight(new Integer(ii.getHeight()));
                    // use exif metadata extractor library
                    try {
                        Metadata metadata = JpegMetadataReader.readMetadata(fileArray[i]);
                        com.drew.metadata.Directory exifDirectory = metadata.getDirectory(ExifDirectory.class);
                        Date dateTimeOriginal = exifDirectory.getDate(ExifDirectory.TAG_DATETIME_ORIGINAL);
                        image.setDateTimeOriginal(dateTimeOriginal);
                    } catch (JpegProcessingException e) {
                        log.error("failed to read jpeg metadata.");
                    } catch (MetadataException e) {
                        log.error("failed to get DateTimeOriginal.");
                    }
                    fileList.add(image);
                }
            }
        }

        Collections.sort(fileList, getComparator(sort));

        int i = 0;
        for (Iterator it = fileList.iterator(); it.hasNext(); i++) {
            if (path.equals(((org.hamamoto.album.vo.File)it.next()).getPath())) {
                files.setIndex(new Integer(i));
            }
        }

        return files;
    }

    public String encodeURL(String s, String enc)
            throws UnsupportedEncodingException {
        String[] str = s.split("/");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            sb.append(URLEncoder.encode(str[i], enc));
            if (i < str.length - 1)
                sb.append("/");
        }
        return sb.toString();
    }

    public BufferedImage getTransformedImage(String path, double size, int degrees)
                throws IOException {
            BufferedImage bi = null;
            if (size > 0.0) {
                bi = scale(getBufferedImage(path), size);

                /*
                if (size == Constants.THUMBNAIL_SIZE.intValue()) {
                    bi = generateThumbnail(path);
                } else {
                    bi = scale(getBufferedImage(path), size);
                }
                */
                
                /*
                if (size == Constants.THUMBNAIL_SIZE.intValue()) {
                    java.io.File file = new java.io.File(Constants.IMAGE_ROOT_PATH + path);
                    Image image = new Image();
                    image.setPath(path);
                    Image retrievedImage = retrieveImage(image);
                    if (retrievedImage != null
                            && retrievedImage.getThumbnail() != null
                            && new Date(file.lastModified()).equals(retrievedImage.getLastModified())) {
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(retrievedImage.getThumbnail());
                        bi = ImageIO.read(inputStream);
                        inputStream.close();
                    } else {
                        bi = scale(getBufferedImage(path), size);
                        image.setName(file.getName());
                        image.setSize(new Integer(Math.round(file.length() / 1024)));
                        image.setLastModified(new Date(file.lastModified()));
                        // use ImageInfo utility class.
                        ImageInfo ii = getImageInfo(file);
                        image.setWidth(new Integer(ii.getWidth()));
                        image.setHeight(new Integer(ii.getHeight()));
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        ImageIO.write(bi, "jpg", outputStream);
                        image.setThumbnail(outputStream.toByteArray());
                        storeImage(image);
                        outputStream.close();
                    }
                } else {
                    bi = scale(getBufferedImage(path), size);
                }
                */
            } else {
                bi = getBufferedImage(path);
            }
    
            if (degrees > 0) {
                bi = rotate(bi, degrees);
            }
            return bi;
        }

    public BufferedImage getBufferedImage(String path) throws IOException {
        java.io.File file = new java.io.File(Constants.IMAGE_ROOT_PATH + path);
        return ImageIO.read(file);
    }

    public BufferedImage generateThumbnail(String path) throws IOException {
        BufferedImage bi = scale(getBufferedImage(path),
                Constants.THUMBNAIL_SIZE.intValue());
        java.io.File thumbnail = new java.io.File(Constants.THUMBNAIL_ROOT_PATH
                + path);
        if (!thumbnail.getParentFile().exists()) {
            thumbnail.getParentFile().mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(thumbnail);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ImageIO.write(bi, "jpg", bos);
        bos.close();
        return bi;
    }

    private Map getBreadcrumb(String path) {
        // set directory map.
        Map breadcrumb = new TreeMap();
        if (path.length() > 0) {
            String[] dirs = path.split("/");
            String curDir = "";
            for (int i = 0; i < dirs.length; i++) {
                if (i == 0) {
                    curDir += dirs[i];
                } else {
                    curDir += "/" + dirs[i];
                }
                breadcrumb.put(curDir, dirs[i]);
            }
        }
        return breadcrumb;
    }

    private ImageInfo getImageInfo(File file) throws IOException {
        // use ImageInfo utility class.
        ImageInfo ii = new ImageInfo();
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ii.setInput(bis);
        ii.check();
        bis.close();
        return ii;
    }

    private Comparator getComparator(String sort) {
        Comparator comparator = null;
    
        if ("type".equals(sort)) {
            comparator = new Comparator() {
                public int compare(Object o1, Object o2) {
                    int result = 0;
                    if ((o1 instanceof Directory && o2 instanceof Directory)) {
                        result = ((org.hamamoto.album.vo.File) o1).getName().compareTo(
                                ((org.hamamoto.album.vo.File) o2).getName());
                    } else if (o1 instanceof Image && o2 instanceof Image) {
                        Date dateTime1 =
                            ((org.hamamoto.album.vo.Image) o1).getDateTimeOriginal();
                        Date dateTime2 =
                            ((org.hamamoto.album.vo.Image) o2).getDateTimeOriginal();
                        if (dateTime1 == null) {
                            dateTime1 = ((org.hamamoto.album.vo.Image) o1).getLastModified();
                        }
                        if (dateTime2 == null) {
                            dateTime2 = ((org.hamamoto.album.vo.Image) o1).getLastModified();
                        }
                        result = dateTime1.compareTo(dateTime2);
                    } else if (o1 instanceof Directory && o2 instanceof Image) {
                        result = -1;
                    } else if (o1 instanceof Image && o2 instanceof Directory) {
                        result = 1;
                    }
                    return result;
                }
            };
        } else if ("name".equals(sort)) {
            comparator = new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((org.hamamoto.album.vo.File) o1).getName().compareTo(
                            ((org.hamamoto.album.vo.File) o2).getName());
                }
            };
        } else if ("date".equals(sort)) {
            comparator = new Comparator() {
                public int compare(Object o1, Object o2) {
                    Date dateTime1 = ((org.hamamoto.album.vo.File) o1).getLastModified();
                    Date dateTime2 = ((org.hamamoto.album.vo.File) o2).getLastModified();
                    if (o1 instanceof org.hamamoto.album.vo.Image
                            && ((org.hamamoto.album.vo.Image) o1)
                            .getDateTimeOriginal() != null) {
                        dateTime1 = ((org.hamamoto.album.vo.Image) o1).getDateTimeOriginal();
                    }
                    if (o2 instanceof org.hamamoto.album.vo.Image
                            && ((org.hamamoto.album.vo.Image) o2)
                            .getDateTimeOriginal() != null) {
                        dateTime2 = ((org.hamamoto.album.vo.Image) o2).getDateTimeOriginal();
                    }
                    return dateTime1.compareTo(dateTime2);
                }
            };
        } else {
            comparator = new Comparator() {
                public int compare(Object o1, Object o2) {
                    int result = 0;
                    if ((o1 instanceof Directory && o2 instanceof Directory)) {
                        result = ((org.hamamoto.album.vo.File) o1).getName().compareTo(
                                ((org.hamamoto.album.vo.File) o2).getName());
                    } else if (o1 instanceof Image && o2 instanceof Image) {
                        Date dateTime1 =
                            ((org.hamamoto.album.vo.Image) o1).getDateTimeOriginal();
                        Date dateTime2 =
                            ((org.hamamoto.album.vo.Image) o2).getDateTimeOriginal();
                        if (dateTime1 == null) {
                            dateTime1 = ((org.hamamoto.album.vo.Image) o1).getLastModified();
                        }
                        if (dateTime2 == null) {
                            dateTime2 = ((org.hamamoto.album.vo.Image) o1).getLastModified();
                        }
                        result = dateTime1.compareTo(dateTime2);
                    } else if (o1 instanceof Directory && o2 instanceof Image) {
                        result = -1;
                    } else if (o1 instanceof Image && o2 instanceof Directory) {
                        result = 1;
                    }
                    return result;
                }
            };
        }
    
        return comparator;
    }

    private BufferedImage scale(BufferedImage bi, double size) {
        double ratio = Math.min(size / bi.getWidth(), size / bi.getHeight());
        if (ratio >= 1.0) return bi;
        AffineTransform af = AffineTransform.getScaleInstance(ratio, ratio);
        AffineTransformOp afOp = new AffineTransformOp(af, null);
        return afOp.filter(bi, null);
    }

    private BufferedImage rotate(BufferedImage bi, int degrees) {
        if (degrees != 90 && degrees != 270) return bi;
        double radians = Math.toRadians(degrees);
        AffineTransform af = AffineTransform.getRotateInstance(radians);
        switch (degrees) {
        case 90:
            af.translate(0, bi.getHeight() * -1);
            break;
        case 270:
            af.translate(bi.getWidth() * -1, 0);
            break;
        }
        AffineTransformOp afOp = new AffineTransformOp(af, null);
        return afOp.filter(bi, null);
    }

    private void storeImage(org.hamamoto.album.vo.Image image){
        ObjectContainer db = Db4o.openFile(Constants.YAP_FILE_NAME);
        db.set(image);
        db.commit();
        db.close();
    }

    private Image retrieveImage(org.hamamoto.album.vo.Image image) {
        Image retrievedImage = null;
        ObjectContainer db = Db4o.openFile(Constants.YAP_FILE_NAME);
        ObjectSet result = db.get(image);
        if (result.hasNext()) {
            retrievedImage = (Image)result.next();
        }
        db.commit();
        db.close();
        return retrievedImage;
    }

    /* (non-Javadoc)
     * @see org.hamamoto.album.util.FileChangeListener#fileChanged(java.lang.String)
     */
    public void fileChanged(String path) {
        try {
            java.io.File directory = new java.io.File(path);
            String relativePath = directory.getCanonicalPath()
                .replaceAll(Constants.IMAGE_ROOT_PATH, "");
            java.io.File thumbnailDirectory =
                new java.io.File(Constants.THUMBNAIL_ROOT_PATH + relativePath);

            if (!directory.exists()
                    || directory.isHidden()
                    || !directory.canRead()) {
                // cancel fileMonitorTask and remove the thumbnail directory
                // when the directory is deleted.
                log.debug("directory seems to have been removed: " + path);
                FileMonitor fileMonitor = FileMonitor.getInstance();
                fileMonitor.removeFileMonitorTask(this, path);
                deleteDirectory(thumbnailDirectory);
                return;
            } else {
                // remove unecessary thumbnails if the thumbnail directory exists.
                File[] thumbnailArray = thumbnailDirectory.listFiles();
                if (thumbnailArray !=  null) {
                    log.debug("removing unnecessary thumbnails in the directory: " + path);
                    for (int i = 0; i < thumbnailArray.length; i++) {
                        if (thumbnailArray[i].isFile()) {
                            relativePath = thumbnailArray[i].getCanonicalPath()
                                .replaceAll(Constants.THUMBNAIL_ROOT_PATH, "");
                            java.io.File image =
                                new java.io.File(Constants.IMAGE_ROOT_PATH + relativePath);
                            if (!image.exists() || image.isHidden() || !image.canRead()) {
                                thumbnailArray[i].delete();
                            }
                        }
                    }
                }
            }
            
            File[] fileArray = directory.listFiles(
                    new FileFilter() {
                        public boolean accept(File pathname) {
                            return !pathname.isHidden() && pathname.canRead();
                        }
                    });

            // there is nothing to be done if the directory does not exist.
            if (fileArray == null) {
                log.debug("directory does not seem to exist: " + path);
                return;
            }

            // generate thumbnail if it's an image file.
            // add FileMonitorTask if it's a directory.
            for (int i = 0; i < fileArray.length; i++) {
                if (fileArray[i].isFile()) {
                    relativePath = fileArray[i].getCanonicalPath()
                        .replaceAll(Constants.IMAGE_ROOT_PATH, "");
                    if (fileArray[i].getName().toLowerCase().endsWith("jpg")
                            || fileArray[i].getName().toLowerCase().endsWith("jpeg")) {
                        java.io.File thumbnail =
                            new java.io.File(Constants.THUMBNAIL_ROOT_PATH + relativePath);
                        if (!thumbnail.exists()
                                || thumbnail.lastModified() < fileArray[i].lastModified()) {
                            generateThumbnail(relativePath);
                        }
                    }
                } else if (fileArray[i].isDirectory()) {
                    addFileMonitorTask(fileArray[i].getCanonicalPath());
                }
            }
        } catch (IOException e) {
            log.error("IOException occured in fileChanged(): ", e);
        }
    }

    public void addFileMonitorTask(String path) {
        FileMonitor fileMonitor = FileMonitor.getInstance();
        try {
            fileMonitor.addFileMonitorTask(this, path, Constants.TIMER_PERIOD.longValue());
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException occured in addFileChangeListener(): ", e);
        }        
    }

    private void deleteDirectory(java.io.File directory) {
        File[] fileArray = directory.listFiles();
        // there is nothing to be done if the directory does not exist.
        if (fileArray == null) {
            log.debug("unable to remove directory that does not exist.");
            return;
        }
        // remove files and directories recursively.
        for (int i = 0; i < fileArray.length; i++) {
            if (fileArray[i].isFile()) {
                fileArray[i].delete();
            } else if (fileArray[i].isDirectory()) {
                deleteDirectory(fileArray[i]);
            }
        }
        // delete the directory.
        boolean result = directory.delete();
        log.debug("deleted directory " + directory.getName()
                + " successfully?: " + result);
    }
}
