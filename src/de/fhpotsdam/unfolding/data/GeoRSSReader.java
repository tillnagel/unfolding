package de.fhpotsdam.unfolding.data;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.data.XML;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * Basic data reader to parse GeoRSS points, in W3C geo
 * 
 * @author tillnagel
 * 
 */
public class GeoRSSReader {

	public static List<Feature> loadData(PApplet p, String fileName) {
		List<Feature> features = new ArrayList<Feature>();

		XML rss = new XML(fileName);
		// Get all items
		XML[] itemXMLElements = rss.getChildren("channel/item");
		for (int i = 0; i < itemXMLElements.length; i++) {
			// Sets lat,lon as locations for each item
			XML latXML = itemXMLElements[i].getChild("geo:lat");
			XML lonXML = itemXMLElements[i].getChild("geo:long");
			if (latXML != null && latXML.getContent() != null) {
				float lat = Float.valueOf(latXML.getContent());
				float lon = Float.valueOf(lonXML.getContent());

				Location location = new Location(lat, lon);
				PointFeature pointFeature = new PointFeature(location);
				features.add(pointFeature);

				// Sets title if existing
				XML titleXML = itemXMLElements[i].getChild("title");
				if (titleXML != null && titleXML.getContent() != null) {
					pointFeature.putProperty("title", titleXML.getContent());
				}
			}
		}

		return features;
	}

	public static List<Feature> loadDataGeoRSS(PApplet p, String fileName) {
		List<Feature> features = new ArrayList<Feature>();

		XML rss = p.loadXML(fileName);
		// Get all items
		XML[] xmlEntries = rss.getChildren("entry");
		for (int i = 0; i < xmlEntries.length; i++) {
			// Sets lat,lon as locations for each item
			XML pointXML = xmlEntries[i].getChild("georss:point");
			if (pointXML != null && pointXML.getContent() != null) {
				String point = pointXML.getContent();
				String[] latLon = point.split(" ");
				float lat = Float.valueOf(latLon[0]);
				float lon = Float.valueOf(latLon[1]);

				Location location = new Location(lat, lon);
				PointFeature pointFeature = new PointFeature(location);
				features.add(pointFeature);

				// Sets title if existing
				XML titleXML = xmlEntries[i].getChild("title");
				if (titleXML != null && titleXML.getContent() != null) {
					pointFeature.putProperty("title", titleXML.getContent());
				}

				// Sets date if existing
				XML dateXML = xmlEntries[i].getChild("dc:date");
				if (dateXML != null && dateXML.getContent() != null) {
					pointFeature.putProperty("date", dateXML.getContent());
				}

				// Sets magnitude if existing
				XML[] catXMLElements = xmlEntries[i].getChildren("category");
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
