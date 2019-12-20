package de.fhpotsdam.unfolding.examples.image;

import processing.core.PApplet;
import processing.core.PImage;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Shows a static image laid over an interactive background map.
 *
 * See {@link ImageOverlayApp} for more information.
 */
public class MapImageOverlayApp extends PApplet {

    UnfoldingMap map;
    Location center = new Location(52.396, 13.058);

    PImage visImg;
    Location visNorthWest = new Location(52.687, 13.06);
    Location visSouthEast = new Location(52.328, 13.78);
    
    @Override
    public void settings() {
        size(1400, 800, OPENGL);
    }

    @Override
    public void setup() {
        visImg = loadImage("http://upload.wikimedia.org/wikipedia/commons/thumb/5/56/Occupied_Berlin.svg/2000px-Occupied_Berlin.svg.png");

        map = new UnfoldingMap(this, "Satellite Map", new Microsoft.AerialProvider());
        map.zoomAndPanTo(center, 14);
        MapUtils.createDefaultEventDispatcher(this, map);
    }

    @Override
    public void draw() {
        tint(255);
        map.draw();

        ScreenPosition topRight = map.getScreenPosition(visNorthWest);
        ScreenPosition bottomLeft = map.getScreenPosition(visSouthEast);

        float width = bottomLeft.x - topRight.x;
        float height = bottomLeft.y - topRight.y;

        tint(255, 110);
        image(visImg, topRight.x, topRight.y, width, height);
    }
    
    public static void main(String args[]) {
        PApplet.main(new String[]{MapImageOverlayApp.class.getName()});
    }
}
