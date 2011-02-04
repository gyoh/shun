package org.hamamoto.album.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hamamoto.album.service.AlbumService;
import org.hamamoto.album.util.Constants;
import org.hamamoto.album.vo.Files;

/**
 * Implements the logic to view thumbnails.
 */
public class DisplayThumbnails extends BaseAction {

    private static final Log log = LogFactory.getLog(DisplayThumbnails.class);
    
    /**
     * Called by the controller when a member attempts to view thumbnails.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String path = request.getParameter("path");
        if (path == null) {
            path = "";
        }

        HttpSession session = request.getSession();
        String sort = (String)session.getAttribute(Constants.SORT);
        if (request.getParameter("sort") != null) {
            sort = request.getParameter("sort");
            session.setAttribute(Constants.SORT, sort);
        }
        if (sort == null) {
            sort = "type";
            session.setAttribute(Constants.SORT, sort);
        }

        AlbumService service = getAlbumService();
        Files files = service.getFiles(path, sort);

        // set sort parameter map.
        Map sortByTypeParam = new HashMap();
        if (path.length() > 0) sortByTypeParam.put("path", path);
        sortByTypeParam.put("sort", "type");
        Map sortByNameParam = new HashMap();
        if (path.length() > 0) sortByNameParam.put("path", path);
        sortByNameParam.put("sort", "name");
        Map sortByDateParam = new HashMap();
        if (path.length() > 0) sortByDateParam.put("path", path);
        sortByDateParam.put("sort", "date");

        // set tranform parameter map
        Map transformParam = new HashMap();
        transformParam.put("size", Constants.THUMBNAIL_SIZE);
        
        // set request attributes
        request.setAttribute(Constants.FILES, files);
        request.setAttribute(Constants.SORT_BY_TYPE_PARAM, sortByTypeParam);
        request.setAttribute(Constants.SORT_BY_NAME_PARAM, sortByNameParam);
        request.setAttribute(Constants.SORT_BY_DATE_PARAM, sortByDateParam);
        request.setAttribute(Constants.TRANSFORM_PARAM, transformParam);

        return mapping.findForward(Constants.SUCCESS);
    }
}
