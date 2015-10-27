package de.fhpotsdam.unfolding.examples.interaction;

import processing.core.PApplet;
import processing.core.PFont;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Simple manual navigation example. Click on one of the two buttons to jump to specific locations.
 */
public class NaviButtonMapApp extends PApplet {

	Location berlinLocation = new Location(52.51861f, 13.408056f);
	int berlinZoomLevel = 10;
	Location universityLocation = new Location(52.411613f, 13.051779f);
	int universityZoomLevel = 14;

	UnfoldingMap map;
	PFont font;

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		font = createFont("sans-serif", 14);

		map = new UnfoldingMap(this, "map", 0, 0, 600, 600);
		map.setTweening(true);
		map.zoomToLevel(3);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		map.draw();

		drawButtons();
	}

	public void mouseReleased() {
		if (mouseX > 610 && mouseX < 790 && mouseY > 10 && mouseY < 90) {
			map.zoomAndPanTo(berlinZoomLevel, berlinLocation);

		} else if (mouseX > 610 && mouseX < 790 && mouseY > 110 && mouseY < 190) {
			map.zoomAndPanTo(universityZoomLevel, universityLocation);
		}
	}

	public void drawButtons() {
		textFont(font);
		textSize(14);

		// Simple Berlin button
		fill(127);
		stroke(200);
		strokeWeight(2);
		rect(610, 10, 180, 80);
		fill(0);
		text("Berlin (zoom 10)", 620, 52);

		// FHP button
		fill(127);
		rect(610, 110, 180, 80);
		fill(0);
		text("University (zoom 14)", 620, 152);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { NaviButtonMapApp.class.getName() });
	}

}
