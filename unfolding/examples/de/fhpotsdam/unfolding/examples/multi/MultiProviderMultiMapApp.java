package de.fhpotsdam.unfolding.examples.multi;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class MultiProviderMultiMapApp extends PApplet {

	Map map1;
	Map map2;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map1 = new Map(this, "map1", 10, 10, 385, 580, true, false, new Microsoft.AerialProvider());
		map1.setTweening(false);
		map2 = new Map(this, "map2", 405, 10, 385, 580, true, false,
				new OpenStreetMap.CloudmadeProvider(MapDisplayFactory.OSM_API_KEY,
						30635));
		map2.setTweening(false);
		MapUtils.createDefaultEventDispatcher(this, map1, map2);
	}

	public void draw() {
		background(0);

		map1.draw();
		map2.draw();
	}

}
