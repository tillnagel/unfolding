package de.fhpotsdam.unfolding.ui;

import processing.core.PApplet;
import processing.core.PImage;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.mapdisplay.AbstractMapDisplay;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * A simple compass which shows the cardinal directions of the map.
 */
public class CompassUI {
	private static final ScreenPosition DEFAULT_POS = new ScreenPosition(100, 100);
	private static final String IMG_DEFAULT = "compass_white.png";

	private PApplet p;
	private AbstractMapDisplay mapDisplay;

	private ScreenPosition pos;

	private float scale;
	private PImage img;

	/**
	 * Create CompassUI.
	 * @param p the applet to draw on.
	 * @param mapDisplay the map to draw on.
	 * @param img the image for the compass to show.
	 * @param x x position on the screen.
	 * @param y y position on the screen.
	 */
	public CompassUI(PApplet p, AbstractMapDisplay mapDisplay, PImage img, float x, float y) {
		this.p = p;
		this.mapDisplay = mapDisplay;
		this.pos = new ScreenPosition(x, y);
		this.img = img;
		scale = 1;
	}

	/**
	 * Create default CompassUI at default position.
	 * @param p the applet to draw on.
	 * @param mapDisplay the map to draw on.
	 */
	public CompassUI(PApplet p, AbstractMapDisplay mapDisplay) {
		this(p, mapDisplay, p.loadImage(IMG_DEFAULT), DEFAULT_POS.x, DEFAULT_POS.y);
	}
	/**
	 * Create default CompassUI at default position.
	 * @param p the applet to draw on.
	 * @param map the map to draw on.
	 */
	public CompassUI(PApplet p, UnfoldingMap map) {
		this(p, map.mapDisplay, p.loadImage(IMG_DEFAULT), DEFAULT_POS.x, DEFAULT_POS.y);
	}

	/**
	 * Draw the compass.
	 */
	public void draw() {
		// int zoomLevel = Map.getZoomLevelFromScale(mapDisplay.innerScale);
		// float zoom = Map.getZoomFromScale(mapDisplay.innerScale);
		float dir = mapDisplay.innerAngle;

		if (img != null) {
			p.pushMatrix();
			p.translate(pos.x, pos.y);
			p.rotate(dir);
			p.scale(scale);
			p.image(img, -img.width / 2, -img.height / 2);
			p.popMatrix();
		}
	}
	
	public ScreenPosition getScreenPosition() {
		return pos;
	}

	public void setImage(PImage img) {
		this.img = img;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
}
