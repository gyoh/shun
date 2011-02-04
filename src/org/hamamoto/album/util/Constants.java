package org.hamamoto.album.util;

/**
 * A class defined for the constants used throughout the app.
 */
public class Constants {
    private Constants() {
        // prevents instantiation
    }

    public static final String SUCCESS = "Success";

    public static final String FAILURE = "Failure";

    public static final String SERVICE_FACTORY = "org.hamamoto.album.service.AlbumServiceFactory";

    public static final String SERVICE_INTERFACE = "org.hamamoto.album.service.AlbumService";

    public static final String SERVICE_CLASS = "org.hamamoto.album.service.AlbumServiceImpl";
    
    public static final String IMAGE_ROOT_PATH = "/home/shun/public_html/img/";

    public static final String THUMBNAIL_ROOT_PATH = "/home/shun/public_html/thumbnails/";

    public static final String FILES = "files";

    public static final String IMAGE = "image";

    public static final String SORT = "sort";

    public static final String SORT_BY_TYPE_PARAM = "sortByTypeParam";

    public static final String SORT_BY_NAME_PARAM = "sortByNameParam";

    public static final String SORT_BY_DATE_PARAM = "sortByDateParam";

    public static final String ROTATE_CCW_PARAM = "rotateCCWParam";

    public static final String ROTATE_CW_PARAM = "rotateCWParam";

    public static final String TRANSFORM_PARAM = "transformParam";

    public static final String PREVIOUS_PATH_PARAM = "previousPathParam";

    public static final String NEXT_PATH_PARAM = "nextPathParam";

    public static final Integer THUMBNAIL_SIZE = new Integer(160);
    
    public static final String TRANSFORMED_IMAGE = "transformedImage";

    public static final Long TIMER_PERIOD = new Long(10000);

    public static final String YAP_FILE_NAME = "/home/gyo/file.yap";
}
