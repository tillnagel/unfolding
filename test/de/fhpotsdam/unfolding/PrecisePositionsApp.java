package de.fhpotsdam.unfolding;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class PrecisePositionsApp extends PApplet {

	UnfoldingMap map;

	public static final Location SINGAPORE_LOCATION = new Location(1.283f, 103.833f);
	Location location1 = new Location(1.283000f, 103.833000f);
	// loc2 has seemingly same pos than loc1
	Location location2 = new Location(1.283013f, 103.833011f);
	// loc3 has different pos than loc1
	Location location3 = new Location(1.283014f, 103.833012f);

	@Override
	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(SINGAPORE_LOCATION, 14);
		map.setZoomRange(14, 22);

		MapUtils.createDefaultEventDispatcher(this, map);

		map.updateMap();

		float x = 103.833000f;
		float y = 103.833002f;
		println(x + ", " + y);
		System.out.printf("x = %.9f y = %.9f \n", x, y);
		// println(PApplet.nf(x, 1, 9) + ", " + PApplet.nf(y, 1, 9));

		Marker marker1 = new SimplePointMarker(location1);
		Marker marker2 = new SimplePointMarker(location2);
		Marker marker3 = new SimplePointMarker(location3);
		map.addMarkers(marker1, marker2, marker3);
	}

	@Override
	public void draw() {
		background(0);
		map.draw();

		ScreenPosition pos1 = map.getScreenPosition(location1);
		ScreenPosition pos2 = map.getScreenPosition(location2);
		ScreenPosition pos3 = map.getScreenPosition(location3);

		Location locA = map.getLocationFromScreenPosition(mouseX, mouseY);
		Location locB = map.getLocationFromScreenPosition(mouseX + 5, mouseY + 5);

		ellipse(mouseX, mouseY, 10, 10);
		ellipse(mouseX + 5, mouseY + 5, 10, 10);
		println("locA " + locA);
		println("locB " + locB);

		fill(255, 0, 0);
		ellipse(pos1.x, pos1.y, 6, 6);
		fill(0, 255, 0);
		ellipse(pos2.x, pos2.y, 6, 6);
		fill(0, 0, 255);
		ellipse(pos3.x, pos3.y, 6, 6);
	}

}
