package de.fhpotsdam.unfolding.examples.marker;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import processing.xml.XMLElement;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays earthquake markers from an RSS feed. Locations are mapped manually according to current
 * map transformation.
 * 
 * As you can see, the markers are not cut off at the border of the map. So, this simple mechanism
 * only works for single full-canvas maps. If you have maps placed on the canvas, or multiple ones,
 * or other more sophisticated usages look into examples with MarkerManager, e.g.
 * {@link InfoMarkerApp}.
 */
public class SimpleRSSFeedMarkerApp extends PApplet {

	public static Logger log = Logger.getLogger(SimpleRSSFeedMarkerApp.class);

	Map map;

	List<Location> rssGeoLocations = new ArrayList<Location>();

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		smooth();

		// Create default mapDisplay
		map = new Map(this, "map", 50, 50, 700, 500);
		map.setTweening(false);
		map.zoomToLevel(2);

		MapUtils.createDefaultEventDispatcher(this, map);

		loadRSSGeoLocations();
	}

	public void draw() {
		background(0);
		map.draw();

		// Draw the locations on the screen position according to their geo-locations.
		for (Location location : rssGeoLocations) {
			float xy[] = map.mapDisplay.getScreenPositionFromLocation(location);
			noStroke();
			fill(200, 200, 0, 100);
			ellipse(xy[0], xy[1], 10, 10);

		}
	}

	private void loadRSSGeoLocations() {
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

}
