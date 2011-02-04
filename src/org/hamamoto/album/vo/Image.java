package org.hamamoto.album.vo;

import java.util.Date;

public class Image extends File {
    private Integer size;
    private Integer width;
    private Integer height;
    private byte[] thumbnail;
    private Boolean thumbnailExists;
    private Date dateTimeOriginal;
    
    /**
     * @return Returns the height.
     */
    public Integer getHeight() {
        return height;
    }
    /**
     * @param height The height to set.
     */
    public void setHeight(Integer height) {
        this.height = height;
    }
    /**
     * @return Returns the size.
     */
    public Integer getSize() {
        return size;
    }
    /**
     * @param size The size to set.
     */
    public void setSize(Integer size) {
        this.size = size;
    }
    /**
     * @return Returns the width.
     */
    public Integer getWidth() {
        return width;
    }
    /**
     * @param width The width to set.
     */
    public void setWidth(Integer width) {
        this.width = width;
    }
    /**
     * @return Returns the thumbnail.
     */
    public byte[] getThumbnail() {
        return thumbnail;
    }
    /**
     * @param thumbnail The thumbnail to set.
     */
    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }
    /**
     * @return Returns the thumbnailExists.
     */
    public Boolean getThumbnailExists() {
        return thumbnailExists;
    }
    /**
     * @param thumbnail The thumbnailExists to set.
     */
    public void setThumbnailExists(Boolean thumbnailExists) {
        this.thumbnailExists = thumbnailExists;
    }
    /**
     * @return Returns the dateTimeOriginal.
     */
    public Date getDateTimeOriginal() {
        return dateTimeOriginal;
    }
    /**
     * @param dateTimeOriginal The dateTimeOriginal to set.
     */
    public void setDateTimeOriginal(Date dateTimeOriginal) {
        this.dateTimeOriginal = dateTimeOriginal;
    }
}
