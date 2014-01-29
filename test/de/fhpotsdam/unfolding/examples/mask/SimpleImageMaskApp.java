package de.fhpotsdam.unfolding.examples.mask;

import processing.core.PApplet;
import processing.core.PImage;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.mapdisplay.OpenGLMapDisplay;
import de.fhpotsdam.unfolding.mapdisplay.shaders.MaskedMapDisplayShader;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Map is shown inside a fuzzy circle. Uses an gray-scale image as map mask.
 */
public class SimpleImageMaskApp extends PApplet {

	UnfoldingMap map;

	MaskedMapDisplayShader mapDisplayShader;

	public void setup() {
		size(400, 400, OPENGL);
		map = new UnfoldingMap(this);
		MapUtils.createDefaultEventDispatcher(this, map);

		PImage maskImage = loadImage("test/mask-circular.png");
		mapDisplayShader = new MaskedMapDisplayShader(this, 400, 400, maskImage);
		((OpenGLMapDisplay) map.mapDisplay).setMapDisplayShader(mapDisplayShader);
	}

	public void draw() {
		background(0);
		map.draw();
	}

}