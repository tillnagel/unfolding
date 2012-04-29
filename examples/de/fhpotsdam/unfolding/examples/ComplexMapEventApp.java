package de.fhpotsdam.unfolding.examples;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.events.PanMapEvent;
import de.fhpotsdam.unfolding.events.ScopedListeners;
import de.fhpotsdam.unfolding.events.ZoomMapEvent;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.interactions.KeyboardHandler;
import de.fhpotsdam.unfolding.interactions.MouseHandler;
import de.fhpotsdam.unfolding.utils.DebugDisplay;

/**
 * Multiple maps with various interaction connections between the maps, i.e. panning one map does
 * also pan another (listening) one.
 * 
 * Press SPACE to set zoom and location programmatically, i.e. w/o interaction.
 * 
 * This example shows a more complex event registration pattern:
 * <ul>
 * <li>mouse-drag on map1: pan map1 (and pan map2 + map3 via listener)</li>
 * <li>dbl-click on map1: zoom map1 (and zoom map2 via listener)</li>
 * <li>key-zoom on map1: zoom map2 (key events only for active: map2)</li>
 * <li>mouse-drag on map2: pan map2 (and pan map3 via listener)</li>
 * <li>dbl-click on map2: zoom map2 (and zoom map3 via listener)</li>
 * <li>key-zoom on map2: zoom map2 (is active) (and zoom map3 via listener)</li>
 * <li>mouse-drag on map3: nothing (own default listener was overwritten)</li>
 * </ul>
 * 
 * Press SPACE to set zoom and location programmatically, i.e. w/o interaction.
 * 
 */
public class ComplexMapEventApp extends PApplet {

	public static Logger log = Logger.getLogger(ComplexMapEventApp.class);

	List<Map> maps = new ArrayList<Map>();

	EventDispatcher eventDispatcher;

	DebugDisplay debugDisplay;

	public void setup() {
		size(1200, 600, GLConstants.GLGRAPHICS);
		smooth();

		// Creates non-default dispatcher to register own broadcasters and listeners.
		eventDispatcher = new EventDispatcher();

		// Creates default mapDisplay
		Map map1 = new Map(this, "map1", 10, 10, 400, 400);
		map1.setTweening(false);
		map1.setActive(false);
		maps.add(map1);
		Map map2 = new Map(this, "map2", 420, 10, 400, 400);
		map2.setTweening(false);
		maps.add(map2);
		Map map3 = new Map(this, "map3", 830, 10, 400, 400);
		map3.setTweening(false);
		map3.setActive(false);
		maps.add(map3);

		debugDisplay = new DebugDisplay(this, map1.mapDisplay, 10, 390, 250, 150);

		MouseHandler mouseHandler = new MouseHandler(this, maps);
		eventDispatcher.addBroadcaster(mouseHandler);
		KeyboardHandler keyboardHandler = new KeyboardHandler(this, maps);
		eventDispatcher.addBroadcaster(keyboardHandler);

		// See class description for detailed explanation of these registrations.
		eventDispatcher.register(map1, "pan");
		eventDispatcher.register(map1, "zoom");
		eventDispatcher.register(map2, "pan", map1.getId(), map2.getId());
		eventDispatcher.register(map2, "zoom", map1.getId(), map2.getId());
		map2.setActive(true); // only map2 gets keyboard (non hit test) events
		eventDispatcher.register(map3, "pan", map1.getId(), map2.getId());
		eventDispatcher.register(map3, "zoom", map2.getId());

		// Prints all listeners
		log.debug("pan=" + eventDispatcher.typedScopedListeners.size());
		List<ScopedListeners> scopedListenersList = eventDispatcher.typedScopedListeners.get("pan");
		log.debug("pan-scopes=" + scopedListenersList.size());
		for (ScopedListeners scopedListeners : scopedListenersList) {
			log.debug(scopedListeners.scopeIds + " has + " + scopedListeners.listeners.size()
					+ " listeners");
		}
	}

	public void draw() {
		background(0);

		for (Map map : maps) {
			map.draw();
		}

		debugDisplay.draw();
	}

	public void keyPressed() {
		if (key == ' ') {
			log.debug("programmed: fire panTo + zoomTo");
			PanMapEvent panMapEvent = new PanMapEvent(this, maps.get(0).getId());
			Location location = new Location(52.4115f, 13.0516f);
			panMapEvent.setToLocation(location);
			eventDispatcher.fireMapEvent(panMapEvent);
			ZoomMapEvent zoomMapEvent = new ZoomMapEvent(this, maps.get(0).getId());
			zoomMapEvent.setSubType("zoomTo");
			zoomMapEvent.setZoomLevel(14);
			eventDispatcher.fireMapEvent(zoomMapEvent);
		}
	}

}
