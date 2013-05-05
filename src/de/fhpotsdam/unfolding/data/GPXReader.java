package de.fhpotsdam.unfolding.data;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.xml.XMLElement;
import de.fhpotsdam.unfolding.data.Feature.FeatureType;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * Experimental GPX reader with just a very basic functionality.
 * 
 * Reads all track points of all track segments of all tracks into one line feature.
 */
public class GPXReader extends GeoDataReader {

	/**
	 * Loads track segments of a GPX file, and returns them as a single line marker.
	 */
	public static List<Feature> loadData(PApplet p, String gpxFilename) {
		List<Feature> trackFeatures = new ArrayList<Feature>();

		// Load GPX file
		XMLElement gpx = new XMLElement(p, gpxFilename);

		// Create track with all track points
		ShapeFeature trackFeature = new ShapeFeature(FeatureType.LINES);
		List<String> trackPointTimes = new ArrayList<String>();

		XMLElement[] itemXMLElements = gpx.getChildren("trk/trkseg/trkpt");
		for (int i = 0; i < itemXMLElements.length; i++) {
			XMLElement trackPoint = itemXMLElements[i];

			// Adds location for track point
			float lat = trackPoint.getFloat("lat");
			float lon = trackPoint.getFloat("lon");
			Location location = new Location(lat, lon);
			trackFeature.addLocation(location);

			XMLElement trackPointTime = trackPoint.getChild("time");
			if (trackPointTime != null) {
				trackPointTimes.add(trackPointTime.getContent());
			}
		}

		// Add (single) time for whole track as property
		XMLElement timeXMLElement = gpx.getChild("trk/time");
		if (timeXMLElement != null) {
			trackFeature.addProperty("time", timeXMLElement.getContent());
		}
		// Add times of all track points as property
		if (!trackPointTimes.isEmpty() && trackFeature.getLocations().size() == trackPointTimes.size()) {
			trackFeature.addProperty("trackPointTimes", trackPointTimes);
		}

		trackFeatures.add(trackFeature);
		return trackFeatures;
	}

}
