package de.fhpotsdam.unfolding.interactions;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
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
import de.fhpotsdam.unfolding.geo.Location;

public class MouseHandler extends MapEventBroadcaster {

	public static Logger log = Logger.getLogger(MouseHandler.class);

	public MouseHandler(PApplet p, Map... maps) {
		this(p, Arrays.asList(maps));
	}

	public MouseHandler(PApplet p, List<Map> maps) {
		super(maps);

		p.registerMouseEvent(this);

		p.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
				mouseWheel(evt.getWheelRotation());
			}
		});
	}

	public void mouseClicked() {
		for (Map map : maps) {
			if (map.isHit(mouseX, mouseY)) {
				if (mouseEvent.getClickCount() == 2) {

					// FIXME Order of events is important: Zoom before Pan does not work.
					log.debug("mouse: fire panTo + zoomBy");

					// FIXME Pan + Zoom does not work without tweening
					PanMapEvent panMapEvent = new PanMapEvent(this, map.getId());
					Location location = map.getLocation(mouseX, mouseY);
					panMapEvent.setLocation(location);
					eventDispatcher.fireMapEvent(panMapEvent);

					ZoomMapEvent zoomMapEvent = new ZoomMapEvent(this, map.getId(),
							ZoomMapEvent.ZOOM_BY, 1);
					eventDispatcher.fireMapEvent(zoomMapEvent);
				}
			}
		}
	}

	public void mouseWheel(float delta) {
		for (Map map : maps) {
			if (map.isHit(mouseX, mouseY)) {
				log.debug("mouse: fire zoomBy for " + map.getId());

				ZoomMapEvent zoomMapEvent = new ZoomMapEvent(this, map.getId(),
						ZoomMapEvent.ZOOM_BY);
				if (delta < 0) {
					zoomMapEvent.setZoomLevelDelta(-1);
				} else if (delta > 0) {
					zoomMapEvent.setZoomLevelDelta(1);
				}
				eventDispatcher.fireMapEvent(zoomMapEvent);
			}
		}
	}

	public void mouseDragged() {
		for (Map map : maps) {
			if (map.isHit(mouseX, mouseY)) {
				log.debug("mouse: fire panTo for " + map.getId());

				// FIXME Zoom 17 and 18 not every mouse coord diff results in location diff.
				// Might exist in modest maps, already. modestmaps.processing.SimpleMapApp has same
				// error.

				PVector center = map.getCenterInScreenCoordinates();
				PVector mouse = new PVector(mouseX, mouseY);
				PVector pmouse = new PVector(pmouseX, pmouseY);
				pmouse.sub(mouse);
				pmouse.add(center);
				Location newCenter = map.getLocation(pmouse.x, pmouse.y);

				PanMapEvent panMapEvent = new PanMapEvent(this, map.getId());
				panMapEvent.setLocation(newCenter);
				panMapEvent.setTweening(false);
				eventDispatcher.fireMapEvent(panMapEvent);
			}
		}
	}

	public void mouseMoved() {
	}

	// --------------------------------------------------------------
	// Shamelessly copied code from Processing PApplet. No other way to hook into
	// register Processing mouse event and still have the same functionality with pmouseX, etc.
	// --------------------------------------------------------------

	private int mouseX;
	private int mouseY;
	private int pmouseX, pmouseY;
	private int dmouseX, dmouseY;
	private int emouseX, emouseY;
	private boolean firstMouse;
	private int mouseButton;
	private boolean mousePressed;
	private MouseEvent mouseEvent;

	public void mouseEvent(MouseEvent event) {
		int id = event.getID();
		mouseEvent = event;

		if ((id == MouseEvent.MOUSE_DRAGGED) || (id == MouseEvent.MOUSE_MOVED)) {
			pmouseX = emouseX;
			pmouseY = emouseY;
			mouseX = event.getX();
			mouseY = event.getY();
		}

		int modifiers = event.getModifiers();
		if ((modifiers & InputEvent.BUTTON1_MASK) != 0) {
			mouseButton = PConstants.LEFT;
		} else if ((modifiers & InputEvent.BUTTON2_MASK) != 0) {
			mouseButton = PConstants.CENTER;
		} else if ((modifiers & InputEvent.BUTTON3_MASK) != 0) {
			mouseButton = PConstants.RIGHT;
		}

		if (firstMouse) {
			pmouseX = mouseX;
			pmouseY = mouseY;
			dmouseX = mouseX;
			dmouseY = mouseY;
			firstMouse = false;
		}

		switch (id) {
		case MouseEvent.MOUSE_PRESSED:
			mousePressed = true;
			// mousePressed();
			break;
		case MouseEvent.MOUSE_RELEASED:
			mousePressed = false;
			// mouseReleased();
			break;
		case MouseEvent.MOUSE_CLICKED:
			mouseClicked();
			break;
		case MouseEvent.MOUSE_DRAGGED:
			mouseDragged();
			break;
		case MouseEvent.MOUSE_MOVED:
			mouseMoved();
			break;
		}

		if ((id == MouseEvent.MOUSE_DRAGGED) || (id == MouseEvent.MOUSE_MOVED)) {
			emouseX = mouseX;
			emouseY = mouseY;
		}
	}
}
