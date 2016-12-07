package de.fhpotsdam.unfolding.examples.marker.imagemarker;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Demonstrates how to use ImageMarkers with different icons. Note, the used icons contain translucent (the shadows) and
 * transparent (the inner holes) areas.
 */
public class ImageMarkerApp extends PApplet {

	Location berlinLocation = new Location(52.5f, 13.4f);
	Location veniceLocation = new Location(45.44f, 12.34f);
	Location lisbonLocation = new Location(38.71f, -9.14f);

	UnfoldingMap map;

	public void settings() {
		size(800, 600, P2D);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { ImageMarkerApp.class.getName() });
	}

	public void setup() {
		map = new UnfoldingMap(this);
		map.zoomAndPanTo(4, new Location(50.26f, 12.1f));
		MapUtils.createDefaultEventDispatcher(this, map);

		ImageMarker imgMarker1 = new ImageMarker(lisbonLocation, loadImage("ui/marker.png"));
		ImageMarker imgMarker2 = new ImageMarker(veniceLocation, loadImage("ui/marker_red.png"));
		ImageMarker imgMarker3 = new ImageMarker(berlinLocation, loadImage("ui/marker_gray.png"));
		map.addMarkers(imgMarker1, imgMarker2, imgMarker3);
	}

	public void draw() {
		map.draw();
	}

}
