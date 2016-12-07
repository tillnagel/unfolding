package de.fhpotsdam.unfolding.examples.multi;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.CartoDB.DarkMatter;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.utils.Integrator;

/**
 * Press'd' and 'n' to animate between day and night!
 */
public class DayNightTransitionApp extends PApplet {

	UnfoldingMap mapDay;
	UnfoldingMap mapNight;

	Integrator blendIntegrator = new Integrator(0);

	public void settings() {
		size(800, 600, P2D);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { DayNightTransitionApp.class.getName() });
	}

	public void setup() {

		mapDay = new UnfoldingMap(this);
		mapNight = new UnfoldingMap(this, new DarkMatter());

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
