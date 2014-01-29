package de.fhpotsdam.unfolding;

import processing.core.PApplet;
import processing.core.PVector;

import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.geo.Transformation;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class PrecisePositionsApp extends PApplet {

	UnfoldingMap map;
	DebugDisplay debugDisplay;

	public static final Location SINGAPORE_LOCATION = new Location(1.283f, 103.833f);

	Location location1 = new Location(1.283000, 103.833000);
	// loc2 has seemingly same pos than loc1
	Location location2 = new Location(1.283013, 103.833011);
	// loc3 has different pos than loc1
	Location location3 = new Location(1.283014, 103.833012);

	@Override
	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this, new Microsoft.AerialProvider());
		map.zoomAndPanTo(SINGAPORE_LOCATION, 14);
		map.setZoomRange(14, 22);

		MapUtils.createDefaultEventDispatcher(this, map);

		debugDisplay = new DebugDisplay(this, map);

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

		debugDisplay.draw();

		ScreenPosition pos1 = map.getScreenPosition(location1);
		ScreenPosition pos2 = map.getScreenPosition(location2);
		ScreenPosition pos3 = map.getScreenPosition(location3);

		printInfo("1", location1, pos1);
		printInfo("2", location2, pos2);
		printInfo("3", location3, pos3);

		noStroke();
		fill(255, 0, 0);
		ellipse(pos1.x, pos1.y, 6, 6);
		fill(0, 255, 0);
		ellipse(pos2.x, pos2.y, 6, 6);
		fill(0, 0, 255);
		ellipse(pos3.x, pos3.y, 6, 6);
	}

	private void printInfo(String info, Location location, ScreenPosition pos) {
		System.out.printf(info + " : lat = %.9f lon = %.9f x = %.9f y = %.9f \n", location.getLat(), location.getLon(),
				pos.x, pos.y);
		Coordinate coord = map.mapDisplay.getMapProvider().locationCoordinate(location).zoomTo(0);
		System.out.printf("\tcoord : col = %.9f row = %.9f \n", coord.column, coord.row);

		// from AbstractProjection#locationCoordinate
		PVector point1 = new PVector(PApplet.PI * location.y / 180.0f, PApplet.PI * location.x / 180.0f);
		System.out.printf("\tpoint1 : x = %.9f y = %.9f \n", point1.x, point1.y);

		// from MercatorProjection#rawProject
		PVector point2 = new PVector(point1.x, PApplet.log(PApplet.tan(0.25f * PApplet.PI + 0.5f * point1.y)));
		System.out.printf("\tpoint2 : x = %.9f y = %.9f \n", point2.x, point2.y);

		// from AbstractProjection#project
		Transformation transformation = new Transformation(1.068070779e7f, 0.0f, 3.355443185e7f, 0.0f,
				-1.068070890e7f, 3.355443057e7f);
		PVector point3 = transformation.transform(point2);
		System.out.printf("\tpoint3a : x = %.9f y = %.9f \n", point3.x, point3.y);

		double x = 1.068070779e7 * point2.x + 0.0 * point2.y + 3.355443185e7;
		double y = 0.0 * point2.x + -1.068070890e7 * point2.y + 3.355443057e7;
		System.out.printf("\tpoint3b : x = %.9f y = %.9f \n", x, y);
		PVector point3b = new PVector((float) x, (float) y);
		System.out.printf("\tpoint3b : x = %.9f y = %.9f \n", point3b.x, point3b.y);
	}
}
