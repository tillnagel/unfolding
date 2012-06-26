package de.fhpotsdam.unfolding.examples.ui;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import codeanticode.glgraphics.GLGraphicsOffScreen;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.mapdisplay.MaskedGLGraphicsMapDisplay;
import de.fhpotsdam.unfolding.ui.*;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * 
 * @author Christopher Pietsch
 * 
 *         This example shows the use of an grayscale mask applied to the map.
 */
public class SimpleMaskApp extends PApplet {

	UnfoldingMap map;

	GLGraphicsOffScreen mask;

	public void setup() {
		size(830, 420, GLConstants.GLGRAPHICS);
		map = new UnfoldingMap(this, "map1", 10, 10, 400, 400, true, false, null);
		MapUtils.createDefaultEventDispatcher(this, map);

		mask = map.mapDisplay.getMask();
	}

	public void draw() {
		background(0);

		updateMask();
		map.draw();

		// shows the mask next to the map
		image(mask.getTexture(), 420, 10);
	}
	
	// draw the grayscale mask on an mask object
	// 255 = invisible
	// 0 = visible
	public void updateMask() {
		mask.beginDraw();
		mask.background(255);
		mask.noStroke();
		mask.fill(100);
		mask.ellipse(mouseX, mouseY, 122, 122);
		mask.fill(0);
		mask.ellipse(mouseX, mouseY, 100, 100);
		mask.endDraw();
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.ui.SimpleMaskApp" });
	}

}