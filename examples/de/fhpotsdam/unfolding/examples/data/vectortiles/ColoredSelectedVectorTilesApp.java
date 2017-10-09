package de.fhpotsdam.unfolding.examples.data.vectortiles;

import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Loads vector tiles, and displays museums in a different color.
 * 
 * Click on the map to load its vector tiles.
 */
public class ColoredSelectedVectorTilesApp extends PApplet {

	UnfoldingMap map;
	DebugDisplay debugDisplay;

	VectorTilesUtils vectorTilesUtils;

	String filteredType = "museum";

	public void settings() {
		size(800, 600, P2D);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { ColoredSelectedVectorTilesApp.class.getName() });
	}

	public void setup() {
		map = new UnfoldingMap(this, "myMap");
		map.zoomAndPanTo(16, new Location(52.501, 13.395));
		MapUtils.createDefaultEventDispatcher(this, map);

		debugDisplay = new DebugDisplay(this, map);

		vectorTilesUtils = new VectorTilesUtils(this, map, VectorTilesApp.MAPZEN_API_KEY);

		loadAndAddColoredMarkers(width / 2, height / 2, filteredType);
	}

	public void draw() {
		map.draw();
		debugDisplay.draw();
	}

	public void mouseClicked() {
		loadAndAddColoredMarkers(mouseX, mouseY, filteredType);
	}

	public void loadAndAddColoredMarkers(int x, int y, String filteredType) {
		List<Marker> markers = vectorTilesUtils.loadMarkersForScreenPos("buildings", x, y);
		for (Marker marker : markers) {
			String kind = marker.getStringProperty("kind");
			if (filteredType.equals(kind)) {
				marker.setColor(color(0, 255, 0, 200));
			}
		}
		map.addMarkers(markers);
	}

}
