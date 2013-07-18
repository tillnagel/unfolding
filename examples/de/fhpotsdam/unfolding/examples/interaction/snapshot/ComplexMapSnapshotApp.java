package de.fhpotsdam.unfolding.examples.interaction.snapshot;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Allows storing and restoring multiple snapshots. With markers.
 * 
 * Press 's' to take a snapshot of the current map. Click on its thumbnail to restore.
 * 
 * @see {@link MapSnapshotApp}
 */
public class ComplexMapSnapshotApp extends PApplet {

	UnfoldingMap map;

	List<MapSnapshot> mapSnapshots = new ArrayList<MapSnapshot>();

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		MapUtils.createDefaultEventDispatcher(this, map);

		addRandomMarkers();
	}

	public void addRandomMarkers() {
		// Add some markers to demonstrate they are visible in thumbnail, too.
		for (int i = 0; i < 100; i++) {
			SimplePointMarker marker = new SimplePointMarker();
			marker.setLocation(random(30, 60), random(-10, 30));
			map.addMarker(marker);
		}
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
