package de.fhpotsdam.unfolding.examples.interaction.snapshot;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Stores and restores a snapshot of the current map view. Each snapshot consists of location, zoom level, and a
 * thumbnail.
 * 
 * Press 's' to take a snapshot of the current map. Click on its thumbnail to restore.
 */
public class MapSnapshotApp extends PApplet {

	UnfoldingMap map;

	MapSnapshot mapSnapshot;

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);

		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		map.draw();

		if (mapSnapshot != null) {
			mapSnapshot.draw(10, 10, 80, 60);
		}
	}

	public void mouseClicked() {
		if (mapSnapshot != null && mouseX > 10 && mouseX < 90 && mouseY > 10 && mouseY < 70) {
			map.zoomAndPanTo(mapSnapshot.location, mapSnapshot.zoomLevel);
		}
	}

	public void keyPressed() {
		if (key == 's') {
			mapSnapshot = new MapSnapshot(this, map);
			println("Bookmarked map at " + mapSnapshot.location + " with " + mapSnapshot.zoomLevel);
		}
	}

}
