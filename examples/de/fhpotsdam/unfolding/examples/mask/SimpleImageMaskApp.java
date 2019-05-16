package de.fhpotsdam.unfolding.examples.mask;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.mapdisplay.OpenGLMapDisplay;
import de.fhpotsdam.unfolding.mapdisplay.shaders.MaskedMapDisplayShader;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Map is shown with a mask. Uses an gray-scale image as map mask, to show map inside a fuzzy circle. 
 */
public class SimpleImageMaskApp extends PApplet {

	UnfoldingMap map;

	MaskedMapDisplayShader mapDisplayShader;

	public void settings() {
		size(800, 800, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this);
		MapUtils.createDefaultEventDispatcher(this, map);

		PImage maskImage = loadImage("shader/mask-circular.png");
		mapDisplayShader = new MaskedMapDisplayShader(this, 400, 400, maskImage);
		((OpenGLMapDisplay) map.mapDisplay).setMapDisplayShader(mapDisplayShader);
	}

	public void draw() {
		background(0);
		map.draw();
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { SimpleImageMaskApp.class.getName() });
	}

}