package de.fhpotsdam.unfolding;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Microsoft;

public class TransparentThingsOnOpenGLMapApp extends PApplet {

	UnfoldingMap map;
	SimplePointMarker marker;

	public void setup() {
		size(600, 600, OPENGL);
		map = new UnfoldingMap(this, new Microsoft.AerialProvider());

		marker = new SimplePointMarker(new Location(-15, -40));
		marker.setColor(color(0, 0, 255, 127));
		marker.setRadius(50);
		
		map.addMarker(marker);
	}

	public void draw() {
		background(0);
		map.draw();

		fill(255, 0, 0, 50);
		ellipse(width / 2, height / 2, 200, 200);
	}

	public void test() {
		// PGraphics pg = createGraphics((int) width, (int) height);
		// pg.beginDraw();
		// pg.background(0);
		// pg.endDraw();
		//
		// pushStyle();
		// blendMode(REPLACE);
		// image(pg, 0, 0);
		// popStyle();
		// //blendMode(BLEND);
	}

}
