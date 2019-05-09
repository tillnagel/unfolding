package de.fhpotsdam.unfolding.examples.mask;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.mapdisplay.OpenGLMapDisplay;
import de.fhpotsdam.unfolding.mapdisplay.shaders.BlurredMapDisplayShader;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

/**
 * Map is used with a (non-mask) shader. This blurs the map.
 */
public class SimpleBlurApp extends PApplet {

	UnfoldingMap map;

	BlurredMapDisplayShader mapDisplayShader;

	public void settings() {
		size(800, 600, P3D);
	}

	public void setup() {
		size(800, 600, P3D);
		map = new UnfoldingMap(this, 100, 100, 600, 400);
		MapUtils.createDefaultEventDispatcher(this, map);

		mapDisplayShader = new BlurredMapDisplayShader(this);
		((OpenGLMapDisplay) map.mapDisplay).setMapDisplayShader(mapDisplayShader);
	}

	public void draw() {
		background(0);
		map.draw();
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { SimpleBlurApp.class.getName() });
	}

}