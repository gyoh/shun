package org.hamamoto.album.service;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

import org.hamamoto.album.util.Constants;

/**
 * A factory for creating service Implementations. The specific service to
 * instantiate is determined from the initialization parameter of the
 * ServiceContext. Otherwise, a default implementation is used.
 */
public class AlbumServiceFactory implements PlugIn {
    // Hold onto the servlet for the destroy method
    private ActionServlet servlet = null;

    public AlbumService createService() throws ClassNotFoundException,
            IllegalAccessException, InstantiationException {
        ServletContext context = servlet.getServletContext();
        AlbumService service = (AlbumService) context
                .getAttribute(Constants.SERVICE_INTERFACE);
        if (service == null) {
            service = (AlbumService) Class.forName(Constants.SERVICE_CLASS)
                    .newInstance();
            service.addFileMonitorTask(Constants.IMAGE_ROOT_PATH);
            context.setAttribute(Constants.SERVICE_INTERFACE, service);
        }
        return service;
    }

    public void init(ActionServlet servlet, ModuleConfig config)
            throws ServletException {
        // Store the servlet for later
        this.servlet = servlet;

        /*
         * Store the factory for the application. Any service factory must
         * either store itself in the ServletContext at this key, or extend this
         * class and don't override this method. The application assumes that a
         * factory class that implements the IBServiceFactory is stored at the
         * proper key in the ServletContext.
         */
        servlet.getServletContext().setAttribute(Constants.SERVICE_FACTORY,
                this);
    }

    public void destroy() {
        servlet.getServletContext().removeAttribute(
                Constants.SERVICE_INTERFACE);
    }
}