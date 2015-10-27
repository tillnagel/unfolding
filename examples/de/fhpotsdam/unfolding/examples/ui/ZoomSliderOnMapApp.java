package de.fhpotsdam.unfolding.examples.ui;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.events.MapEvent;
import de.fhpotsdam.unfolding.events.PanMapEvent;
import de.fhpotsdam.unfolding.events.ZoomMapEvent;
import de.fhpotsdam.unfolding.interactions.MouseHandler;

/**
 * Interactive map with slider atop. Uses work-around to mute MouseHandler when dragging slider. Slider always shows
 * current map zoom level, as app listens to map changed events.
 */
public class ZoomSliderOnMapApp extends PApplet {

	UnfoldingMap map;
	ZoomSlider slider;

	EventDispatcher eventDispatcher;

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this);

		eventDispatcher = new EventDispatcher();
		MouseHandler mouseHandler = new MouseHandler(this, map);
		eventDispatcher.addBroadcaster(mouseHandler);
		listen();

		slider = new ZoomSlider(this, map, 50, 30);
	}

	public void listen() {
		eventDispatcher.register(map, PanMapEvent.TYPE_PAN, map.getId());
		eventDispatcher.register(map, ZoomMapEvent.TYPE_ZOOM, map.getId());
	}

	public void mute() {
		eventDispatcher.unregister(map, PanMapEvent.TYPE_PAN, map.getId());
		eventDispatcher.unregister(map, ZoomMapEvent.TYPE_ZOOM, map.getId());
	}

	public void draw() {
		map.draw();

		slider.draw();
	}

	public void mapChanged(MapEvent mapEvent) {
		// Updates slider based on current map zoom
		slider.setZoomLevel(map.getZoomLevel());
	}

	public void mousePressed() {
		if (slider.contains(mouseX, mouseY)) {
			slider.startDrag(mouseX, mouseY);
			mute(); // mute mouse event handling
		}
	}

	public void mouseDragged() {
		if (slider.isDragging()) {
			slider.drag(mouseX, mouseY);
		}
	}

	public void mouseReleased() {
		if (slider.isDragging()) {
			slider.endDrag();
			listen(); // unmute mouse event handling
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { ZoomSliderOnMapApp.class.getName() });
	}

}
