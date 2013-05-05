package de.fhpotsdam.unfolding.examples.data.styled;

import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;

/**
 * Own marker with custom style. Only handles a single geo-point. 
 */
public class EarthquakeMarker extends SimplePointMarker {

	public EarthquakeMarker(Location location) {
		super(location);
	}

	public void draw(PGraphics pg, float x, float y) {
		pg.pushStyle();
		pg.noStroke();
		pg.fill(200, 200, 0, 100);
		pg.ellipse(x, y, 40, 40);
		pg.fill(255, 100);
		pg.ellipse(x, y, 30, 30);
		pg.fill(200, 200, 0, 100);
		pg.ellipse(x, y, 20, 20);
		pg.fill(255, 200);
		pg.ellipse(x, y, 10, 10);
		pg.popStyle();
	}
}
