package org.hamamoto.album.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;

import org.hamamoto.album.service.AlbumService;
import org.hamamoto.album.service.AlbumServiceFactory;
import org.hamamoto.album.util.Constants;

/**
 * An abstract Action class that all action classes should extend.
 */
abstract public class BaseAction extends Action {
    Log log = LogFactory.getLog(this.getClass());

    protected AlbumService getAlbumService() {

        AlbumServiceFactory factory = (AlbumServiceFactory) servlet
                .getServletContext()
                .getAttribute(Constants.SERVICE_FACTORY);

        AlbumService service = null;
        try {
            service = factory.createService();
        } catch (Exception e) {
            log.error("Problem creating the AlbumService", e);
        }

        return service;
    }
}