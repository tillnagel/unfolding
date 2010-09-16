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
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class InfoMarkerApp extends PApplet {

	public static Logger log = Logger.getLogger(InfoMarkerApp.class);

	Map map;
	EventDispatcher eventDispatcher;

	List<LabeledMarker> labeledMarkers = new ArrayList<LabeledMarker>();

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		smooth();

		textFont(loadFont("Miso-Light-12.vlw"));

		map = new Map(this, "map", 10, 10, 780, 580, true, false,
				new OpenStreetMap.CloudmadeProvider(MapDisplayFactory.OSM_API_KEY, 23058));
		map.setTweening(false);
		
		//eventDispatcher = MapUtils.createDefaultEventDispatcher(this, map);

		loadMarkers();
	}

	public void draw() {
		background(0);

		map.draw();

		for (LabeledMarker marker : labeledMarkers) {
			float[] xy = map.mapDisplay.getScreenPositionFromLocation(marker.getLocation());
			PVector v = new PVector(xy[0], xy[1]);
			marker.update(v);
			marker.draw();
		}
	}

	protected void loadMarkers() {
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
				LabeledMarker labeledMarker = new LabeledMarker(this, location, 20);
				labeledMarkers.add(labeledMarker);
			}
		}
	}

}
