package de.fhpotsdam.unfolding.ui;

import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.mapdisplay.AbstractMapDisplay;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class CompassUI {
	public static final float SIZE_DEFAULT = 75;
	public static final float X_DEFAULT = 100;
	public static final float Y_DEFAULT = 100;
	public static final String IMG_DEFAULT = "data/compass_white.png";

	private PApplet p;
	private AbstractMapDisplay mapDisplay;

	public float x;
	public float y;
	private float size;
	public float scale;
	private PImage img;

	public CompassUI(PApplet p, AbstractMapDisplay mapDisplay, PImage img,
		float x, float y, float size) {
		this.p = p;
		this.mapDisplay = mapDisplay;
		this.size = size;
		this.x = x;
		this.y = y;
		this.img = img;
		scale = 1;
	}

	public CompassUI(PApplet p, AbstractMapDisplay mapDisplay) {
		this(p, mapDisplay, p.loadImage(IMG_DEFAULT), X_DEFAULT, Y_DEFAULT,
				SIZE_DEFAULT);
	}

	public CompassUI(PApplet p, Map map) {
		this(p, map.mapDisplay, p.loadImage(IMG_DEFAULT), X_DEFAULT, Y_DEFAULT,
				SIZE_DEFAULT);
	}
	public void draw() {
		// int zoomLevel = Map.getZoomLevelFromScale(mapDisplay.innerScale);
		// float zoom = Map.getZoomFromScale(mapDisplay.innerScale);
		float dir = mapDisplay.innerAngle;

		if (img != null) {
			p.pushMatrix();
			p.translate(x, y);
			p.rotate(dir);
			p.scale(scale);
			p.image(img, -img.width / 2, -img.height / 2);
			p.popMatrix();
		}
	}
	
	public void setImage(PImage img){
		this.img = img;
	}
}
