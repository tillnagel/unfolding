package de.fhpotsdam.unfolding.performance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.MapEvent;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays many markers on the map.
 * 
 * Filters dots only on map change.
 * Also calculates screen position for each dot only on map change! 
 * Stores visible pvectors in a separate temp list, and displays those.
 * 
 * 
 */
public class MillionDotsMapApp3 extends PApplet {

	UnfoldingMap map;
	List<Dot> dots = new ArrayList<Dot>();
	List<PVector> visibleDotVertices = new ArrayList<PVector>();

	Location tlLoc;
	Location brLoc;

	public void setup() {
		size(800, 600, OPENGL);
		smooth();

		dots = createRandomDots(100000);

		map = new UnfoldingMap(this);
		map.zoomToLevel(3);
		MapUtils.createDefaultEventDispatcher(this, map);

		mapChanged(null);
	}

	public void draw() {
		map.draw();

		fill(0, 180);
		noStroke();

		int zoomLevel = map.getZoomLevel();
		synchronized (visibleDotVertices) {
			for (PVector pos : visibleDotVertices) {
				if (zoomLevel <= 4) {
					rect(pos.x, pos.y, 4, 4);
				} else {
					// Draw more expensive representations on higher zoom levels (i.e. when fewer dots)
					ellipse(pos.x, pos.y, 8, 8);
				}
			}
		}

		fill(255);
		rect(5, 5, 180, 20);
		fill(0);
		text("fps: " + nfs(frameRate, 0, 2) + " (" + visibleDotVertices.size() + " dots)", 10, 20);
	}

	public void mapChanged(MapEvent mapEvent) {
		// Check map area only once after user interaction.
		// Additionally, instead of calculating the screen position each frame, store it in new list.
		brLoc = map.getBottomRightBorder();
		tlLoc = map.getTopLeftBorder();
		synchronized (visibleDotVertices) {
			visibleDotVertices.clear();
			for (Dot dot : dots) {
				if (dot.location.getLat() > brLoc.getLat() && dot.location.getLat() < tlLoc.getLat()
						&& dot.location.getLon() > tlLoc.getLon() && dot.location.getLon() < brLoc.getLon()) {
					PVector pos = map.getScreenPosition(dot.location);
					visibleDotVertices.add(pos);
				}
			}
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
