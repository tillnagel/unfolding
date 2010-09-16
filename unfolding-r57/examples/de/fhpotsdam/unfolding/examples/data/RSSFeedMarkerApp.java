package de.fhpotsdam.unfolding.examples.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import processing.core.PVector;
import processing.xml.XMLElement;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

@SuppressWarnings("serial")
public class RSSFeedMarkerApp extends PApplet {

	public static Logger log = Logger.getLogger(RSSFeedMarkerApp.class);

	Map map;
	EventDispatcher eventDispatcher;

	List<Location> rssGeoLocations = new ArrayList<Location>();

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		smooth();

		// Creates default mapDisplay
		map = new Map(this, "map", 50, 50, 700, 500);

		map.zoomToLevel(3);
		map.panCenterTo(new Location(0, 0));
		// map.rotate(radians(-30));

		// Creates default dispatcher
		eventDispatcher = MapUtils.createDefaultEventDispatcher(this, map);

		loadRSSGeoLocations();
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

	public void draw() {
		background(0);
		map.draw();

		// draw the locations
		for (Location location : rssGeoLocations) {
			PVector p = map.mapDisplay.getPointForLocation(location);
			noStroke();
			fill(200, 200, 0, 100);
			ellipse(p.x, p.y, 10, 10);
		}
	}

	public void keyPressed() {
		PVector rotateCenter = new PVector(mouseX, mouseY);
		
		if (key == 'r') {
			map.rotate(-PI/8, rotateCenter);
		} else if (key == 'l') {
			map.rotate(PI/8, rotateCenter);
		}
	}


}
