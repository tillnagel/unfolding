package de.fhpotsdam.unfolding.utils;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PMatrix3D;
import de.fhpotsdam.unfolding.UnfoldingMap;

public class DebugDisplay {

	public static final float WIDTH_DEFAULT = 250;
	public static final float HEIGHT_DEFAULT = 140;

	PApplet p;
	UnfoldingMap map;

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
	 * @param map
	 *            The map to display debug information about.
	 * @param x
	 *            Horizontal position of the display.
	 * @param y
	 *            Vertical position of the display.
	 * @param width
	 *            Width of the display. Text is auto-newline.
	 * @param height
	 *            Height of the display.
	 */
	public DebugDisplay(PApplet p, UnfoldingMap map, float x, float y, float width, float height) {
		this.p = p;
		this.map = map;

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		font = p.createFont("Helvetica-12.vlw", 12);
		p.textFont(font);
	}

	public DebugDisplay(PApplet p, UnfoldingMap map) {
		this(p, map, 10, 10, WIDTH_DEFAULT, HEIGHT_DEFAULT);
	}

	public void draw() {

		StringBuffer infoText = new StringBuffer();
		infoText.append("Map-Zoom: " + map.getZoomLevel() + " (" + map.getZoom() + ")\n");
		infoText.append("Map-Borders (lat, lng): \n[" + map.getTopLeftBorder() + ", " + map.getBottomRightBorder()
				+ "]\n\n");
		
		infoText.append("Mouse: " + p.mouseX + ", " + p.mouseY + "\n");
		infoText.append("Mouse (lat, lng): " + map.getLocation(p.mouseX, p.mouseY) + "\n");

		infoText.append("fps: " + Math.round(p.frameRate) + "\n");
		
		infoText.append(getInternalDebugInfo());
		
		p.noStroke();
		p.fill(0, 150);
		p.rect(x, y, width, height);
		p.fill(240);
		p.text(infoText.toString(), x + padding, y + padding, width - padding, height - padding);
	}
	
	protected String getInternalDebugInfo() {
		String internalInfoText = "";
		
		// infoText += "tx, ty : " + PApplet.nf((float) mapDisplay.innerOffsetX, 1, 3) + ","
		// + PApplet.nf((float) mapDisplay.innerOffsetY, 1, 3) + "\n";

		// PVector center = mapDisplay.getScreenPosForLocation(mapDisplay.getCenterLocation());
		// infoText += "center (scr, w/2): " + center + "\n";
		// infoText += "center (loc): " + mapDisplay.getLocation(center.x, center.y) + "\n";

		// PVector itc = mapDisplay.getInnerTransformationCenter();
		// infoText += "innerCenter (objPos): " + itc + "\n";
		// infoText += "innerCenter (loc): " + mapDisplay.getLocationForObjectPosition(itc.x, itc.y) + "\n";
		// PVector otc = mapDisplay.getTransformationCenter();
		// infoText += "outerCenter (scrPos): " + otc + "\n";
		// infoText += "outerCenter (loc): " + mapDisplay.getLocationForScreenPosition(otc.x, otc.y) + "\n";

		// infoText += "Mouse (Coord): "
		// + mapDisplay.getMapProvider()
		// .locationCoordinate(mapDisplay.getLocation(p.mouseX, p.mouseY)).zoomTo(zoom)
		// + "\n";

		return internalInfoText;
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
