package de.fhpotsdam.unfolding.utils;

import java.util.List;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.events.MapEvent;
import de.fhpotsdam.unfolding.events.MapEventListener;
import de.fhpotsdam.unfolding.events.PanMapEvent;
import de.fhpotsdam.unfolding.events.ScopedListeners;
import de.fhpotsdam.unfolding.events.ZoomMapEvent;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * Displays various information about the map. Shows information about the current status of the map (e.g. zoom level),
 * the position of the mouse pointer (e.g. geo-location), and some general information (e.g. the map tile provider).
 */
public class DebugDisplay implements MapEventListener {

	public static final float WIDTH_DEFAULT = 200;
	public static final float HEIGHT_DEFAULT = 240;

	PApplet p;
	UnfoldingMap map;
	EventDispatcher eventDispatcher;

	int maxZoomLevels = 18;

	// Position and size
	float x;
	float y;
	float width = WIDTH_DEFAULT;
	float height = HEIGHT_DEFAULT;

	float padding = 4;
	float margin = 14;

	PImage logo;

	// Text for labels and values
	PFont font;
	PFont titleFont;
	int textSize = 12;
	int textColor = 0xFFFFFFFF;

	// colors
	int backgroundColor = 0xF0222222;
	int valueBoxColor = 0x7F000000;
	int separatorColor = 0x32FFFFFF;
	int zoomStepColorOn = 0xFFFFFFFF;
	int zoomStepColorOff = 0xFF666666;
	int eventBoxColorSendingOn = 0xFF5bdae7;
	// int eventBoxColorListeningOff = 0x335bdae7;
	int eventBoxColorReceivingOn = 0xFFfc8720;
	// int eventBoxColorBroadcastingOff = 0x33fc8720;

	// box sizes
	int valueBoxLongWidth = 120;
	float valueBoxMediumWidth = (valueBoxLongWidth - padding) / 2;
	int valueBoxShortWidth = 22;
	int valueBoxHeight = 15;
	int eventBoxHeight = 12;
	int zoomBarWidth = 90;

	// counters to fade event lights
	public static final float MIN_TRANSPARENCY = 0.3f;
	float transparencyDelta = 0.05f;
	float panByListened = MIN_TRANSPARENCY;
	float panToListened = MIN_TRANSPARENCY;
	float zoomByListened = MIN_TRANSPARENCY;
	float zoomToListened = MIN_TRANSPARENCY;
	float panByBroadcasted = MIN_TRANSPARENCY;
	float panToBroadcasted = MIN_TRANSPARENCY;
	float zoomByBroadcasted = MIN_TRANSPARENCY;
	float zoomToBroadcasted = MIN_TRANSPARENCY;

	/**
	 * Shows current information on the mapDisplay and the mouse pointer.
	 * 
	 * @param p
	 *            The PApplet, used to draw and to get mouse position.
	 * @param map
	 *            The map to display debug information about.
	 * @param eventDispatcher
	 *            The dispatcher to register this DebugDisplay to in order to display fired events.
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

			// TODO Register to all scopes, instead of using a few hard-coded strings
			eventDispatcher.register(this, "pan", map.getId(), "map", "map1", "map2");
			eventDispatcher.register(this, "zoom", map.getId(), "map", "map1", "map2");

			// TODO Implement registerToAll
			// eventDispatcher.registerToAll(this, "pan");
		}

		this.x = x;
		this.y = y;

		// Loads resources either from data folder or from jar (see issue #64)
		font = p.loadFont("ui/Lato-Regular-11.vlw");
		titleFont = p.loadFont("ui/Lato-Bold-14.vlw");
		logo = p.loadImage("ui/unfolding-mini-icon.png");
	}

	/**
	 * Shows current information on the mapDisplay and the mouse pointer. Is positioned at top left corner.
	 * 
	 * @param p
	 *            The PApplet, used to draw and to get mouse position.
	 * @param map
	 *            The map to display debug information about.
	 */
	public DebugDisplay(PApplet p, UnfoldingMap map) {
		this(p, map, 10, 10);
	}

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
	public DebugDisplay(PApplet p, UnfoldingMap map, int x, int y) {
		this(p, map, null, x, y);
	}

	// ----------------------------------------------------

	@Override
	public String getId() {
		return null;
	}

	@Override
	public void onManipulation(MapEvent mapEvent) {
		// PApplet.println("Received at DebugDisplay for " + map.getId() + ": " + mapEvent.getType() + " with subtype "
		// + mapEvent.getSubType() + " in scope " + mapEvent.getScopeId());

		if (map.getId().equals(mapEvent.getScopeId())) {
			// Sending event (due to broadcasting in scope)

			// Event is in same scope, which means it was sent by my map

			if (mapEvent.getSubType().equals(PanMapEvent.PAN_BY)) {
				panByBroadcasted = 1;
			}
			if (mapEvent.getSubType().equals(PanMapEvent.PAN_TO)) {
				panToBroadcasted = 1;
			}
			if (mapEvent.getSubType().equals(ZoomMapEvent.ZOOM_BY_LEVEL)) {
				zoomByBroadcasted = 1;
			}
			if (mapEvent.getSubType().equals(ZoomMapEvent.ZOOM_TO_LEVEL)) {
				zoomToBroadcasted = 1;
			}
		}

		// Receiving event (due to listening in scope)

		// Display only the ones registered to in EventDispatcher

		List<ScopedListeners> scopedListenersList = eventDispatcher.typedScopedListeners.get(mapEvent.getType());
		for (ScopedListeners scopedListeners : scopedListenersList) {
			if (scopedListeners.isInScope(mapEvent)) {
				for (MapEventListener listener : scopedListeners.listeners) {
					if (listener instanceof UnfoldingMap) {
						UnfoldingMap listeningMap = (UnfoldingMap) listener;
						if (map.getId().equals(listeningMap.getId())) {

							if (mapEvent.getSubType().equals(PanMapEvent.PAN_BY)) {
								panByListened = 1;
							}
							if (mapEvent.getSubType().equals(PanMapEvent.PAN_TO)) {
								panToListened = 1;
							}
							if (mapEvent.getSubType().equals(ZoomMapEvent.ZOOM_BY_LEVEL)) {
								zoomByListened = 1;
							}
							if (mapEvent.getSubType().equals(ZoomMapEvent.ZOOM_TO_LEVEL)) {
								zoomToListened = 1;
							}
						}
					}
				}
			}
		}
	}

	// ----------------------------------------------------

	public void draw() {

		p.noStroke();
		p.fill(backgroundColor);
		p.rect(x, y, width, height - ((eventDispatcher == null) ? 50 : 0));

		p.image(logo, x + margin, y + margin);

		p.textFont(titleFont);
		p.textSize(14);
		p.fill(textColor);
		String mapName = map.getId();
		int titleX = (int) (x + margin + logo.width + padding * 2) - 2;
		int titleY = (int) (y + margin + logo.height - padding) + 1;
		// Cuts of title from the right if too long to fit 
		while (p.textWidth(mapName) > x + width - titleX - 15) {
			mapName = mapName.substring(0, mapName.length() - 1);
		}

		p.text(mapName, titleX, titleY);

		p.textFont(font);
		p.textSize(11);
		String zoomStr = String.valueOf(map.getZoomLevel());
		String mouseXStr = String.valueOf(p.mouseX) + "px";
		String mouseYStr = String.valueOf(p.mouseY) + "px";
		Location mouseLoc = map.getLocation(p.mouseX, p.mouseY);
		String mouseLatStr = PApplet.nf(mouseLoc.getLat(), 1, 3) + "°";
		String mouseLngStr = PApplet.nf(mouseLoc.getLon(), 1, 3) + "°";

		String rendererFQNStr = p.g.getClass().toString();
		String rendererStr = rendererFQNStr.substring(rendererFQNStr.lastIndexOf('.') + 1);

		String fpsStr = String.valueOf(PApplet.round(p.frameRate));

		String providerFQNStr = map.mapDisplay.getMapProvider().getClass().toString();
		String providerStr = providerFQNStr.substring(providerFQNStr.lastIndexOf('$') + 1);

		float yo = y + 45;
		drawLabelValue("Zoom", zoomStr, x + 60, yo, valueBoxShortWidth);

		drawZoomBar(map.getZoomLevel(), x + 95, yo + 5, maxZoomLevels);

		yo += valueBoxHeight + padding * 2;
		drawSeparator(yo);
		yo += padding * 2;

		drawLabel("Mouse", x + 60, yo);
		drawValue(mouseXStr, x + 60, yo, valueBoxMediumWidth, false);
		drawValue(mouseYStr, x + 60 + valueBoxMediumWidth + padding, yo, valueBoxMediumWidth, false);
		yo += valueBoxHeight + padding;
		drawLabel("", x + 60, yo);
		drawValue(mouseLatStr, x + 60, yo, valueBoxMediumWidth, false);
		drawValue(mouseLngStr, x + 60 + valueBoxMediumWidth + padding, yo, valueBoxMediumWidth, false);

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
			drawLabelEvent("Zoom To", zoomToListened, zoomToBroadcasted, xEventStart + 70, yo, 3);

			fadeEventLights();
		}
	}

	protected void fadeEventLights() {
		if (panByListened > MIN_TRANSPARENCY) {
			panByListened -= transparencyDelta;
		}
		if (panToListened > MIN_TRANSPARENCY) {
			panToListened -= transparencyDelta;
		}
		if (zoomByListened > MIN_TRANSPARENCY) {
			zoomByListened -= transparencyDelta;
		}
		if (zoomToListened > MIN_TRANSPARENCY) {
			zoomToListened -= transparencyDelta;
		}
		if (panByBroadcasted > MIN_TRANSPARENCY) {
			panByBroadcasted -= transparencyDelta;
		}
		if (panToBroadcasted > MIN_TRANSPARENCY) {
			panToBroadcasted -= transparencyDelta;
		}
		if (zoomByBroadcasted > MIN_TRANSPARENCY) {
			zoomByBroadcasted -= transparencyDelta;
		}
		if (zoomToBroadcasted > MIN_TRANSPARENCY) {
			zoomToBroadcasted -= transparencyDelta;
		}
	}

	protected void drawLabelEvent(String label, float listeningValue, float broadcastingValue, float x, float y,
			float valueBoxWidth) {

		int alphaSend = (int) PApplet.map(broadcastingValue, 0, 1, 0, 255);
		drawEvent(x, y + 4, valueBoxWidth, p.color(eventBoxColorSendingOn, alphaSend));

		int alphaReceive = (int) PApplet.map(listeningValue, 0, 1, 0, 255);
		drawEvent(x + 6, y + 4, valueBoxWidth, p.color(eventBoxColorReceivingOn, alphaReceive));

		// label
		p.textFont(font);
		p.textSize(8);
		float textY = y + textSize - 3;
		float labelX = x - padding - p.textWidth(label.toUpperCase());
		p.noStroke();
		p.fill(textColor);
		p.text(label.toUpperCase(), labelX, textY);
	}

	protected void drawEvent(float x, float y, float valueBoxSize, int color) {
		// value box
		float valueBoxX = x + padding;
		float valueBoxY = y;
		p.fill(color);
		p.rect(valueBoxX, valueBoxY, valueBoxSize, valueBoxSize);
	}

	protected void drawLabelValue(String label, String value, float x, float y, float valueBoxWidth) {
		drawValue(value, x, y, valueBoxWidth);
		drawLabel(label, x, y);
	}

	protected void drawLabel(String label, float x, float y) {
		// label
		float textY = y + textSize - 1;
		float labelX = x - padding - p.textWidth(label);
		p.noStroke();
		p.fill(textColor);
		p.text(label, labelX, textY);
	}

	protected void drawValue(String value, float x, float y, float valueBoxWidth) {
		drawValue(value, x, y, valueBoxWidth, true);
	}

	protected void drawValue(String value, float x, float y, float valueBoxWidth, boolean leftAlign) {
		p.noStroke();

		// value box
		float valueBoxX = x + padding;
		float valueBoxY = y;
		p.fill(valueBoxColor);
		p.rect(valueBoxX, valueBoxY, valueBoxWidth, valueBoxHeight);

		// value label (atop box)
		float textY = y + textSize - 1;
		float valueX = 0;
		if (leftAlign) {
			valueX = x + padding * 2;
		} else {
			valueX = (valueBoxX + valueBoxWidth) - padding * 2 - p.textWidth(value);
		}
		p.fill(textColor);
		p.text(value, valueX, textY);
	}

	protected void drawZoomBar(int zoomLevel, float x, float y, int maxZoomLevel) {
		int stepWidth = PApplet.floor((zoomBarWidth / maxZoomLevel));
		for (int i = 0; i < maxZoomLevel; i++) {
			p.fill(i < zoomLevel ? zoomStepColorOn : zoomStepColorOff);

			p.rect(x + i * stepWidth, y, stepWidth - 1, 6);
		}
	}

	protected void drawSeparator(float y) {
		p.fill(separatorColor);
		p.rect(x + margin, y, width - margin * 2, 1);
	}

}
