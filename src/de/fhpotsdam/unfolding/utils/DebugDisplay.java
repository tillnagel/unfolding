package de.fhpotsdam.unfolding.utils;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PMatrix3D;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.mapdisplay.AbstractMapDisplay;

public class DebugDisplay {

	public static final float WIDTH_DEFAULT = 250;
	public static final float HEIGHT_DEFAULT = 140;

	PApplet p;
	AbstractMapDisplay mapDisplay;

	float x;
	float y;
	float width;
	float height;
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
	public DebugDisplay(PApplet p, AbstractMapDisplay mapDisplay, float x, float y, float width,
			float height) {
		this.p = p;
		this.mapDisplay = mapDisplay;

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		font = p.createFont("Miso-Light-12.vlw", 12);
		p.textFont(font);
	}

	public DebugDisplay(PApplet p, AbstractMapDisplay mapDisplay) {
		this(p, mapDisplay, 10, 10, WIDTH_DEFAULT, HEIGHT_DEFAULT);
	}

	public DebugDisplay(PApplet p, Map map) {
		this(p, map.mapDisplay, 10, 10, WIDTH_DEFAULT, HEIGHT_DEFAULT);
	}

	public void draw() {
		int zoomLevel = Map.getZoomLevelFromScale(mapDisplay.innerScale);
		float zoom = Map.getZoomFromScale(mapDisplay.innerScale);

		String infoText = "Scale: " + mapDisplay.innerScale + "\n";
		infoText += "Zoom: " + zoomLevel + " (" + zoom + ")\n";
		infoText += "tx, ty : " + PApplet.nf((float) mapDisplay.innerOffsetX, 1, 3) + ","
				+ PApplet.nf((float) mapDisplay.innerOffsetY, 1, 3) + "\n";
//		PVector center = mapDisplay.getScreenPosForLocation(mapDisplay.getCenterLocation());
//		infoText += "center (scr, w/2): " + center + "\n";
//		infoText += "center (loc): " + mapDisplay.getLocationForScreenPosition(center.x, center.y) + "\n";
//		
//		PVector itc = mapDisplay.getInnerTransformationCenter();
//
//		infoText += "innerCenter (objPos): " + itc + "\n";
//		infoText += "innerCenter (loc): " + mapDisplay.getLocationForObjectPosition(itc.x, itc.y) + "\n";
//		PVector otc = mapDisplay.getTransformationCenter();
//		infoText += "outerCenter (scrPos): " + otc + "\n";
//		infoText += "outerCenter (loc): " + mapDisplay.getLocationForScreenPosition(otc.x, otc.y) + "\n";
//
//		infoText += "Mouse: " + p.mouseX + ", " + p.mouseY + "\n";
//		infoText += "Mouse (Lat,Lng): " + mapDisplay.getLocationForScreenPosition(p.mouseX, p.mouseY) + "\n";
//		infoText += "Mouse (Coord): "
//				+ mapDisplay.getMapProvider().locationCoordinate(
//						mapDisplay.getLocationForScreenPosition(p.mouseX, p.mouseY)).zoomTo(zoom) + "\n";
		infoText += "fps: " + Math.round(p.frameRate) + "\n";

		p.noStroke();
		p.fill(0, 150);
		p.rect(x, y, width, height);

		p.fill(240);
		p.text(infoText, x + padding, y + padding, width - padding, height - padding);
	}

	public static String getMatrix3DAsString(PMatrix3D m) {
		return PApplet.nfs(m.m00, 2, 2) + "," + PApplet.nfs(m.m01, 2, 2) + "," + PApplet.nfs(m.m02, 2, 2) + ","
				+ PApplet.nfs(m.m03, 2, 2) + "\n" + PApplet.nfs(m.m10, 2, 2) + "," + PApplet.nfs(m.m11, 2, 2) + ","
				+ PApplet.nfs(m.m12, 2, 2) + "," + PApplet.nfs(m.m13, 2, 2) + "\n" + PApplet.nfs(m.m20, 2, 2) + ","
				+ PApplet.nfs(m.m21, 2, 2) + "," + PApplet.nfs(m.m22, 2, 2) + "," + PApplet.nfs(m.m33, 2, 2) + "\n"
				+ PApplet.nfs(m.m30, 2, 2) + "," + PApplet.nfs(m.m31, 2, 2) + "," + PApplet.nfs(m.m32, 2, 2) + ","
				+ PApplet.nfs(m.m33, 2, 2) + "\n";
	}

}
