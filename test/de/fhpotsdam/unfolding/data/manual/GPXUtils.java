package de.fhpotsdam.unfolding.data.manual;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import processing.core.PApplet;
import processing.data.XML;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.utils.StringUtils;

/**
 * Simple utility class to load track points from a GPX file.
 */
public class GPXUtils {

	public static List<TrackPoint> loadGPXTrack(PApplet p, String gpxFilename) {
		List<TrackPoint> trackPoints = new ArrayList<TrackPoint>();
		Calendar prevTime = null;
		Location prevLocation = null;

		// Load GPX file
		XML gpx = p.loadXML(gpxFilename);
		// Get all track points
		XML[] itemXML = gpx.getChildren("trk/trkseg/trkpt");
		for (int i = 0; i < itemXML.length; i++) {
			// Creates location for track point
			float lat = itemXML[i].getFloat("lat");
			float lon = itemXML[i].getFloat("lon");
			Location location = new Location(lat, lon);

			// Calculates speed for track point
			// Uses time span (h) and distance (km) to previous point to get km/h
			double speed = 0;
			try {
				String timeStr = itemXML[i].getChild("time").getContent();
				// Replace "Z" for Zulu/GMT time with parseable hour offset
				timeStr = timeStr.replaceAll("Z", "+0000");
				Calendar time = StringUtils.parseIsoDateTime(timeStr);

				long timeMS = time.getTimeInMillis();
				if (prevTime != null) {
					long timeDiff = timeMS - prevTime.getTimeInMillis();
					double dist = GeoUtils.getDistance(location, prevLocation);
					speed = dist / ((float) timeDiff / 1000 / 60 / 60);
				}
				prevTime = time;
				prevLocation = location;
			} catch (ParseException e) {
				// println("Error:" + e);
			}

			trackPoints.add(new GPXUtils.TrackPoint(location, speed));
			// println("Added track point at " + location + " w/ speed=" + speed);
		}

		return trackPoints;
	}

	public static class TrackPoint {
		public Location location;
		public double speed;

		public TrackPoint(Location location, double speed) {
			this.location = location;
			this.speed = speed;
		}
	}

}
