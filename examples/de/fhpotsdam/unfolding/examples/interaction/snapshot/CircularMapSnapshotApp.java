package de.fhpotsdam.unfolding.examples.interaction.snapshot;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.StamenMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Users can save snapshots of the current map, which are then shown as radial thumbnails. By clicking on one of those
 * thumbnails the map pans and zooms to the stored location.
 * 
 * Press 's' to take a snapshot of the current map. Click on its thumbnail to restore.
 * 
 * See {@link MapSnapshot} and {@link CircularMapSnapshot} for creating the actual snapshot and storage of metadata.
 */
public class CircularMapSnapshotApp extends PApplet {

	UnfoldingMap map;

	List<MapSnapshot> mapSnapshots = new ArrayList<MapSnapshot>();

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this, 0, 0, 400, 400, new StamenMapProvider.WaterColor());
		map.zoomAndPanTo(10, new Location(51.507222, -0.1275));

		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		map.draw();

		int x = 415;
		int y = 20;
		for (MapSnapshot mapSnapshot : mapSnapshots) {
			mapSnapshot.draw(x, y, 80, 80);
			x += 90;
			if (x > width - 90) {
				x = 415;
				y += 90;
			}
		}
	}

	public void mouseClicked() {
		for (MapSnapshot mapSnapshot : mapSnapshots) {
			if (mapSnapshot.isInside(mouseX, mouseY)) {
				map.zoomAndPanTo(mapSnapshot.zoomLevel, mapSnapshot.location);
			}
		}
	}

	public void keyPressed() {
		if (key == 's') {
			MapSnapshot mapSnapshot = new CircularMapSnapshot(this, map);
			println("Bookmarked map at " + mapSnapshot.location + " with " + mapSnapshot.zoomLevel);
			mapSnapshots.add(mapSnapshot);
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { CircularMapSnapshotApp.class.getName() });
	}

}
