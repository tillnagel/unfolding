package de.fhpotsdam.unfolding.utils;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.events.PanMapEvent;
import de.fhpotsdam.unfolding.events.ZoomMapEvent;
import de.fhpotsdam.unfolding.interactions.KeyboardHandler;
import de.fhpotsdam.unfolding.interactions.MouseHandler;

public class MapUtils {

	public static EventDispatcher createDefaultEventDispatcher(PApplet p, Map... maps) {
		EventDispatcher eventDispatcher = new EventDispatcher();

		MouseHandler mouseHandler = new MouseHandler(p, maps);
		KeyboardHandler keyboardHandler = new KeyboardHandler(p, maps);

		eventDispatcher.addBroadcaster(mouseHandler);
		eventDispatcher.addBroadcaster(keyboardHandler);

		for (Map map : maps) {
			eventDispatcher.register(map, PanMapEvent.PAN, map.getId());
			eventDispatcher.register(map, ZoomMapEvent.ZOOM, map.getId());
		}
		
		return eventDispatcher;
	}
	
}
