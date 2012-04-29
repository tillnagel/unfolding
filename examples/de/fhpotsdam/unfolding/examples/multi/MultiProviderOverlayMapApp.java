package de.fhpotsdam.unfolding.examples.multi;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.ImmoScout;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * A two layer map. One map lays atop another one with same size and position.
 * 
 * @author tillnagel
 */
public class MultiProviderOverlayMapApp extends PApplet {

	Map map1;
	Map map2;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map1 = new Map(this, "map1", 0, 0, width, height, true, false, new Microsoft.RoadProvider());
		map1.setTweening(false);
		map1.zoomAndPanTo(new Location(52.439046f, 13.447266f), 8);

		map2 = new Map(this, "map2", 0, 0, width, height, true, false,
				new ImmoScout.HeatMapProvider());
		map2.setTweening(false);
		map2.zoomToLevel(8);
		map2.zoomAndPanTo(new Location(52.439046f, 13.447266f), 8);

		MapUtils.createDefaultEventDispatcher(this, map1, map2);
	}

	public void draw() {
		background(0);

		map1.draw();
		tint(255, 100);
		map2.draw();
	}

}
