package org.hamamoto.album.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hamamoto.album.service.AlbumService;
import org.hamamoto.album.util.Constants;
import org.hamamoto.album.vo.Files;
import org.hamamoto.album.vo.Image;

/**
 * Implements the logic to view thumbnails.
 */
public class DisplayImage extends BaseAction {

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
        String degrees = request.getParameter("degrees");

        HttpSession session = request.getSession();
        String sort = (String)session.getAttribute(Constants.SORT);

        AlbumService service = getAlbumService();
        Files files = service.getFile(path, sort);

        // set rotate parameter map.
        Map rotateCCWParam = new HashMap();
        if (path.length() > 0) rotateCCWParam.put("path", path);
        if (degrees == null) rotateCCWParam.put("degrees", "270");
        Map rotateCWParam = new HashMap();
        if (path.length() > 0) rotateCWParam.put("path", path);
        if (degrees == null) rotateCWParam.put("degrees", "90");

        // set transform parameter map
        Map transformParam = new HashMap();
        transformParam.put("path", path);
        if (degrees != null) transformParam.put("degrees", degrees);
        
        // set previous & next parameter map
        int index = 0;
        if (files.getIndex() != null) {
            index = files.getIndex().intValue();
        }
        if (index > 0) {
            Map previousParam = new HashMap();
            previousParam.put("path",
                    ((org.hamamoto.album.vo.File)(files.getList()
                            .get(index - 1))).getPath());
            request.setAttribute(Constants.PREVIOUS_PATH_PARAM, previousParam);
        }
        if (index < files.getList().size() - 1) {
            Map nextParam = new HashMap();
            nextParam.put("path",
                    ((org.hamamoto.album.vo.File)(files.getList()
                            .get(index + 1))).getPath());
            request.setAttribute(Constants.NEXT_PATH_PARAM, nextParam);            
        }

        // swap image width and height when rotated.
        Image image = (Image)(files.getList().get(files.getIndex().intValue()));
        if (degrees != null) {
            Integer width = image.getWidth();
            image.setWidth(image.getHeight());
            image.setHeight(width);
        }

        // set request attributes
        request.setAttribute(Constants.FILES, files);
        request.setAttribute(Constants.IMAGE, image);
        request.setAttribute(Constants.ROTATE_CCW_PARAM, rotateCCWParam);
        request.setAttribute(Constants.ROTATE_CW_PARAM, rotateCWParam);
        request.setAttribute(Constants.TRANSFORM_PARAM, transformParam);

        return mapping.findForward(Constants.SUCCESS);
    }
}
