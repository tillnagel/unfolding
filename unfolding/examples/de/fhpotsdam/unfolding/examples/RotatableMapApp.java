package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import processing.core.PMatrix2D;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class RotatableMapApp extends PApplet {

	Map map;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this, "map1", 50, 50, 700, 500);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		textFont(loadFont("Miso-Light-12.vlw"), 20);
	}

	public void draw() {
		background(0);

		map.draw();
		
		Location location = map.getLocation(mouseX, mouseY);
		fill(0, 250);
		rect(10, 560, 150, 30);
		fill(255);
		text(location.toString(), 10, 580);
	}
	
	public void keyPressed() {
		if (key == 'r') {
			map.rotate(-0.1);
		} else if (key == 'l') {
			map.rotate(0.1);
		}
	}

}
