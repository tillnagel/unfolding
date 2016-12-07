package de.fhpotsdam.unfolding.examples.multi;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.events.MapEventListener;
import de.fhpotsdam.unfolding.events.PanMapEvent;
import de.fhpotsdam.unfolding.events.ZoomMapEvent;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.interactions.MouseHandler;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.Microsoft;

/**
 * Compares the visual style of natural parks in different map providers.
 * 
 * Uses multiple maps, semi-transparent overlaid maps, and all-listenTo-few event chain.
 * 
 */
public class MapComparisonApp extends PApplet {

	public static Logger log = Logger.getLogger(MapComparisonApp.class);

	// Natural park in Oldenburg, Germany
	public Location location = new Location(53.015f, 8.2f);

	EventDispatcher eventDispatcher;

	UnfoldingMap mapOSM;
	UnfoldingMap mapOSMSmall;
	UnfoldingMap mapOSMSmallSatBlend;
	UnfoldingMap mapOSMSmallOSMBlend;
	UnfoldingMap mapOSMSmallSat;

	UnfoldingMap mapGoogle;
	UnfoldingMap mapGoogleSmall;
	UnfoldingMap mapGoogleSmallSatBlend;
	UnfoldingMap mapGoogleSmallGoogleBlend;
	UnfoldingMap mapGoogleSmallSat;

	public void settings() {
		size(1230, 700, P2D);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { MapComparisonApp.class.getName() });
	}

	public void setup() {
		smooth();

		eventDispatcher = new EventDispatcher();

		// OSM --------------------------------------------
		mapOSM = new UnfoldingMap(this, "mapOSM", 10, 10, 400, 335);

		// OSM to Satellite
		mapOSMSmall = new UnfoldingMap(this, "mapOSMSmall", 420, 10, 260, 335);

		mapOSMSmallSatBlend = new UnfoldingMap(this, "mapOSMSmallSatBlend", 690, 10, 260, 335, true, false,
				new Microsoft.AerialProvider());
		mapOSMSmallOSMBlend = new UnfoldingMap(this, "mapOSMSmallOSMBlend", 690, 10, 260, 335);

		// FIXME MS Satellite maps may not be smaller than tile-size. Why? (This is new!)
		mapOSMSmallSat = new UnfoldingMap(this, "mapOSMSmallSat", 960, 10, 260, 335, true, false,
				new Microsoft.AerialProvider());

		// Google -----------------------------------------
		mapGoogle = new UnfoldingMap(this, "maoGoogle", 10, 355, 400, 335, true, false, new Google.GoogleMapProvider());

		// Google to Satellite
		mapGoogleSmall = new UnfoldingMap(this, "mapGoogleSmall", 420, 355, 260, 335, true, false,
				new Google.GoogleMapProvider());

		mapGoogleSmallSatBlend = new UnfoldingMap(this, "mapGoogleSmallSatBlend", 690, 355, 260, 335, true, false,
				new Microsoft.AerialProvider());
		mapGoogleSmallGoogleBlend = new UnfoldingMap(this, "mapGoogleSmallGoogleBlend", 690, 355, 260, 335, true,
				false, new Google.GoogleMapProvider());

		mapGoogleSmallSat = new UnfoldingMap(this, "mapGoogleSmallSat", 960, 355, 260, 335, true, false,
				new Microsoft.AerialProvider());

		// First OSM and first google map are broadcasters
		MouseHandler mouseHandler = new MouseHandler(this, mapOSM, mapGoogle);
		eventDispatcher.addBroadcaster(mouseHandler);

		// All maps listen to events from both broadcasters
		List<MapEventListener> maps = new ArrayList<MapEventListener>();
		maps.add(mapOSM);
		maps.add(mapOSMSmall);
		maps.add(mapOSMSmallOSMBlend);
		maps.add(mapOSMSmallSatBlend);
		maps.add(mapOSMSmallSat);
		maps.add(mapGoogle);
		maps.add(mapGoogleSmall);
		maps.add(mapGoogleSmallGoogleBlend);
		maps.add(mapGoogleSmallSatBlend);
		maps.add(mapGoogleSmallSat);
		eventDispatcher.register(maps, "pan", mapOSM.getId(), mapGoogle.getId());
		eventDispatcher.register(maps, "zoom", mapOSM.getId(), mapGoogle.getId());

		zoomAndPanTo(location);
	}

	public void draw() {
		background(0);

		tint(255, 255);
		mapOSM.draw();
		mapGoogle.draw();

		mapOSMSmall.draw();
		mapOSMSmallSatBlend.draw();
		tint(255, 170);
		mapOSMSmallOSMBlend.draw();
		tint(255, 255);
		mapOSMSmallSat.draw();

		mapGoogleSmall.draw();
		mapGoogleSmallSatBlend.draw();
		tint(255, 170);
		mapGoogleSmallGoogleBlend.draw();
		tint(255, 255);
		mapGoogleSmallSat.draw();
	}

	public void zoomAndPanTo(Location location) {
		PanMapEvent panMapEvent = new PanMapEvent(this, mapOSM.getId());
		panMapEvent.setToLocation(location);
		eventDispatcher.fireMapEvent(panMapEvent);
		ZoomMapEvent zoomMapEvent = new ZoomMapEvent(this, mapOSM.getId(), ZoomMapEvent.ZOOM_TO_LEVEL);
		zoomMapEvent.setTransformationCenterLocation(location);
		zoomMapEvent.setZoomLevel(10);
		eventDispatcher.fireMapEvent(zoomMapEvent);
	}

}
