package de.fhpotsdam.unfolding.marker;

import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;

// Test code by platinumICE https://github.com/platinumICE

public class PointMarkerSizeTestApp extends PApplet {

	UnfoldingMap map;

	public void settings() {
		size(600, 600, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this);
		Location moscow = new Location(55.75f, 37.61f);
		map.zoomToLevel(15);
		map.panTo(moscow);

		SimplePointMarker m = new SimplePointMarker(moscow);
		m.setDiameter(200f);
		m.setColor(color(255, 0, 0, 127));

		map.addMarker(m);
	}

	public void draw() {
		background(240);

		// Drawing Markers in handled internally
		map.draw();

		fill(0, 127);
		ellipseMode(RADIUS);
		ellipse(width / 2, height / 2, 200f, 200f);

	}

	public void mouseClicked() {
		List<Marker> hits = map.getHitMarkers(mouseX, mouseY);
		if (hits.size() > 0) {
			println("Marker is hit");
		} else {
			println("Marker is missed");
		}
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { PointMarkerSizeTestApp.class.getName() });
	}

}