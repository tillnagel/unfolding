package de.fhpotsdam.unfolding.examples.data.speed;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import processing.core.PApplet;
import processing.data.XML;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.Feature.FeatureType;
import de.fhpotsdam.unfolding.data.GPXReader;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.utils.StringUtils;

public class GPXSpeedReader {

	/**
	 * Loads track segments of a GPX file, and returns them as a lines marker. Additionally, the speed is calculated
	 * between two points, and stored as property.
	 * 
	 * This varies from {@link GPXReader#loadData(PApplet, String)}.
	 */
	public static List<Feature> loadData(PApplet p, String gpxFilename) {
		List<Feature> trackFeatures = new ArrayList<Feature>();

		// Load GPX file
		XML gpx = p.loadXML(gpxFilename);

		Calendar prevTime = null;
		Location prevLocation = null;

		// Create track with all track points
		ShapeFeature trackFeature = new ShapeFeature(FeatureType.LINES);
		List<Double> speedList = new ArrayList<Double>();

		XML[] xmlTrackPoints = gpx.getChildren("trk/trkseg/trkpt");
		for (int i = 0; i < xmlTrackPoints.length; i++) {

			// Adds location for track point
			float lat = xmlTrackPoints[i].getFloat("lat");
			float lon = xmlTrackPoints[i].getFloat("lon");
			Location location = new Location(lat, lon);

			// Calculates speed for track point
			// Uses time span (h) and distance (km) to previous point to get km/h
			double speed = 0;
			try {
				String timeStr = xmlTrackPoints[i].getChild("time").getContent();
				// Replace "Z" for Zulu/GMT time with parseable hour offset
				timeStr = timeStr.replaceAll("Z", "+0000");
				Calendar time = StringUtils.parseIsoDateTime(timeStr);

				if (prevTime != null) {
					long timeMS = time.getTimeInMillis();
					long timeDiff = timeMS - prevTime.getTimeInMillis();
					double dist = GeoUtils.getDistance(location, prevLocation);
					speed = dist / ((float) timeDiff / 1000 / 60 / 60);
				}

				prevTime = time;
				prevLocation = location;

			} catch (ParseException e) {
				// println("Error:" + e);
			}

			speedList.add(speed);
			trackFeature.addLocation(location);
		}
		trackFeature.putProperty("speedList", speedList);
		trackFeatures.add(trackFeature);

		return trackFeatures;
	}

}
