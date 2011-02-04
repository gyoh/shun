package org.hamamoto.album.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Files implements Serializable {
    private List list;
    private Map breadcrumb;
    private Integer index;
    
    /**
     * @return Returns the list.
     */
    public List getList() {
        return list;
    }
    /**
     * @param list The list to set.
     */
    public void setList(List list) {
        this.list = list;
    }
    /**
     * @return Returns the breadcrumb.
     */
    public Map getBreadcrumb() {
        return breadcrumb;
    }
    /**
     * @param breadcrumb The breadcrumb to set.
     */
    public void setBreadcrumb(Map breadcrumb) {
        this.breadcrumb = breadcrumb;
    }
    /**
     * @return Returns the index.
     */
    public Integer getIndex() {
        return index;
    }
    /**
     * @param index The index to set.
     */
    public void setIndex(Integer index) {
        this.index = index;
    }
}
