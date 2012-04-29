package de.fhpotsdam.unfolding.interactions;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import processing.core.PConstants;
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
				if (key == '+' || key == '-') {
					ZoomMapEvent zoomMapEvent = new ZoomMapEvent(this, map.getId());
					zoomMapEvent.setSubType(ZoomMapEvent.ZOOM_BY);
					if (key == '+') {
						zoomMapEvent.setZoomLevelDelta(1);
					} else {
						zoomMapEvent.setZoomLevelDelta(-1);
					}
					eventDispatcher.fireMapEvent(zoomMapEvent);

				} else if (key == PConstants.CODED) {

					// FIXME Use toLocation instead of panLeft, etc to allow listening maps to pan
					// correctly.
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
			}
		}
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
