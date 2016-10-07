package de.fhpotsdam.unfolding.examples.events;

import org.apache.log4j.Logger;
import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.events.MapEvent;
import de.fhpotsdam.unfolding.events.MapEventListener;
import de.fhpotsdam.unfolding.events.PanMapEvent;
import de.fhpotsdam.unfolding.events.ZoomMapEvent;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Advanced! This example shows the internal way of listening to map events. Look into {@link MapChangedApp} for the
 * standard way.
 * <p>
 * By implementing MapEventListener and registering this PApplet, it handles events in the same way as the maps
 * themselves.
 */
public class InternalMapEventListenerApp extends PApplet implements MapEventListener {

    private static final Logger LOGGER = Logger.getLogger(InternalMapEventListenerApp.class);
    private Location oldLocation = new Location(0, 0);
    private UnfoldingMap map;

    @Override
    public void settings() {
        size(800, 600, P2D);
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{InternalMapEventListenerApp.class.getName()});
    }

    @Override
    public void setup() {
        map = new UnfoldingMap(this, "myMap");

        final EventDispatcher eventDispatcher = MapUtils.createDefaultEventDispatcher(this, map);
        // Register this PApplet to listen to map events
        eventDispatcher.register(this, "pan", map.getId());
        eventDispatcher.register(this, "zoom", map.getId());
    }

    @Override
    public void draw() {
        map.draw();
    }

    @Override
    public void onManipulation(MapEvent mapEvent) {
        LOGGER.info("Map Interaction: " + mapEvent.getType() + " with subtype " + mapEvent.getSubType() + " in scope "
                + mapEvent.getScopeId());

        if (mapEvent.getType().equals(PanMapEvent.TYPE_PAN)) {
            Location location = map.getCenter();
            double dist = GeoUtils.getDistance(oldLocation, location);
            LOGGER.info("Panned by " + dist + " km");
            oldLocation = location;
        }

        if (mapEvent.getType().equals(ZoomMapEvent.TYPE_ZOOM)) {
            // Do something with the event
            // ZoomMapEvent zoomMapEvent = (ZoomMapEvent) mapEvent;
        }
    }

    @Override
    public String getId() {
        return "app";
    }
}
