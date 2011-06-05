package de.fhpotsdam.unfolding.examples.marker;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import processing.core.PFont;
import processing.xml.XMLElement;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Multiple markers on single map. If mouse hovers over a marker, it is selcted, thus the label is
 * shown.
 * 
 * On click the clicked marker is returned from the MarkerManager and printed (debug).
 */
public class InfoMarkerApp extends PApplet {

	public static Logger log = Logger.getLogger(ZoomDependentMarkerApp.class);

	Map map;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		smooth();

		map = new Map(this, "map", 10, 10, 780, 580, true, false,
				new OpenStreetMap.CloudmadeProvider(MapDisplayFactory.OSM_API_KEY, 23058));
		map.setTweening(false);
		MapUtils.createDefaultEventDispatcher(this, map);

		PFont font = loadFont("Miso-Light-12.vlw");
		// Create markers and add them to the MarkerManager
		List<Marker> labeledMarkers = loadGeoRSSMarkers(this, "bbc-georss-test.xml", font);
		MarkerManager markerManager = new MarkerManager(map, labeledMarkers);
		map.mapDisplay.setMarkerManager(markerManager);
	}

	public void draw() {
		background(0);

		// Draws map, and all markers connected to this map from the MarkerManager
		map.draw();
	}

	public void mouseMoved() {
		MarkerManager<LabeledMarker> mm = map.mapDisplay.getMarkerManager();
		
		// Deselect all marker
		for (LabeledMarker lm : (List<LabeledMarker>) mm.getMarkers()) {
			lm.setSelected(false);
		}
		
		// Select hit marker
		LabeledMarker marker = (LabeledMarker) mm.getFirstHitMarker(mouseX, mouseY);
		if (marker != null) {
			marker.setSelected(true);
		}

	}

	public void mouseClicked() {
		MarkerManager mm = map.mapDisplay.getMarkerManager();
		LabeledMarker marker = (LabeledMarker) mm.getFirstHitMarker(mouseX, mouseY);
		println("Marker clicked: " + marker.name + ".");
	}

	public static List<Marker> loadGeoRSSMarkers(PApplet p, String url, PFont font) {
		List<Marker> markers = new ArrayList<Marker>();

		XMLElement rss = new XMLElement(p, url);
		XMLElement[] itemXMLElements = rss.getChildren("channel/item");
		for (int i = 0; i < itemXMLElements.length; i++) {
			String name = itemXMLElements[i].getChild("title").getContent();
			XMLElement latXML = itemXMLElements[i].getChild("geo:lat");
			XMLElement lonXML = itemXMLElements[i].getChild("geo:long");
			if (latXML != null && latXML.getContent() != null) {
				float lat = Float.valueOf(latXML.getContent());
				float lon = Float.valueOf(lonXML.getContent());

				Location location = new Location(lat, lon);
				LabeledMarker labeledMarker = new LabeledMarker(font, name, location, 10);
				// labeledMarker.setVisible(false);
				markers.add(labeledMarker);
			}
		}
		return markers;
	}

}
