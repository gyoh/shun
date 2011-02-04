package org.hamamoto.album.action;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hamamoto.album.service.AlbumService;

public class TransformImage extends BaseAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Get parameters
        String path = request.getParameter("path");
        if (path == null) {
            path = "";
        }
        String sizeStr = request.getParameter("size");
        double size = 0.0;
        if (sizeStr != null) {
            size = Double.parseDouble(sizeStr);
        }
        String degreesStr = request.getParameter("degrees");
        int degrees = 0;
        if (degreesStr != null) {
            degrees = Integer.parseInt(degreesStr);
        }

        AlbumService service = getAlbumService();
        BufferedImage bi = service.getTransformedImage(path, size, degrees);

        // write the image out to response stream
        response.setContentType("image/jpeg");
        BufferedOutputStream bos =
                new BufferedOutputStream(response.getOutputStream());
        ImageIO.write(bi, "jpg", bos);
        bos.close();

        return null;
    }
}
