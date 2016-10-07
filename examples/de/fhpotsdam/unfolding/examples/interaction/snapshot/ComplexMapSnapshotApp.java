package de.fhpotsdam.unfolding.examples.interaction.snapshot;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Allows storing and restoring multiple snapshots. With markers.
 * <p>
 * Press 's' to take a snapshot of the current map. Click on its thumbnail to restore.
 *
 * @see {@link MapSnapshotApp}
 */
public class ComplexMapSnapshotApp extends PApplet {

    public static final Logger LOGGER = Logger.getLogger(ComplexMapSnapshotApp.class);
    private UnfoldingMap map;
    private List<MapSnapshot> mapSnapshots = new ArrayList<MapSnapshot>();

    @Override
    public void settings() {
        size(800, 600, P2D);
    }

    @Override
    public void setup() {
        map = new UnfoldingMap(this);
        map.zoomAndPanTo(10, new Location(52.5f, 13.4f));
        MapUtils.createDefaultEventDispatcher(this, map);

        addRandomMarkers();
    }

    protected void addRandomMarkers() {
        // Add some markers to demonstrate they are visible in thumbnail, too.
        for (int i = 0; i < 100; i++) {
            final SimplePointMarker marker = new SimplePointMarker();
            marker.setLocation(random(30, 60), random(-10, 30));
            map.addMarker(marker);
        }
    }

    @Override
    public void draw() {
        background(0);
        map.draw();

        int y = 10;
        for (final MapSnapshot mapSnapshot : mapSnapshots) {
            mapSnapshot.draw(10, y, 80, 60);
            y += 70;
        }
    }

    @Override
    public void mouseClicked() {
        for (final MapSnapshot mapSnapshot : mapSnapshots) {
            if (mapSnapshot.isInside(mouseX, mouseY)) {
                map.zoomAndPanTo(mapSnapshot.zoomLevel, mapSnapshot.location);
            }
        }
    }

    @Override
    public void keyPressed() {
        if (key == 's') {
            MapSnapshot mapSnapshot = new MapSnapshot(this, map);
            LOGGER.info("Bookmarked map at " + mapSnapshot.location + " with " + mapSnapshot.zoomLevel);
            mapSnapshots.add(mapSnapshot);
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{ComplexMapSnapshotApp.class.getName()});
    }

}
