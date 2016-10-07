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

	private static final Location BERLIN_LOCATION = new Location(52.5f, 13.4f);
	private static final Location VENICE_LOCATION = new Location(45.44f, 12.34f);
	private static final Location LISBON_LOCATION = new Location(38.71f, -9.14f);

	private UnfoldingMap map;

	@Override
	public void settings() {
		size(800, 600, P2D);
	}

	@Override
    public void setup() {
		map = new UnfoldingMap(this);
		map.zoomAndPanTo(4, new Location(50.26f, 12.1f));
		MapUtils.createDefaultEventDispatcher(this, map);

		final ImageMarker imgMarker1 = new ImageMarker(LISBON_LOCATION, loadImage("ui/marker.png"));
		final ImageMarker imgMarker2 = new ImageMarker(VENICE_LOCATION, loadImage("ui/marker_red.png"));
		final ImageMarker imgMarker3 = new ImageMarker(BERLIN_LOCATION, loadImage("ui/marker_gray.png"));
		map.addMarkers(imgMarker1, imgMarker2, imgMarker3);
	}

	@Override
    public void draw() {
        background(0);
        map.draw();
	}

    public static void main(String args[]) {
        PApplet.main(new String[]{ImageMarkerApp.class.getName()});
    }

}
