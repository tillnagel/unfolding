package de.fhpotsdam.unfolding.examples.mask;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.mapdisplay.OpenGLMapDisplay;
import de.fhpotsdam.unfolding.mapdisplay.shaders.BlurredMapDisplayShader;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Map is shown inside a fuzzy circle. Uses an gray-scale image as map mask.
 */
public class SimpleBlurApp extends PApplet {

	UnfoldingMap map;

	BlurredMapDisplayShader mapDisplayShader;

	public void setup() {
		size(800, 600, OPENGL);
		map = new UnfoldingMap(this, 100, 100, 600, 400);
		MapUtils.createDefaultEventDispatcher(this, map);

		mapDisplayShader = new BlurredMapDisplayShader(this);
		((OpenGLMapDisplay) map.mapDisplay).setMapDisplayShader(mapDisplayShader);
	}

	public void draw() {
		background(0);
		map.draw();
	}

}