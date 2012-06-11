package de.fhpotsdam.unfolding.examples.ui;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import codeanticode.glgraphics.GLGraphicsOffScreen;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.ui.*;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class SimpleMaskApp extends PApplet {

	Map map;
	MaskUI mask;
	
	GLGraphicsOffScreen canvas;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		map = new Map(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		mask = new MaskUI(this);
		
		canvas = new GLGraphicsOffScreen(this, this.width, this.height);

		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		// update the mask
		updateMask();
		background(0,255,0);
		//map.draw();
		
		mask.draw();
		
	}
	
	public void updateMask() {
		canvas.beginDraw();
		canvas.background(140);
		canvas.fill(255);
		canvas.ellipse(mouseX, mouseY, 100, 100);
		canvas.endDraw();
		
		// put the canvas into the texture
		mask.setTexture(canvas.getTexture());
	}

	public void keyPressed() {
		if (key == '+') map.zoomIn();
		if (key == '-') map.zoomOut();
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.ui.SimpleMaskApp" });
	}

}