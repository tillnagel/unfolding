package de.fhpotsdam.unfolding.performance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.MapEvent;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Displays many markers on the map.
 * 
 * Filters dots only on map change. Stores visible dots in a separate temp list, and displays those.
 * 
 */
public class MillionDotsMapApp2 extends PApplet {

	UnfoldingMap map;
	List<Dot> dots = new ArrayList<Dot>();
	List<Dot> visibleDots = new ArrayList<Dot>();

	Location tlLoc;
	Location brLoc;

	public void setup() {
		size(800, 600, OPENGL);
		smooth();

		dots = createRandomDots(50000);

		map = new UnfoldingMap(this);
		map.zoomToLevel(3);
		MapUtils.createDefaultEventDispatcher(this, map);

		mapChanged(null);
	}

	public void draw() {
		map.draw();

		fill(0, 180);
		noStroke();

		synchronized (visibleDots) {
			for (Dot dot : visibleDots) {
				ScreenPosition pos = map.getScreenPosition(dot.location);
				if (map.getZoomLevel() <= 4) {
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
		text("fps: " + nfs(frameRate, 0, 2) + " (" + visibleDots.size() + " dots)", 10, 20);
	}

	public void mapChanged(MapEvent mapEvent) {
		// println("mapChanged: " + mapEvent);

		brLoc = map.getBottomRightBorder();
		tlLoc = map.getTopLeftBorder();
		synchronized (visibleDots) {
			visibleDots.clear();
			for (Dot dot : dots) {
				if (dot.location.getLat() > brLoc.getLat() && dot.location.getLat() < tlLoc.getLat()
						&& dot.location.getLon() > tlLoc.getLon() && dot.location.getLon() < brLoc.getLon()) {
					visibleDots.add(dot);
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
