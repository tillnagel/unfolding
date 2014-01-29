package de.fhpotsdam.unfolding.performance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Displays many dots on a map. Only shows dots within a region, either the visible area, or set via mouse click.
 * 
 * Dots are filtered each frame. Location is calculated for each dot each frame.
 * 
 */
public class MillionDotsMapApp1 extends PApplet {

	UnfoldingMap map;
	List<Dot> dots = new ArrayList<Dot>();

	Location tlLoc;
	Location brLoc;

	boolean useMouseRect = false;

	public void setup() {
		size(800, 600, OPENGL);
		smooth();

		map = new UnfoldingMap(this);
		map.zoomToLevel(3);
		MapUtils.createDefaultEventDispatcher(this, map);

		dots = createRandomDots(50000);
		brLoc = map.getBottomRightBorder();
		tlLoc = map.getTopLeftBorder();
	}

	public void draw() {
		map.draw();

		if (!useMouseRect) {
			brLoc = map.getBottomRightBorder();
			tlLoc = map.getTopLeftBorder();
		}

		fill(0, 180);
		noStroke();

		int visibleDotNumber = 0;
		for (Dot dot : dots) {
			if (dot.location.getLat() > brLoc.getLat() && dot.location.getLat() < tlLoc.getLat()
					&& dot.location.getLon() > tlLoc.getLon() && dot.location.getLon() < brLoc.getLon()) {

				ScreenPosition pos = map.getScreenPosition(dot.location);
				if (map.getZoomLevel() <= 4) {
					rect(pos.x, pos.y, 4, 4);
				} else {
					// Draw more expensive representations on higher zoom levels (i.e. when fewer dots)
					ellipse(pos.x, pos.y, 8, 8);
				}

				visibleDotNumber++;
			}
		}

		fill(255);
		rect(5, 5, 180, 20);
		fill(0);
		text("fps: " + nfs(frameRate, 0, 2) + " (" + visibleDotNumber + " dots)", 10, 20);
	}

	public void mouseClicked() {
		if (mouseButton == LEFT) {
			tlLoc = map.getLocation(mouseX, mouseY);
			println("tlLoc:" + tlLoc);
		} else {
			brLoc = map.getLocation(mouseX, mouseY);
			println("brLoc:" + brLoc);
		}
	}

	private List<Dot> createRandomDots(int dotNumbers) {
		List<Dot> dots = new ArrayList<Dot>();
		for (int i = 0; i < dotNumbers; i++) {
			Dot dot = new Dot(new Location(random(-85, 85), random(-180, 180)), new Date());
			dots.add(dot);
		}
		return dots;
	}

}
