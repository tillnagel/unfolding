package de.fhpotsdam.unfolding.interactions;

import java.util.Arrays;
import java.util.List;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.event.KeyEvent;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.MapEventBroadcaster;
import de.fhpotsdam.unfolding.events.PanMapEvent;
import de.fhpotsdam.unfolding.events.ZoomMapEvent;

/**
 * Handles keyboard input from the user and broadcasts MapEvents such as zoom and pan.
 */
public class KeyboardHandler extends MapEventBroadcaster {
	
	/**
	 * Creates a KeyboardHandler for the given maps.
	 * 
	 * @param p
	 *            The PApplet.
	 * @param maps
	 *            One or more maps.
	 */
	public KeyboardHandler(PApplet p, UnfoldingMap... maps) {
		this(p, Arrays.asList(maps));
	}

	/**
	 * Creates a KeyboardHandler for the given maps.
	 * 
	 * @param p
	 *            The PApplet.
	 * @param maps
	 *            A list of maps.
	 */
	public KeyboardHandler(PApplet p, List<UnfoldingMap> maps) {
		super(maps);

		p.registerMethod("keyEvent",this);
	}

	public void keyPressed(char key, int keyCode) {
		for (UnfoldingMap map : maps) {
			if (map.isActive()) {
				if (key == '+' || key == '-') {
					ZoomMapEvent zoomMapEvent = new ZoomMapEvent(this, map.getId());
					zoomMapEvent.setTransformationCenterLocation(map.getCenter());
					zoomMapEvent.setSubType(ZoomMapEvent.ZOOM_BY_LEVEL);
					if (key == '+') {
						zoomMapEvent.setZoomLevelDelta(1);
					} else {
						zoomMapEvent.setZoomLevelDelta(-1);
					}
					eventDispatcher.fireMapEvent(zoomMapEvent);

				} else if (key == PConstants.CODED) {

					// FIXME Use toLocation instead of panLeft, etc to allow listening maps to pan
					// correctly.
					if (keyCode == PConstants.LEFT || keyCode == PConstants.RIGHT || keyCode == PConstants.UP
							|| keyCode == PConstants.DOWN) {
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
		char key = keyEvent.getKey();
		int keyCode = keyEvent.getKeyCode();

		switch (keyEvent.getAction()) {
		case KeyEvent.PRESS:
			keyPressed(key, keyCode);
			break;
		case KeyEvent.RELEASE:
			keyReleased(key, keyCode);
			break;
		}
	}
}
