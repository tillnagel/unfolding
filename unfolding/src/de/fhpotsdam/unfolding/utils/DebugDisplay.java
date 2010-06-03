package de.fhpotsdam.unfolding.utils;

import processing.core.PApplet;
import processing.core.PFont;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.mapdisplay.AbstractMapDisplay;

public class DebugDisplay {

	PApplet p;
	AbstractMapDisplay map;

	float x;
	float y;
	float width = 250;
	float height = 100;
	float padding = 4;

	PFont font;

	/**
	 * Shows current information on the mapDisplay and the mouse pointer.
	 * 
	 * @param p
	 *            The PApplet, used to draw and to get mouse position.
	 * @param mapDisplay
	 *            The mapDisplay
	 * @param x
	 *            Horizontal position of the display.
	 * @param y
	 *            Vertical position of the display.
	 * @param width
	 *            Width of the display. Text is auto-newline.
	 * @param height
	 *            Height of the display.
	 */
	public DebugDisplay(PApplet p, AbstractMapDisplay map, float x, float y, float width,
			float height) {
		this.p = p;
		this.map = map;

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		font = p.createFont("Miso-Light-12.vlw", 12);
		p.textFont(font);
	}

	public void draw() {
		int zoomLevel = Map.getZoomLevelFromScale(map.sc);
		float zoom = Map.getZoomFromScale(map.sc);

		String infoText = "Scale: " + map.sc + "\n";
		infoText += "Zoom: " + zoomLevel + " (" + zoom + ")\n";
		infoText += "tx: " + PApplet.nf((float) map.tx, 1, 3) + "\n";
		infoText += "ty: " + PApplet.nf((float) map.ty, 1, 3) + "\n";
		// infoText += "CenterCoord: " + mapDisplay.getCenterCoordinate() + "\n";
		infoText += "Mouse: " + p.mouseX + ", " + p.mouseY + "\n";
		infoText += "Mouse (Lat,Lng): " + map.pointLocation(p.mouseX, p.mouseY) + "\n";
		infoText += "Mouse (Coord): "
				+ map.getMapProvider().locationCoordinate(map.pointLocation(p.mouseX, p.mouseY))
						.zoomTo(zoom) + "\n";
		infoText += "fps: " + Math.round(p.frameRate) + "\n";

		p.noStroke();
		p.fill(0, 150);
		p.rect(x, y, width, height);

		p.fill(240);
		p.text(infoText, x + padding, y + padding, width - padding, height - padding);
	}

}
