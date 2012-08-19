package de.fhpotsdam.unfolding.ui;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.mapdisplay.AbstractMapDisplay;

public class ZoomLevelUI {
	public static final float X_DEFAULT = 100;
	public static final float Y_DEFAULT = 100;


	private PApplet p;
	private AbstractMapDisplay mapDisplay;

	public float x;
	public float y;
	public float scale;

	public ZoomLevelUI(PApplet p, AbstractMapDisplay mapDisplay,
			float x, float y) {
		this.p = p;
		this.mapDisplay = mapDisplay;
		this.x = x;
		this.y = y;
		scale = 1;
	}
	public ZoomLevelUI(PApplet p, UnfoldingMap map, CompassUI compass) {
		this(p, map.mapDisplay, compass.getScreenPosition().x, compass.getScreenPosition().y);
	}

	public ZoomLevelUI(PApplet p, UnfoldingMap map) {
		this(p, map.mapDisplay, X_DEFAULT, Y_DEFAULT);
		//
	}
	public void draw() {
		// int zoomLevel = Map.getZoomLevelFromScale(mapDisplay.innerScale);
		// float zoom = Map.getZoomFromScale(mapDisplay.innerScale);
		float dir = mapDisplay.innerAngle;

		/*if (img != null) {
			p.pushMatrix();
			p.translate(x, y);
			p.rotate(dir);
			p.scale(scale);
			p.image(img, -img.width / 2, -img.height / 2);
			p.popMatrix();
		}*/
	}

}
