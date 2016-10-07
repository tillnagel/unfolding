package de.fhpotsdam.unfolding.examples.geo;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Shows a circle with a 5km radius.
 */
public class GetDistanceApp extends PApplet {

    private UnfoldingMap map;

    @Override
    public void settings() {
        size(800, 600, P2D);
    }

    @Override
    public void setup() {
        map = new UnfoldingMap(this);
        map.zoomAndPanTo(10, new Location(52.5f, 13.4f));
        MapUtils.createDefaultEventDispatcher(this, map);
    }

    @Override
    public void draw() {
        background(255);
        map.draw();

        final Location mainLocation = new Location(52.52f, 13.42f);
        final ScreenPosition pos = map.getScreenPosition(mainLocation);
        float distanceInKm = getDistance(mainLocation, 5);
        fill(255, 255, 0, 127);
        ellipse(pos.x, pos.y, distanceInKm, distanceInKm);
    }

    /**
     * This method gets a distance between two screen positions basing on distance between two locations
     *
     * @param mainLocation start location
     * @param size         distance to finish location
     * @return distance between screen positions
     */
    public float getDistance(final Location mainLocation, final float size) {
        final Location tempLocation = GeoUtils.getDestinationLocation(mainLocation, 90, size);
        final ScreenPosition pos1 = map.getScreenPosition(mainLocation);
        final ScreenPosition pos2 = map.getScreenPosition(tempLocation);
        return dist(pos1.x, pos1.y, pos2.x, pos2.y);
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{GetDistanceApp.class.getName()});
    }

}
