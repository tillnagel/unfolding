package de.fhpotsdam.unfolding.utils;

import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.MarkerFactory;
import de.fhpotsdam.unfolding.data.MarkerFactory;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.events.PanMapEvent;
import de.fhpotsdam.unfolding.events.ZoomMapEvent;
import de.fhpotsdam.unfolding.interactions.KeyboardHandler;
import de.fhpotsdam.unfolding.interactions.MouseHandler;
import de.fhpotsdam.unfolding.marker.Marker;

/**
 * Utility functions for the map.
 *
 * So far only the creation of defaultEventDispatcher is offered.
 */
public class MapUtils {

	private static MarkerFactory markerFactory;

	/**
	 * Initializes default events, i.e. all given maps handle mouse and keyboard interactions. No cross-listening
	 * between maps.
	 * 
	 * @param p
	 *            The PApplet needed for mouse and key user interactions.
	 * @param maps
	 *            One or many maps.
	 * @return The EventDispatcher to use for additional event handling.
	 */
	public static EventDispatcher createDefaultEventDispatcher(PApplet p, Map... maps) {
		EventDispatcher eventDispatcher = new EventDispatcher();

		MouseHandler mouseHandler = new MouseHandler(p, maps);
		KeyboardHandler keyboardHandler = new KeyboardHandler(p, maps);

		eventDispatcher.addBroadcaster(mouseHandler);
		eventDispatcher.addBroadcaster(keyboardHandler);

		for (Map map : maps) {
			eventDispatcher.register(map, PanMapEvent.TYPE_PAN, map.getId());
			eventDispatcher.register(map, ZoomMapEvent.TYPE_ZOOM, map.getId());
		}

		return eventDispatcher;
	}

	/**
	 * Initializes default events, i.e. all given maps handle mouse and keyboard interactions. No
	 * cross-listening between maps.
	 *
	 * @param p
	 *            The PApplet needed for mouse and key user interactions.
	 * @param maps
	 *            One or many maps.
	 * @return The EventDispatcher to use for additional event handling.
	 */
	public static EventDispatcher createDefaultEventDispatcher(PApplet p, List<Map> maps) {
		Map[] mapsArray = (maps != null) ? maps.toArray(new Map[0]) : new Map[0];
		return createDefaultEventDispatcher(p, mapsArray);
	}

	public static List<Marker> createSimpleMarkers(List<Feature> features) {
		if (markerFactory == null) {
			markerFactory = new MarkerFactory();
		}
		return markerFactory.createMarkers(features);
	}

}
