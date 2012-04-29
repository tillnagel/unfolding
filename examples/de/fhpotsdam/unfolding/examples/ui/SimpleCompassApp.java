package de.fhpotsdam.unfolding.examples.ui;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.ui.*;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class SimpleCompassApp extends PApplet {

	Map map;
	CompassUI compass;

	public void setup() {
		size(800, 600);

		map = new Map(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		compass = new CompassUI(this,map);

		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		map.draw();
		compass.draw();
	}
	
	public void keyPressed() {
		if (key == 'a') map.rotate(0.1f);
		if (key == 'd') map.rotate(-0.1f);
		}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.SimpleCompassApp" });
	}

}