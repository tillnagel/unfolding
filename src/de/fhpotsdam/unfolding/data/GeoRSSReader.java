package de.fhpotsdam.unfolding.data;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.xml.XMLElement;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * Reads GeoRSS files and creates Features.
 * 
 * Basic data reader to parse GeoRSS points, in W3C geo
 */
public class GeoRSSReader extends GeoDataReader {

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

	public static List<Feature> loadDataGeoRSS(PApplet p, String fileName) {
		List<Feature> features = new ArrayList<Feature>();

		XMLElement rss = new XMLElement(p, fileName);
		// Get all items
		XMLElement[] itemXMLElements = rss.getChildren("entry");
		for (int i = 0; i < itemXMLElements.length; i++) {
			// Sets lat,lon as locations for each item
			XMLElement pointXML = itemXMLElements[i].getChild("georss:point");
			if (pointXML != null && pointXML.getContent() != null) {
				String point = pointXML.getContent();
				String[] latLon = point.split(" ");
				float lat = Float.valueOf(latLon[0]);
				float lon = Float.valueOf(latLon[1]);

				Location location = new Location(lat, lon);
				PointFeature pointFeature = new PointFeature(location);
				features.add(pointFeature);

				// Sets title if existing
				XMLElement titleXML = itemXMLElements[i].getChild("title");
				if (titleXML != null && titleXML.getContent() != null) {
					pointFeature.putProperty("title", titleXML.getContent());
				}

				// Sets date if existing
				XMLElement dateXML = itemXMLElements[i].getChild("dc:date");
				if (dateXML != null && dateXML.getContent() != null) {
					pointFeature.putProperty("date", dateXML.getContent());
				}

				// Sets magnitude if existing
				XMLElement[] catXMLElements = itemXMLElements[i].getChildren("category");
				for (int c = 0; c < catXMLElements.length; c++) {
					String label = catXMLElements[c].getString("label");
					if ("Magnitude".equals(label)) {
						pointFeature.putProperty("magnitude", catXMLElements[c].getFloat("term"));
					}
				}
				// getChild("category[@label='Magnitude']"); // not supported by XMLELement

			}
		}

		return features;
	}

}
