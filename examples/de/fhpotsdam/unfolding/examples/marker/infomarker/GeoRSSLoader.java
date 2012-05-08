package de.fhpotsdam.unfolding.examples.marker.infomarker;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PFont;
import processing.xml.XMLElement;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;

public class GeoRSSLoader {

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
