package de.fhpotsdam.unfolding.examples.image;

import org.apache.log4j.Logger;
import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.LargeMapImageUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Uses LargeMapImageUtils to create a large map image, stitched together from multiple map screenshots.
 */
public class LargeMapImageSaveApp extends PApplet {

    private static final Logger LOGGER = Logger.getLogger(LargeMapImageSaveApp.class);
    // Set to the center LOCATION you want to grab the map for.
    private static final Location LOCATION = new Location(52.5f, 13.4f);
    // Set to zoom level you want to grab.
    private static final int ZOOM_LEVEL = 9;
    private UnfoldingMap map;
    private LargeMapImageUtils lmiUtils;

    @Override
    public void settings() {
        size(500, 500, P2D);
    }

    @Override
    public void setup() {
        map = new UnfoldingMap(this);
        map.zoomAndPanTo(ZOOM_LEVEL, LOCATION);
        MapUtils.createDefaultEventDispatcher(this, map);

        LOGGER.info("Init large map image.");
        lmiUtils = new LargeMapImageUtils(this, map);
    }

    @Override
    public void draw() {
        map.draw();
        lmiUtils.run();
    }

    @Override
    public void keyPressed() {
        if (key == 's') {
            // Around current center and with current zoom level
            lmiUtils.init();
        }
        if (key == 'b') {
            // Around set center and zoom level (pans there before screenshoting)
            lmiUtils.init(LOCATION, ZOOM_LEVEL);
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{LargeMapImageSaveApp.class.getName()});
    }
}
