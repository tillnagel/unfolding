package de.fhpotsdam.unfolding.examples.misc;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Shows basic information about two independent maps from different providers.
 * 
 * The information widget also shows map events, as the eventDispatcher is passed to the DebugDisplay.
 * 
 * @see {@link de.fhpotsdam.unfolding.examples.events.ComplexMapEventApp ComplexMapEventApp} for DebugDisplays showing
 *      complex map events.
 */
public class MultiDebugDisplayApp extends PApplet {

	UnfoldingMap map1;
	UnfoldingMap map2;
	DebugDisplay debugDisplay1;
	DebugDisplay debugDisplay2;

	public void settings() {
		size(1024, 768, P2D);
	}

	public void setup() {
		map1 = new UnfoldingMap(this, "map1", 0, 0, 512, height);
		map1.zoomAndPanTo(10, new Location(52.5f, 13.4f));
		map2 = new UnfoldingMap(this, "map2", 512, 0, 512, height, true, false, new Microsoft.AerialProvider());
		map2.zoomTo(4);
		EventDispatcher eventDispatcher = MapUtils.createDefaultEventDispatcher(this, map1, map2);

		// Create debug displays
		debugDisplay1 = new DebugDisplay(this, map1, eventDispatcher, 10, 10);
		debugDisplay2 = new DebugDisplay(this, map2, eventDispatcher, 522, 10);
	}

	public void draw() {
		map1.draw();
		debugDisplay1.draw();

		map2.draw();
		debugDisplay2.draw();
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { MultiDebugDisplayApp.class.getName() });
	}
}
