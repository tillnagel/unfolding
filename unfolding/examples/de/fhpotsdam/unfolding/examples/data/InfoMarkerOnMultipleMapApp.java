package de.fhpotsdam.unfolding.examples.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import processing.xml.XMLElement;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class InfoMarkerOnMultipleMapApp extends PApplet {

	public static Logger log = Logger.getLogger(InfoMarkerOnMultipleMapApp.class);

	Map map1;
	Map map2;
	EventDispatcher eventDispatcher;

	MarkerManager markerManager;
	
	PFont font;

	public void setup() {
		size(1300, 600, GLConstants.GLGRAPHICS);
		smooth();

		font = loadFont("Miso-Light-12.vlw");
		textFont(font);

		map1 = new Map(this, "map", 10, 10, 780, 580, true, false,
				new OpenStreetMap.CloudmadeProvider(MapDisplayFactory.OSM_API_KEY, 23058));
		map1.setTweening(false);
		map1.rotate(0.3f);
		
		map2 = new Map(this, "map", 800, 10, 490, 580, true, false,
				new OpenStreetMap.CloudmadeProvider(MapDisplayFactory.OSM_API_KEY, 23058));
		map2.setTweening(false);
		eventDispatcher = MapUtils.createDefaultEventDispatcher(this, map1, map2);
		
		log.warn("Does not work! MarkerManager not implemented");

//		List<Marker> markers = loadMarkers();
//		markerManager = new MarkerManager(markers);
		//map1.mapDisplay.markerManager = markerManager; 
		//map2.mapDisplay.markerManager = markerManager; 
		//log.debug("manager=" + map1.mapDisplay.markerManager);
	}

	public void draw() {
		background(0);
		
		map1.draw();
		map2.draw();
	}

	protected List<Marker> loadMarkers() {
		List<Marker> markers = new ArrayList<Marker>();
		
		String url = "bbc-georss-test.xml";
		XMLElement rss = new XMLElement(this, url);
		XMLElement[] itemXMLElements = rss.getChildren("channel/item");
		for (int i = 0; i < itemXMLElements.length; i++) {
			String name = itemXMLElements[i].getChild("title").getContent();
			XMLElement latXML = itemXMLElements[i].getChild("geo:lat");
			XMLElement lonXML = itemXMLElements[i].getChild("geo:long");
			if (latXML != null && latXML.getContent() != null) {
				float lat = Float.valueOf(latXML.getContent());
				float lon = Float.valueOf(lonXML.getContent());

				Location location = new Location(lat, lon);
				//LabeledMarker labeledMarker = new LabeledMarker(font, name, location, 20);
				//markers.add(labeledMarker);
			}
		}
		return markers;
	}

}
