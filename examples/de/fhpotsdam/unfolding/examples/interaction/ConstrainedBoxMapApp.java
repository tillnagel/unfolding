package de.fhpotsdam.unfolding.examples.interaction;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Manually constrains the map to a rectangular area. Users can only pan within this specified box.
 * <p>
 * See {@link ConstrainedMapApp} for Unfolding's built-in (but radial) constraint methods.
 */
public class ConstrainedBoxMapApp extends PApplet {

    private static final Location BOUND_TOP_LEFT = new Location(52.8, 12.6);
    private static final Location BOUND_BOTTOM_RIGHT = new Location(52.0, 14.5);
    private UnfoldingMap map;

    @Override
    public void settings() {
        size(800, 600, P2D);
    }

    @Override
    public void setup() {
        map = new UnfoldingMap(this);
        map.zoomAndPanTo(10, new Location(52.5, 13.4f));
        map.setZoomRange(10, 12);
        map.setTweening(true);
        map.setRectangularPanningRestriction(BOUND_TOP_LEFT, BOUND_BOTTOM_RIGHT);
        MapUtils.createDefaultEventDispatcher(this, map);
    }

    @Override
    public void draw() {
        background(0);
        map.draw();
    }

    @Override
    public void keyPressed() {
        if (key == ' ') {
            map.resetPanningRestriction();
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{ConstrainedBoxMapApp.class.getName()});
    }
}
