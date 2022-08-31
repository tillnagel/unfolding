package bing.splitview;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class Road extends PApplet {
	private static final long serialVersionUID = 1L;
	public static String mbTilesString = "blankLight-1-3.mbtiles";

	UnfoldingMap map1;
	UnfoldingMap map2;

	public void setup() {
		size(800, 600, P2D);
		AbstractMapProvider provider = new Microsoft.RoadProvider();
		int zoomLevel = 10;

		map1 = new UnfoldingMap(this, 50, 50, 350, 500, provider);
		map1.zoomAndPanTo(zoomLevel, new Location(47.641340f, -122.117958f));
		MapUtils.createDefaultEventDispatcher(this, map1);

		// Set this to false if you want full control over panning and zooming
		map1.setTweening(true);

		map2 = new UnfoldingMap(this, 400, 50, 350, 500, provider);
		map2.zoomAndPanTo(zoomLevel, new Location(47.641340f, -122.117958f));
		MapUtils.createDefaultEventDispatcher(this, map2);

		// Set this to false if you want full control over panning and zooming
		map2.setTweening(true);
	}

	public void draw() {
		map1.draw();
		map2.draw();
	}

}
