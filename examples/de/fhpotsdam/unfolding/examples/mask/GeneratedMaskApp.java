package de.fhpotsdam.unfolding.examples.mask;

import processing.core.PApplet;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.mapdisplay.OpenGLMapDisplay;
import de.fhpotsdam.unfolding.mapdisplay.shaders.MapDisplayShader;
import de.fhpotsdam.unfolding.mapdisplay.shaders.MaskedMapDisplayShader;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Shows how to draw on an offscreen canvas and use this as a map mask.
 */
public class GeneratedMaskApp extends PApplet {

	UnfoldingMap map;
	MapDisplayShader mapDisplayShader;

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this);
		map.zoomAndPanTo(10, new Location(52.5f, 13.4f));
		MapUtils.createDefaultEventDispatcher(this, map);
		
		createDynamicMask();
	}

	public void draw() {
		background(240);
		map.draw();
	}

	public void createDynamicMask() {
		PGraphics maskImage = createGraphics(800, 600);
		maskImage.beginDraw();

		maskImage.noFill();
		maskImage.strokeJoin(ROUND);

		maskImage.stroke(255, 220);
		maskImage.strokeWeight(90);
		maskImage.beginShape();
		maskImage.vertex(100, 100);
		maskImage.vertex(100, 200);
		maskImage.vertex(400, 300);
		maskImage.vertex(300, 200);
		maskImage.endShape();

		maskImage.stroke(255);
		maskImage.strokeWeight(70);
		maskImage.beginShape();
		maskImage.vertex(100, 100);
		maskImage.vertex(100, 200);
		maskImage.vertex(400, 300);
		maskImage.vertex(300, 200);
		maskImage.endShape();

		maskImage.endDraw();

		mapDisplayShader = new MaskedMapDisplayShader(this, 800, 600, maskImage);
		((OpenGLMapDisplay) map.mapDisplay).setMapDisplayShader(mapDisplayShader);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { GeneratedMaskApp.class.getName() });
	}
}
