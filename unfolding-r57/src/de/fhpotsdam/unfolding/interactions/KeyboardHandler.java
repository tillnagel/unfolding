package de.fhpotsdam.unfolding.interactions;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.MapEventBroadcaster;
import de.fhpotsdam.unfolding.events.PanMapEvent;
import de.fhpotsdam.unfolding.events.ZoomMapEvent;

public class KeyboardHandler extends MapEventBroadcaster {

	public static Logger log = Logger.getLogger(KeyboardHandler.class);

	public KeyboardHandler(PApplet p, Map... maps) {
		this(p, Arrays.asList(maps));
	}

	public KeyboardHandler(PApplet p, List<Map> maps) {
		super(maps);

		p.registerKeyEvent(this);
	}

	public void keyPressed(char key, int keyCode) {
		for (Map map : maps) {
			if (map.isActive()) {
				log.debug("key: fire zoom (" + key + ")");
				if (key == '+' || key == '-') {
					ZoomMapEvent zoomMapEvent = new ZoomMapEvent(this, map.getId());
					if (key == '+') {
						zoomMapEvent.setSubType(ZoomMapEvent.ZOOM_BY);
						zoomMapEvent.setZoomLevelDelta(1);
					} else {
						zoomMapEvent.setSubType(ZoomMapEvent.ZOOM_BY);
						zoomMapEvent.setZoomLevelDelta(-1);
					}
					eventDispatcher.fireMapEvent(zoomMapEvent);
				} else if (key == PConstants.CODED) {
					if (keyCode == PConstants.LEFT || keyCode == PConstants.RIGHT
							|| keyCode == PConstants.UP || keyCode == PConstants.DOWN) {
						PanMapEvent panMapEvent = new PanMapEvent(this, map.getId());
						switch (keyCode) {
						case PConstants.LEFT:
							panMapEvent.setSubType(PanMapEvent.PAN_LEFT);
							break;
						case PConstants.RIGHT:
							panMapEvent.setSubType(PanMapEvent.PAN_RIGHT);
							break;
						case PConstants.UP:
							panMapEvent.setSubType(PanMapEvent.PAN_UP);
							break;
						case PConstants.DOWN:
							panMapEvent.setSubType(PanMapEvent.PAN_DOWN);
							break;
						}
						eventDispatcher.fireMapEvent(panMapEvent);
					}
				}

				// log.debug("fire pan by key");
				// PanMapEvent panMapEvent = new PanMapEvent(this, mapDisplay.getId());
				// panMapEvent.setLocation(new Location(0, 0));
				// panMapEvent.setSubType("relative");
				// eventDispatcher.fireMapEvent(panMapEvent);
			}
		}

		/*
		 * // Zoom in and out (level and scale) if (key == '+') { mapManipulation.zoomIn(); } else
		 * if (key == '-') { mapManipulation.zoomOut(); } else if (key == '*') {
		 * mapManipulation.zoomScaleIn(); } else if (key == '_') { mapManipulation.zoomScaleOut(); }
		 * 
		 * // Zoom to specific level if (key == '1') { mapManipulation.zoomToLevel(1); } else if
		 * (key == '2') { mapManipulation.zoomToLevel(2); } else if (key == '3') {
		 * mapManipulation.zoomToLevel(3); } else if (key == '4') { mapManipulation.zoomToLevel(4);
		 * } else if (key == '5') { mapManipulation.zoomToLevel(5); } else if (key == '6') {
		 * mapManipulation.zoomToLevel(6); } else if (key == '7') { mapManipulation.zoomToLevel(7);
		 * } else if (key == '8') { mapManipulation.zoomToLevel(8); } else if (key == '9') {
		 * mapManipulation.zoomToLevel(9); }
		 * 
		 * // Panning if (key == KeyEvent.CHAR_UNDEFINED) { if (keyCode == KeyEvent.VK_LEFT) {
		 * mapManipulation.panLeft(); } else if (keyCode == KeyEvent.VK_RIGHT) {
		 * mapManipulation.panRight(); } else if (keyCode == KeyEvent.VK_UP) {
		 * mapManipulation.panUp(); } else if (keyCode == KeyEvent.VK_DOWN) {
		 * mapManipulation.panDown(); } }
		 * 
		 * if (key == 't') { mapManipulation.switchTweening(); }
		 */
	}

	public void keyReleased(char key, int keyCode) {

	}

	public void keyEvent(KeyEvent keyEvent) {
		char key = keyEvent.getKeyChar();
		int keyCode = keyEvent.getKeyCode();

		switch (keyEvent.getID()) {
		case KeyEvent.KEY_PRESSED:
			keyPressed(key, keyCode);
			break;
		case KeyEvent.KEY_RELEASED:
			keyReleased(key, keyCode);
			break;
		}
	}
}
