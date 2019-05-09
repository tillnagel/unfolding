package de.fhpotsdam.unfolding.examples.mask;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.mapdisplay.OpenGLMapDisplay;
import de.fhpotsdam.unfolding.mapdisplay.shaders.MaskedMapDisplayShader;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * This example shows the use of dynamically generated mask applied to the map.
 * 
 * Move the mouse cursor over the left canvas area. Press any key to reset mask.
 */
public class DynamicMaskApp extends PApplet {

	UnfoldingMap map;

	PGraphics mask;
	MaskedMapDisplayShader mapDisplayShader;

	public void settings() {
		size(830, 420, P3D);
	}

	public void setup() {
		map = new UnfoldingMap(this, "map1", 10, 10, 400, 400, true, false, null);
		MapUtils.createDefaultEventDispatcher(this, map);

		mapDisplayShader = new MaskedMapDisplayShader(this, 400, 400);
		((OpenGLMapDisplay) map.mapDisplay).setMapDisplayShader(mapDisplayShader);

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

	public void keyPressed() {
		resetMask();
	}

	// draw the grayscale mask on an mask object
	// 255 = invisible
	// 0 = visible
	public void updateMask() {
		mask.beginDraw();
		if (mouseX != 0 && mouseY != 0) {
			mask.noStroke();
			mask.fill(255, 127);
			mask.ellipse(mouseX, mouseY, 50, 50);
		}
		mask.endDraw();
	}

	public void resetMask() {
		mask.beginDraw();
		mask.clear();
		mask.endDraw();
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { DynamicMaskApp.class.getName() });
	}

}