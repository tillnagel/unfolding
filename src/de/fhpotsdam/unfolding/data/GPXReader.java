package de.fhpotsdam.unfolding.data;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.xml.XMLElement;
import de.fhpotsdam.unfolding.data.Feature.FeatureType;
import de.fhpotsdam.unfolding.geo.Location;

public class GPXReader {

	/**
	 * Loads track segments of a GPX file, and returns them as a single line marker.
	 */
	public static List<Feature> loadData(PApplet p, String gpxFilename) {
		List<Feature> trackFeatures = new ArrayList<Feature>();

		// Load GPX file
		XMLElement gpx = new XMLElement(p, gpxFilename);

		// Create track with all track points
		MultiFeature trackFeature = new MultiFeature(FeatureType.LINES);
		XMLElement[] itemXMLElements = gpx.getChildren("trk/trkseg/trkpt");
		for (int i = 0; i < itemXMLElements.length; i++) {
			// Adds location for track point
			float lat = itemXMLElements[i].getFloat("lat");
			float lon = itemXMLElements[i].getFloat("lon");
			Location location = new Location(lat, lon);
			trackFeature.addLocation(location);
		}
		trackFeatures.add(trackFeature);

		return trackFeatures;
	}

}
