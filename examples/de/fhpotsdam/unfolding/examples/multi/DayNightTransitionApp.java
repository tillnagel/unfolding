package de.fhpotsdam.unfolding.examples.multi;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.utils.Integrator;

/**
 * Press'd' and 'n' to animate between day and night!
 */
public class DayNightTransitionApp extends PApplet {

	public static final String JDBC_CONN_STRING_APPLET = "jdbc:sqlite:../data/tiles/blankDark-1-3.mbtiles";

	UnfoldingMap mapDay;
	UnfoldingMap mapNight;

	Integrator blendIntegrator = new Integrator(0);

	public void setup() {
		size(800, 600, OPENGL);

		mapDay = new UnfoldingMap(this);
		mapNight = new UnfoldingMap(this, new MBTilesMapProvider(JDBC_CONN_STRING_APPLET));

		mapDay.setZoomRange(1, 3);
		mapDay.zoomToLevel(3);
		mapDay.panTo(new Location(49.6f, 9.4f));
		mapNight.setZoomRange(1, 3);
		mapNight.zoomToLevel(3);
		mapNight.panTo(new Location(49.6f, 9.4f));

		MapUtils.createDefaultEventDispatcher(this, mapDay, mapNight);
	}

	public void draw() {
		background(0);

		blendIntegrator.update();

		tint(255, 255);
		mapDay.draw();
		tint(255, blendIntegrator.value);
		mapNight.draw();
	}

	public void keyPressed() {
		if (key == 'd') {
			blendIntegrator.target(0);
		}
		if (key == 'n') {
			blendIntegrator.target(255);
		}
	}
}
