package de.fhpotsdam.unfolding.examples.performance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Displays a million markers on the map.
 * 
 * 
 * 
 * 
 * Tests
 * - pure drawing (fps for 10k, 100k, 1000k.)
 * - pure drawing markers (ditto)
 * - cut-off on map border
 * - GLGraphics vertices
 * 
 * 
 * 
 * Outcomes - rect is faster than ellipse (20k. rect: 24fps, ellipse: 7fps)
 * 
 */
public class MillionDotsMapApp extends PApplet {

	UnfoldingMap map;
	List<Dot> dots = new ArrayList<Dot>();

	Location tlLoc;
	Location brLoc;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		smooth();

		map = new UnfoldingMap(this);
		MapUtils.createDefaultEventDispatcher(this, map);

		dots = createRandomDots();

	}

	public void draw() {
		map.draw();
		brLoc = map.getBottomRightBorder();
		tlLoc = map.getTopLeftBorder();

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

	private List<Dot> createRandomDots() {
		List<Dot> dots = new ArrayList<Dot>();
		for (int i = 0; i < 200000; i++) {
			Dot dot = new Dot(new Location(random(-85, 85), random(-180, 180)), new Date());
			dots.add(dot);
		}
		return dots;
	}

}
