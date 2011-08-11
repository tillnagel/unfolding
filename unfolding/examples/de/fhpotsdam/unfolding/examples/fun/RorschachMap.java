package de.fhpotsdam.unfolding.examples.fun;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.mapdisplay.FlippedMapDisplay;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class RorschachMap extends PApplet {

	Map map1;
	Map map2;
	Map map3;
	Map map4;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map1 = new Map(this, "map1", 0, 0, 400, 300, true, false, new Microsoft.AerialProvider());
		map1.mapDisplay = new FlippedMapDisplay(this, new Microsoft.AerialProvider(), 0, 0, 400, 300);
		map2 = new Map(this, "map2", 400, 0, 400, 300, true, false, new Microsoft.AerialProvider());
		map3 = new Map(this, "map3", 0, 300, 400, 300, true, false, new Microsoft.AerialProvider());
		map4 = new Map(this, "map4", 400, 300, 400, 300, true, false, new Microsoft.AerialProvider());
		EventDispatcher eventDispatcher = MapUtils.createDefaultEventDispatcher(this, map1, map2, map3, map4);

		//map1.rotate(PI);

		// All maps listen to all pan interactions. (Note: Maps do not register to themselves,
		// again. This has been done in default EventDispatcher, above.)
		eventDispatcher.register(map1, "pan", map2.getId(), map3.getId(), map4.getId());
		eventDispatcher.register(map2, "pan", map1.getId(), map3.getId(), map4.getId());
		eventDispatcher.register(map3, "pan", map1.getId(), map2.getId(), map4.getId());
		eventDispatcher.register(map4, "pan", map1.getId(), map2.getId(), map3.getId());
	}

	public void draw() {
		background(0);

		map1.draw();
		map2.draw();
		map3.draw();
		map4.draw();
	}

}
