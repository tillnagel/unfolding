package de.fhpotsdam.unfolding.examples.interaction;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.examples.marker.DrawOuterMarker;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.marker.SimpleMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Allows storing and restoring multiple snapshots. With markers.
 * 
 * @see {@link MapSnapshotApp}
 */
public class ComplexMapSnapshotApp extends PApplet {

	Map map;

	List<MapSnapshot> mapSnapshots = new ArrayList<MapSnapshot>();

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);

		MapUtils.createDefaultEventDispatcher(this, map);

		// Add random markers
		MarkerManager markerManager = new MarkerManager(map);
		for (int i = 0; i < 100; i++) {
			SimpleMarker marker = new SimpleMarker();
			marker.setLocation(random(30, 60), random(-10, 30));
			markerManager.addMarker(marker);
		}
		map.mapDisplay.addMarkerManager(markerManager);
	}

	public void draw() {
		background(0);
		map.draw();

		int y = 10;
		for (MapSnapshot mapSnapshot : mapSnapshots) {
			mapSnapshot.draw(10, y, 80, 60);
			y += 70;
		}
	}

	public void mouseClicked() {
		for (MapSnapshot mapSnapshot : mapSnapshots) {
			if (mapSnapshot.isInside(mouseX, mouseY)) {
				map.zoomAndPanTo(mapSnapshot.location, mapSnapshot.zoomLevel);
			}
		}
	}

	public void keyPressed() {
		if (key == 's') {
			MapSnapshot mapSnapshot = new MapSnapshot(this, map);
			println("Bookmarked map at " + mapSnapshot.location + " with " + mapSnapshot.zoomLevel);
			mapSnapshots.add(mapSnapshot);
		}
	}

}
