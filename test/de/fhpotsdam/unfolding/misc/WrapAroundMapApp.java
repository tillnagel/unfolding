package de.fhpotsdam.unfolding.misc;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class WrapAroundMapApp extends PApplet {

	UnfoldingMap map;

	UnfoldingMap leftMap;
	UnfoldingMap rightMap;

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this);
		map.zoomToLevel(2);
		EventDispatcher eventDispatcher = MapUtils.createDefaultEventDispatcher(this, map);

		leftMap = createWrappedMap(map, eventDispatcher, true);
		rightMap = createWrappedMap(map, eventDispatcher, false);
	}

	public UnfoldingMap createWrappedMap(UnfoldingMap mainMap, EventDispatcher eventDispatcher, boolean left) {
		UnfoldingMap wrappedMap = new UnfoldingMap(this);
		wrappedMap.zoomToLevel(mainMap.getZoomLevel());
		eventDispatcher.register(wrappedMap, "zoom", mainMap.getId());
		return wrappedMap;
	}

	public void draw() {
		background(0, 255, 0);

		updateMap(map, leftMap, true);
		updateMap(map, rightMap, false);

		map.draw();
		leftMap.draw();
		rightMap.draw();
	}

	public void updateMap(UnfoldingMap mainMap, UnfoldingMap nextMap, boolean left) {
		float degree = (left) ? -180 : 180;

		// Move next map
		ScreenPosition pos = mainMap.getScreenPosition(new Location(0, degree));
		nextMap.move(pos.x, 0);
		if (left) {
			nextMap.moveBy(-800, 0);
		}

		// Pan next map
		nextMap.panTo(new Location(0, 0));
		ScreenPosition map1RightPos = mainMap.getScreenPosition(new Location(0, degree));
		Location map1RightLocation = nextMap.getLocation(map1RightPos);
		float lonDiff = (-map1RightLocation.getLon()) - degree;
		nextMap.panTo(new Location(-map1RightLocation.getLat(), lonDiff));

		// Ensure next map is always over main map (push 1px)
		float fixLastPixel = (left) ? 1 : -1;
		nextMap.panBy(fixLastPixel, 0);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { WrapAroundMapApp.class.getName() });
	}

}
