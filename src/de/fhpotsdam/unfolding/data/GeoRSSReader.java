package de.fhpotsdam.unfolding.data;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.xml.XMLElement;
import de.fhpotsdam.unfolding.geo.Location;

public class GeoRSSReader {

	public static List<Feature> loadData(PApplet p, String fileName) {
		List<Feature> features = new ArrayList<Feature>();

		XMLElement rss = new XMLElement(p, fileName);
		// Get all items
		XMLElement[] itemXMLElements = rss.getChildren("channel/item");
		for (int i = 0; i < itemXMLElements.length; i++) {
			// Adds lat,lon as locations for each item
			XMLElement latXML = itemXMLElements[i].getChild("geo:lat");
			XMLElement lonXML = itemXMLElements[i].getChild("geo:long");
			float lat = Float.valueOf(latXML.getContent());
			float lon = Float.valueOf(lonXML.getContent());

			Location location = new Location(lat, lon);
			features.add(new PointFeature(location));
		}

		return features;
	}

}
