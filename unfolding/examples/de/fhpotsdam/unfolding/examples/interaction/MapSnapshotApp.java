package de.fhpotsdam.unfolding.examples.interaction;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class MapSnapshotApp extends PApplet {

	Map map;

	MapSnapshot mapSnapshot;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this);
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
