package de.fhpotsdam.unfolding.data;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.data.XML;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * Reads GeoRSS files and creates Features.
 * 
 * Basic data reader to parse GeoRSS points, in W3C geo
 */
public class GeoRSSReader extends GeoDataReader {

	/**
	 * Loads features from RSS feed in W3C Geo format (geo:lat and geo:long)
	 * 
	 * @param p
	 *            The PApplet.
	 * @param fileName
	 *            The name of the GeoRSS file (can be local or remote, i.e. a URI)
	 * @return A list of geo features.
	 */
	public static List<Feature> loadData(PApplet p, String fileName) {
		List<Feature> features = new ArrayList<Feature>();

		XML rss = p.loadXML(fileName);
		// Get all items
		XML[] itemXML = rss.getChildren("channel/item");
		for (int i = 0; i < itemXML.length; i++) {
			// Sets lat,lon as locations for each item
			XML latXML = itemXML[i].getChild("geo:lat");
			XML lonXML = itemXML[i].getChild("geo:long");
			if (latXML != null && latXML.getContent() != null) {
				float lat = Float.valueOf(latXML.getContent());
				float lon = Float.valueOf(lonXML.getContent());

				Location location = new Location(lat, lon);
				PointFeature pointFeature = new PointFeature(location);
				features.add(pointFeature);

				// Sets title if existing
				XML titleXML = itemXML[i].getChild("title");
				if (titleXML != null && titleXML.getContent() != null) {
					pointFeature.putProperty("title", titleXML.getContent());
				}
			}
		}

		return features;
	}

	/**
	 * Loads features from RSS feed in GeoRSS format (georss:point).
	 * 
	 * @param p
	 *            The PApplet.
	 * @param fileName
	 *            The name of the GeoRSS file (can be local or remote, i.e. a URI)
	 * @return A list of geo features.
	 */
	public static List<Feature> loadDataGeoRSS(PApplet p, String fileName) {
		List<Feature> features = new ArrayList<Feature>();

		XML rss = p.loadXML(fileName);
		// Get all items
		XML[] itemXML = rss.getChildren("entry");
		for (int i = 0; i < itemXML.length; i++) {
			// Sets lat,lon as locations for each item
			XML pointXML = itemXML[i].getChild("georss:point");
			if (pointXML != null && pointXML.getContent() != null) {
				String point = pointXML.getContent();
				String[] latLon = point.split(" ");
				float lat = Float.valueOf(latLon[0]);
				float lon = Float.valueOf(latLon[1]);

				Location location = new Location(lat, lon);
				PointFeature pointFeature = new PointFeature(location);
				features.add(pointFeature);

				// Sets title if existing
				XML titleXML = itemXML[i].getChild("title");
				if (titleXML != null && titleXML.getContent() != null) {
					pointFeature.putProperty("title", titleXML.getContent());
				}

				// Sets date if existing
				XML dateXML = itemXML[i].getChild("dc:date");
				if (dateXML != null && dateXML.getContent() != null) {
					pointFeature.putProperty("date", dateXML.getContent());
				}

				// Sets magnitude if existing
				XML[] catXML = itemXML[i].getChildren("category");
				for (int c = 0; c < catXML.length; c++) {
					String label = catXML[c].getString("label");
					if ("Magnitude".equals(label)) {
						Float magnitude = 0f;
						String magnitudeString = catXML[c].getString("term");
						if (magnitudeString.contains("Magnitude")) {
							magnitudeString = magnitudeString.replace("Magnitude", "");
						}
						magnitude = Float.valueOf(magnitudeString);
						pointFeature.putProperty("magnitude", magnitude);
					}
				}
				// getChild("category[@label='Magnitude']"); // not supported by XMLElement

			}
		}

		return features;
	}

}
