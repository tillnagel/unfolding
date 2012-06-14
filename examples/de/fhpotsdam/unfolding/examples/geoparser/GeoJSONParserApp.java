package de.fhpotsdam.unfolding.examples.geoparser;

import java.awt.event.KeyEvent;

import org.json.JSONException;

import codeanticode.glgraphics.GLConstants;

import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.examples.marker.multimarker.GLLinesMarker;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.utils.GeoJsonParser;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

@SuppressWarnings("serial")
public class GeoJSONParserApp extends PApplet {

	MarkerManager<Marker> contries;
	MarkerManager<Marker> transitLines;
	Map map;

	public void setup() {

		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this);
		MapUtils.createDefaultEventDispatcher(this, map);

		contries = new MarkerManager<Marker>(map);
		transitLines = new MarkerManager<Marker>(map);

		try {
			contries.addMarkers(GeoJsonParser.parseFromJSON(this, "countries.geo.json"));
			transitLines.addMarkers(GeoJsonParser.parseFromJSON(this, "MBTARapidTransitLines.json"));
		} catch (JSONException e) {
			println("Could not load data from JSON");
			e.printStackTrace();
		}

		map.addMarkerManager(contries);
		map.addMarkerManager(transitLines);

		// TODO @Pete - added this additional marker to show that SimpleLinesMarkers are working
		// but there is a problem with transitLines - cant see them
		SimpleLinesMarker area = new SimpleLinesMarker();
		area.addLocations(new Location(52.5f, 13.4f + 0.5f));
		area.addLocations(new Location(51.5f, 0.0f));
		area.addLocations(new Location(48.f, 5f));
		area.addLocations(new Location(52.5f, 13.4f + 0.5f));

		transitLines.addMarker(area);
	}

	public void draw() {
		map.draw();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == 'c') {
			contries.toggleDrawing();
		} else if (e.getKeyChar() == 't') {
			transitLines.toggleDrawing();
		}
	}

}
