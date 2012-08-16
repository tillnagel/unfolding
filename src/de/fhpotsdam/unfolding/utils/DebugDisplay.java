package de.fhpotsdam.unfolding.utils;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.events.MapEvent;
import de.fhpotsdam.unfolding.events.MapEventListener;
import de.fhpotsdam.unfolding.events.PanMapEvent;
import de.fhpotsdam.unfolding.events.ZoomMapEvent;
import de.fhpotsdam.unfolding.geo.Location;

public class DebugDisplay implements MapEventListener {

	public static final float WIDTH_DEFAULT = 200;
	public static final float HEIGHT_DEFAULT = 240;

	PApplet p;
	UnfoldingMap map;
	EventDispatcher eventDispatcher;

	float x;
	float y;
	float width = WIDTH_DEFAULT;
	float height = HEIGHT_DEFAULT;
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
	int eventBoxHeight = 12;

	PImage logo;

	int backgroundColor = 0xF0222222;
	
	int eventBoxColorListeningOn = 0xFF5bdae7;
	int eventBoxColorListeningOff = 0x335bdae7;
	int eventBoxColorBroadcastingOn = 0xFFfc8720;
	int eventBoxColorBroadcastingOff = 0x33fc8720;

	float panByListened = 0.3f;
	float panToListened = 0.3f;
	float zoomByListened = 0.3f;

	float panByBroadcasted = 0.3f;
	float panToBroadcasted = 0.3f;
	float zoomByBroadcasted = 0.3f;
	
	int zoomStepColorOn = 0xFFFFFFFF;
	int zoomStepColorOff = 0xFF666666;
	
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
	 */
	public DebugDisplay(PApplet p, UnfoldingMap map, EventDispatcher eventDispatcher, float x, float y) {
		this.p = p;
		this.map = map;
		this.eventDispatcher = eventDispatcher;
		if (eventDispatcher != null) {
			// eventDispatcher.register(this, "pan", map.getId());
			// eventDispatcher.register(this, "zoom", map.getId());

			// TODO Register to all scopes, instead of using hard-coded strings
			eventDispatcher.register(this, "pan", "map1", "map2");
			eventDispatcher.register(this, "zoom", "map1", "map2");
		}

		this.x = x;
		this.y = y;

		font = p.loadFont("Lato-Regular-11.vlw");
		titleFont = p.loadFont("Lato-Bold-14.vlw");

		logo = p.loadImage("unfolding-mini-icon.png");

		textColor = p.color(255);
		valueBoxColor = p.color(0, 127);
		separatorColor = p.color(255, 50);
	}

	public DebugDisplay(PApplet p, UnfoldingMap map) {
		this(p, map, 10, 10);
	}

	public DebugDisplay(PApplet p, UnfoldingMap map, int x, int y) {
		this(p, map, null, x, y);
	}

	// ----------------------------------------------------

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onManipulation(MapEvent mapEvent) {
		PApplet.println("Received at DebugDisplay for " + map.getId() + ": " + mapEvent.getType() + " with subtype "
				+ mapEvent.getSubType() + " in scope " + mapEvent.getScopeId());

		if (map.getId().equals(mapEvent.getScopeId())) {
			if (mapEvent.getSubType().equals(PanMapEvent.PAN_BY)) {
				panByListened = 1;
			}
			if (mapEvent.getSubType().equals(PanMapEvent.PAN_TO)) {
				panToListened = 1;
			}
			if (mapEvent.getSubType().equals(ZoomMapEvent.ZOOM_BY_LEVEL)) {
				zoomByListened = 1;
			}
		} else {

			// TODO Do not listen to all, but only to the one registered to in EventDispatcher

			if (mapEvent.getSubType().equals(PanMapEvent.PAN_BY)) {
				panByBroadcasted = 1;
			}
			if (mapEvent.getSubType().equals(PanMapEvent.PAN_TO)) {
				panToBroadcasted = 1;
			}
			if (mapEvent.getSubType().equals(ZoomMapEvent.ZOOM_BY_LEVEL)) {
				zoomByBroadcasted = 1;
			}

		}

	}

	// ----------------------------------------------------

	public void draw() {

		p.noStroke();
		p.fill(backgroundColor);
		p.rect(x, y, width, height);

		p.image(logo, x + margin, y + margin);

		p.textFont(titleFont);
		p.textSize(14);
		p.fill(textColor);
		String mapName = map.getId();
		p.text(mapName, (int) (x + margin + logo.width + padding * 2) - 2,
				(int) (y + margin + logo.height - padding) + 1);

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

		drawZoomBar(map.getZoomLevel(), x + 107, yo + 5);
		
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

		if (eventDispatcher != null) {

			// eventDispatcher.typedScopedListeners

			yo += valueBoxHeight + padding * 2;
			drawSeparator(yo);
			yo += padding * 2;

			// background box around all events
			p.fill(valueBoxColor);
			p.rect(x + margin, yo, width - margin * 2, 24 + padding * 2);

			// Math.random() > 0.5, Math.random() > 0.5
			float xEventStart = x + 80;
			yo += padding;
			drawLabelEvent("Pan By", panByListened, panByBroadcasted, xEventStart, yo, 3);
			drawLabelEvent("Pan To", panToListened, panToBroadcasted, xEventStart + 70, yo, 3);
			yo += eventBoxHeight;
			drawLabelEvent("Zoom By", zoomByListened, zoomByBroadcasted, xEventStart, yo, 3);
			drawLabelEvent("Zoom To", 1, 1, xEventStart + 70, yo, 3);

			if (panByListened > 0.3) {
				panByListened -= 0.05;
			}
			if (panToListened > 0.3) {
				panToListened -= 0.05;
			}
			if (zoomByListened > 0.3) {
				zoomByListened -= 0.05;
			}
			if (panByBroadcasted > 0.3) {
				panByBroadcasted -= 0.05;
			}
			if (panToBroadcasted > 0.3) {
				panToBroadcasted -= 0.05;
			}
			if (zoomByBroadcasted > 0.3) {
				zoomByBroadcasted -= 0.05;
			}
		}

	}

	public void drawLabelEvent(String label, float listeningValue, float broadcastingValue, float x, float y,
			float valueBoxWidth) {

		// drawValue(value, x, y, valueBoxWidth);
		int alphaL = (int) PApplet.map(listeningValue, 0, 1, 0, 255);
		int alphaB = (int) PApplet.map(broadcastingValue, 0, 1, 0, 255);
		drawEvent(x, y + 4, valueBoxWidth, p.color(eventBoxColorListeningOn, alphaL));
		drawEvent(x + 6, y + 4, valueBoxWidth, p.color(eventBoxColorBroadcastingOn, alphaB));

		// label
		p.textFont(font);
		p.textSize(8);
		float textY = y + textSize - 3;
		float labelX = x - padding - p.textWidth(label.toUpperCase());
		p.noStroke();
		p.fill(textColor);
		p.text(label.toUpperCase(), labelX, textY);
	}

	private void drawEvent(float x, float y, float valueBoxSize, int color) {
		// value box
		float valueBoxX = x + padding;
		float valueBoxY = y;
		p.fill(color);
		p.rect(valueBoxX, valueBoxY, valueBoxSize, valueBoxSize);

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

	public void drawZoomBar(int zoomLevel, float x, float y) {
		int zoomBarWidth = 100;
		int maxZoomLevel = 16;
		int stepWidth = (zoomBarWidth / (maxZoomLevel + 1));
		for (int i = 0; i < maxZoomLevel; i++) {
			p.fill(i < zoomLevel ? zoomStepColorOn : zoomStepColorOff);
			p.rect(x + i * stepWidth, y, stepWidth - 1, 6);
		}
	}

	public void drawSeparator(float y) {
		p.fill(separatorColor);
		p.rect(x + margin, y, width - margin * 2, 1);
	}

}
