package de.fhpotsdam.unfolding.examples.mask;

import processing.core.PApplet;
import processing.core.PGraphics;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.mapdisplay.MaskedOpenGLMapDisplay;
import de.fhpotsdam.unfolding.mapdisplay.OpenGLMapDisplay;
import de.fhpotsdam.unfolding.mapdisplay.shaders.MaskedMapDisplayShader;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * This example shows the use of an gray-scale mask applied to the map.
 */
public class SimpleMaskApp extends PApplet {

	UnfoldingMap map;

	PGraphics mask;
	MaskedMapDisplayShader mapDisplayShader;

	public void setup() {
		size(830, 420, OPENGL);
		map = new UnfoldingMap(this, "map1", 10, 10, 400, 400, true, false, null);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		mapDisplayShader = new MaskedMapDisplayShader(this,400,400); 
		((OpenGLMapDisplay)map.mapDisplay).setMapDisplayShader(mapDisplayShader);
		
		mask = mapDisplayShader.getMask();
		mask.beginDraw();
		mask.background(0);
		mask.endDraw();
	}

	public void draw() {
		background(0);

		updateMask();
		map.draw();

		// shows the mask next to the map
		image(mask, 420, 10);
	}

	// draw the grayscale mask on an mask object
	// 255 = invisible
	// 0 = visible
	public void updateMask() {
		mask.beginDraw();
		  if (mouseX != 0 && mouseY != 0) {  
			  mask.noStroke();
			  mask.fill(255, 255,255);
			  mask.ellipse(mouseX, mouseY, 50, 50);
		  }
		mask.ellipse(mouseX, mouseY, 100, 100);
		mask.endDraw();
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.ui.SimpleMaskApp" });
	}

}