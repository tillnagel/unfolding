package de.fhpotsdam.unfolding.ui;

import processing.core.PApplet;
import processing.core.PImage;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * A simple compass which shows the cardinal directions of the map.
 */
public class CompassUI {

	private static final ScreenPosition DEFAULT_POS = new ScreenPosition(100, 100);
	private static final String IMG_DEFAULT = "ui/compass_white.png";

	private PApplet p;
	private UnfoldingMap map;

	private ScreenPosition pos;

	private float scale;
	private PImage img;

	/**
	 * Create CompassUI.
	 * 
	 * @param p
	 *            The main PApplet.
	 * @param map
	 *            The map to show compass for.
	 * @param img
	 *            The image for the compass to show.
	 * @param x
	 *            x position on the screen.
	 * @param y
	 *            y position on the screen.
	 */
	public CompassUI(PApplet p, UnfoldingMap map, PImage img, float x, float y) {
		this.p = p;
		this.map = map;
		this.pos = new ScreenPosition(x, y);
		this.img = img;
		scale = 1;
	}

	/**
	 * Create default CompassUI at default position.
	 * 
	 * @param p
	 *            The main PApplet.
	 * @param map
	 *            The map to show compass for.
	 */
	public CompassUI(PApplet p, UnfoldingMap map) {
		this(p, map, p.loadImage(IMG_DEFAULT), DEFAULT_POS.x, DEFAULT_POS.y);
	}

	/**
	 * Draw the compass.
	 */
	public void draw() {
		float angle = map.mapDisplay.innerAngle;

		if (img != null) {
			p.pushMatrix();
			p.translate(pos.x, pos.y);
			p.rotate(angle);
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
