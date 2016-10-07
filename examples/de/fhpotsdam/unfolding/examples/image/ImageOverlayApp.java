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
 * Shows a static image laid over an interactive background map. The north-west and south-east boundaries of the image
 * are used to position and scale the image. (There is no tile mechanism for the overlay image).
 * <p>
 * In this example, the image shows from where people take pictures of famous sights. The visualization is laid over a
 * satellite map. Below, you can see how people photograph the Hans-Otto-Theater in Potsdam, Germany. See
 * http://schwinki.de/splendor/ for more information.
 */
public class ImageOverlayApp extends PApplet {

    private static final Location CENTER_LOCATION = new Location(52.407, 13.05);
    private UnfoldingMap map;
    private DebugDisplay debugDisplay;
    private PImage visImg;

    // Kaiserbahnhof Potsdam, 52.39466, 13.013944 (west)
    // Meierei, 52.421944, 13.069722 (north)
    // Schloss Babelsberg, 52.407639, 13.093289 (east)
    // Potsdam Hauptbahnhof, 52.391667, 13.066667 (south)

    private static final Location VIS_NORTH_WEST = new Location(52.421944, 13.013944);
    private static final Location VIS_SOUTH_EAST = new Location(52.391667, 13.093289);

    @Override
    public void settings() {
        size(1400, 800, P2D);
    }

    @Override
    public void setup() {
        // Image from http://schwinki.de/splendor/
        visImg = loadImage("test/splendor-big.jpg");

        map = new UnfoldingMap(this, "Satellite Map", new Microsoft.AerialProvider());
        map.zoomAndPanTo(15, CENTER_LOCATION);
        MapUtils.createDefaultEventDispatcher(this, map);

        debugDisplay = new DebugDisplay(this, map);
    }

    @Override
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
        PApplet.main(new String[]{ImageOverlayApp.class.getName()});
    }
}
