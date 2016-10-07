package de.fhpotsdam.unfolding.examples.image;

import processing.core.PApplet;
import processing.core.PImage;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Shows a static image laid over an interactive background map.
 * <p>
 * See {@link ImageOverlayApp} for more information.
 */
public class SmallImageOverlayApp extends PApplet {

    private static final Location CENTER_LOCATION = new Location(52.396, 13.058);
    private static final Location VIS_NORTH_WEST = new Location(52.399539, 13.048003);
    private static final Location VIS_SOUTH_EAST = new Location(52.391667, 13.066667);
    private UnfoldingMap map;
    private DebugDisplay debugDisplay;
    private PImage visImg;

    @Override
    public void settings() {
        size(1400, 800, P2D);
    }

    @Override
    public void setup() {
        visImg = loadImage("test/splendor-cutout.png");

        map = new UnfoldingMap(this, "Satellite Map", new Microsoft.AerialProvider());
        map.zoomAndPanTo(16, CENTER_LOCATION);
        MapUtils.createDefaultEventDispatcher(this, map);

        debugDisplay = new DebugDisplay(this, map);
    }

    public void draw() {
        map.draw();

        final ScreenPosition topRight = map.getScreenPosition(VIS_NORTH_WEST);
        final ScreenPosition bottomLeft = map.getScreenPosition(VIS_SOUTH_EAST);

        final float width = bottomLeft.x - topRight.x;
        final float height = bottomLeft.y - topRight.y;

        tint(255, 127);
        image(visImg, topRight.x, topRight.y, width, height);

        debugDisplay.draw();
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{SmallImageOverlayApp.class.getName()});
    }
}
