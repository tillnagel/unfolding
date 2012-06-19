package de.fhpotsdam.unfolding.examples.data.manual;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.xml.XMLElement;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.examples.marker.infomarker.InfoMarkerApp;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Displays earthquake markers from an RSS feed. Geo-locations are read, and mapped manually
 * according to current map transformation.
 * 
 * (Not using Features, GeoDataReader, nor Marker and MarkerManager.)
 * 
 * As you can see, the markers are not cut off at the border of the map. So, this simple mechanism
 * only works for single full-canvas maps. If you have maps placed on the canvas, or multiple ones,
 * or other more sophisticated usages look into examples with MarkerManager, e.g.
 * {@link InfoMarkerApp}.
 */
public class ManualGeoRSSApp extends PApplet {

	Map map;

	List<Location> rssGeoLocations = new ArrayList<Location>();

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		smooth();

		map = new Map(this, 50, 50, 700, 500);
		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);

		loadRSSGeoLocations();
	}

	public void loadRSSGeoLocations() {
		// Load RSS feed
		String url = "http://earthquake.usgs.gov/earthquakes/catalogs/eqs7day-M5.xml";
		XMLElement rss = new XMLElement(this, url);
		// Get all items
		XMLElement[] itemXMLElements = rss.getChildren("channel/item");
		for (int i = 0; i < itemXMLElements.length; i++) {
			// Adds lat,lon as locations for each item
			XMLElement latXML = itemXMLElements[i].getChild("geo:lat");
			XMLElement lonXML = itemXMLElements[i].getChild("geo:long");
			float lat = Float.valueOf(latXML.getContent());
			float lon = Float.valueOf(lonXML.getContent());

			rssGeoLocations.add(new Location(lat, lon));
		}
	}

	public void draw() {
		background(0);
		map.draw();

		for (Location location : rssGeoLocations) {
			ScreenPosition pos = map.getScreenPosition(location);
			drawEarthquakeMarker(pos);
		}
	}

	public void drawEarthquakeMarker(ScreenPosition pos) {
		noStroke();
		fill(200, 200, 0, 100);
		ellipse(pos.x, pos.y, 40, 40);
		fill(255, 100);
		ellipse(pos.x, pos.y, 30, 30);
		fill(200, 200, 0, 100);
		ellipse(pos.x, pos.y, 20, 20);
		fill(255, 200);
		ellipse(pos.x, pos.y, 10, 10);
	}
	// map = new Map(this, "map", 50, 50, 700, 500);

}
