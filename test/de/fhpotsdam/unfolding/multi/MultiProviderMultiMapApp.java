package de.fhpotsdam.unfolding.multi;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Two independent maps, with own interactions and different providers.
 */
public class MultiProviderMultiMapApp extends PApplet {

	UnfoldingMap map1;
	UnfoldingMap map2;

	public void setup() {
		size(800, 600, OPENGL);

		map1 = new UnfoldingMap(this, "map1", 10, 10, 385, 580, true, false, new Microsoft.AerialProvider());
		map2 = new UnfoldingMap(this, "map2", 405, 10, 385, 580, true, false, new OpenStreetMap.CloudmadeProvider(
				MapDisplayFactory.OSM_API_KEY, 30635));
		MapUtils.createDefaultEventDispatcher(this, map1, map2);
	}

	public void draw() {
		background(0);

		map1.draw();
		map2.draw();
	}

}
