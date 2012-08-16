package de.fhpotsdam.unfolding.utils;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PMatrix3D;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;

public class DebugDisplay {

	public static final float WIDTH_DEFAULT = 200;
	public static final float HEIGHT_DEFAULT = 190;

	PApplet p;
	UnfoldingMap map;

	float x;
	float y;
	float width;
	float height;
	float padding = 4;
	float margin = 14;

	PFont font;
	PFont titleFont;
	int textColor = 0;
	int valueBoxColor = 0;
	int separatorColor = 0;

	int textSize = 12;
	int valueBoxLongWidth = 120;
	float valueBoxMediumWidth = (valueBoxLongWidth - padding) / 2;
	int valueBoxShortWidth = 30;
	int valueBoxHeight = 15;

	PImage logo;
	
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

		font = p.loadFont("Lato-Regular-11.vlw");
		titleFont = p.loadFont("Lato-Bold-14.vlw");
		
		logo = p.loadImage("unfolding-mini-icon.png");

		textColor = p.color(255);
		valueBoxColor = p.color(0, 127);
		separatorColor = p.color(255, 50);
	}

	public DebugDisplay(PApplet p, UnfoldingMap map, float x, float y) {
		this(p, map, x, y, WIDTH_DEFAULT, HEIGHT_DEFAULT);
	}

	public DebugDisplay(PApplet p, UnfoldingMap map) {
		this(p, map, 10, 10, WIDTH_DEFAULT, HEIGHT_DEFAULT);
	}

	public void draw() {

		p.noStroke();
		p.fill(20, 180);
		p.rect(x, y, width, height);
		
		p.image(logo, x + margin, y + margin);

		p.textFont(titleFont);
		p.textSize(14);
		p.fill(textColor);
		String mapName = map.getId();
		p.text(mapName, (int) (x + margin + logo.width + padding * 2) - 2, (int) (y + margin + logo.height - padding) + 1);
		
		
		p.textFont(font);
		p.textSize(11);
		String zoomStr = String.valueOf(map.getZoomLevel());
		String mouseXStr = String.valueOf(p.mouseX);
		String mouseYStr = String.valueOf(p.mouseY);
		Location mouseLoc = map.getLocation(p.mouseX, p.mouseY);
		String mouseLatStr = PApplet.nf(mouseLoc.getLat(), 1, 3);
		String mouseLngStr = PApplet.nf(mouseLoc.getLon(), 1, 3);
		
		
		String rendererFQNStr = p.g.getClass().toString();
		String rendererStr = rendererFQNStr.substring(rendererFQNStr.lastIndexOf('.') + 1);
		
		String fpsStr = String.valueOf(PApplet.round(p.frameRate));
		
		String providerFQNStr = map.mapDisplay.getMapProvider().getClass().toString();
		String providerStr = providerFQNStr.substring(providerFQNStr.lastIndexOf('$') + 1);
		

		float yo = y + 45;
		drawLabelValue("Zoom", zoomStr, x + 60, yo, valueBoxShortWidth);

		yo += valueBoxHeight + padding * 2;
		drawSeparator(yo);
		yo += padding * 2;

		drawLabelValue("px", mouseXStr, x + 60, yo, valueBoxMediumWidth);
		drawValue(mouseYStr, x + 60 + valueBoxMediumWidth + padding, yo, valueBoxMediumWidth);
		yo += valueBoxHeight + padding;
		drawLabelValue("¡", mouseLatStr, x + 60, yo, valueBoxMediumWidth);
		drawValue(mouseLngStr, x + 60 + valueBoxMediumWidth + padding, yo, valueBoxMediumWidth);

		yo += valueBoxHeight + padding * 2;
		drawSeparator(yo);
		yo += padding * 2;

		drawLabelValue("Renderer", rendererStr, x + 60, yo, valueBoxLongWidth);
		yo += valueBoxHeight + padding;
		drawLabelValue("Provider", providerStr, x + 60, yo, valueBoxLongWidth);
		yo += valueBoxHeight + padding;
		drawLabelValue("fps", fpsStr, x + 60, yo, valueBoxShortWidth);
	}

	
	public void drawLabelValue(String label, String value, float x, float y, float valueBoxWidth) {

		drawValue(value, x, y, valueBoxWidth);

		// label
		float textY = y + textSize - 1;
		float labelX = x - padding - p.textWidth(label);
		p.noStroke();
		p.fill(textColor);
		p.text(label, labelX, textY);
	}

	public void drawValue(String value, float x, float y, float valueBoxWidth) {
		p.noStroke();

		// value box
		float valueBoxX = x + padding;
		float valueBoxY = y;
		p.fill(valueBoxColor);
		p.rect(valueBoxX, valueBoxY, valueBoxWidth, valueBoxHeight);

		// value (atop box)
		float textY = y + textSize - 1;
		float valueX = x + padding * 2;
		p.fill(textColor);
		p.text(value, valueX, textY);
	}

	public void drawZoom(float x, float y) {
		String label = "Zoom";
		float labelX = x - padding - p.textWidth(label);
		p.fill(textColor);
		p.text(label, labelX, y);

		p.fill(valueBoxColor);
		// p.rect(valueX, y, boxWidth, boxHeight);

	}

	public void drawSeparator(float y) {
		p.fill(separatorColor);
		p.rect(x + margin, y, width - margin * 2, 1);
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
