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
public class GPXReader {

	/**
	 * Loads track segments of a GPX file, and returns them as a single line marker.
	 */
	public static List<Feature> loadData(PApplet p, String gpxFilename) {
		List<Feature> trackFeatures = new ArrayList<Feature>();

		// Load GPX file
		XMLElement gpx = new XMLElement(p, gpxFilename);

		// Create track with all track points
		ShapeFeature trackFeature = new ShapeFeature(FeatureType.LINES);
		
		XMLElement[] itemXMLElements = gpx.getChildren("trk/trkseg/trkpt");
		for (int i = 0; i < itemXMLElements.length; i++) {
			// Adds location for track point
			float lat = itemXMLElements[i].getFloat("lat");
			float lon = itemXMLElements[i].getFloat("lon");
			Location location = new Location(lat, lon);
			trackFeature.addLocation(location);
		}
		
		// Add time as property
		XMLElement timeXMLElement = gpx.getChild("trk/time");
		trackFeature.addProperty("time", timeXMLElement.getContent());
		
		trackFeatures.add(trackFeature);
		return trackFeatures;
	}

}
