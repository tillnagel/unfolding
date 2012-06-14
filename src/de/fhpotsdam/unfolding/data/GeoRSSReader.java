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
			// Sets lat,lon as locations for each item
			XMLElement latXML = itemXMLElements[i].getChild("geo:lat");
			XMLElement lonXML = itemXMLElements[i].getChild("geo:long");
			if (latXML != null && latXML.getContent() != null) {
				float lat = Float.valueOf(latXML.getContent());
				float lon = Float.valueOf(lonXML.getContent());

				Location location = new Location(lat, lon);
				PointFeature pointFeature = new PointFeature(location);
				features.add(pointFeature);

				// Sets title if existing
				XMLElement titleXML = itemXMLElements[i].getChild("title");
				if (titleXML != null && titleXML.getContent() != null) {
					pointFeature.putProperty("title", titleXML.getContent());
				}
			}
		}

		return features;
	}

}
