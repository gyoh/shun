package org.hamamoto.album.vo;

import java.io.Serializable;
import java.util.Date;

public class File implements Serializable {
    protected String name;
    protected String path;
    protected Date lastModified;

    /**
     * @return Returns the lastModified.
     */
    public Date getLastModified() {
        return lastModified;
    }
    /**
     * @param lastModified The lastModified to set.
     */
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the path.
     */
    public String getPath() {
        return path;
    }
    /**
     * @param path The path to set.
     */
    public void setPath(String path) {
        this.path = path;
    }
}