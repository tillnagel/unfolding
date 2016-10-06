package de.fhpotsdam.unfolding.examples.multi;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.examples.provider.dynamic.DynamicMapSwitch;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.ImmoScout;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * A two layer map. One map lays atop another one with same size and position.
 */
public class MultiProviderOverlayMapApp extends PApplet {

	private UnfoldingMap map1;
	private UnfoldingMap map2;
	
	@Override
	public void settings() {
		size(800, 600, P2D);
	}
	
	@Override
	public void setup() {
		final Location berlinLocation = new Location(52.439046f, 13.447266f);
		map1 = new UnfoldingMap(this, "map1", new Microsoft.RoadProvider());
		map1.zoomAndPanTo(11, berlinLocation);

		map2 = new UnfoldingMap(this, "map2", new ImmoScout.HeatMapProvider());
		map2.zoomAndPanTo(11, berlinLocation);

		MapUtils.createDefaultEventDispatcher(this, map1, map2);
	}

	@Override
	public void draw() {
		background(0);
		map1.draw();
		tint(255, 100);
		map2.draw();
	}
	
	public static void main(String[] args) {
		PApplet.main(new String[] { MultiProviderOverlayMapApp.class.getName() });
	}

}
