package de.fhpotsdam.unfolding.examples.data.temporal;

import java.util.List;

import org.joda.time.DateTime;

import processing.core.PApplet;
import processing.core.PVector;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoRSSReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Displays earthquake markers from an RSS feed over time.
 * 
 * Simply animates through earthquakes in 1h steps, and keeps markers so they aggregate over time.
 * 
 * See {@link AnimatedTemporalDots} for an extended version.
 */
public class TemporalDotsApp extends PApplet {

	String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/atom/4.5/week";

	UnfoldingMap map;
	List<Marker> markers;

	DateTime startTime;
	DateTime endTime;
	DateTime currentTime;

	public void setup() {
		size(800, 600, OPENGL);
		smooth();

		map = new UnfoldingMap(this);
		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);

		List<Feature> features = GeoRSSReader.loadDataGeoRSS(this, earthquakesURL);
		markers = MapUtils.createSimpleMarkers(features);

		// Earthquakes are ordered from latest to oldest
		startTime = new DateTime(features.get(features.size() - 1).getProperty("date"));
		endTime = new DateTime(features.get(0).getProperty("date"));
		currentTime = startTime.plus(0);
		println("Dates of earthquakes ranges from " + startTime + " to " + endTime);
	}

	public void draw() {
		background(0);
		map.draw();

		for (Marker marker : markers) {
			DateTime markerTime = new DateTime(marker.getStringProperty("date"));
			// Check if earthquake has occurred before current time, i.e. visible
			if (markerTime.isBefore(currentTime)) {
				ScreenPosition pos = map.getScreenPosition(marker.getLocation());
				drawEarthquakeMarker(pos);
			}
		}

		// Every 10 frames increase current time by 1h
		if (frameCount % 10 == 0) {
			currentTime = currentTime.plusHours(1);

			// Loop: If end is reached start at beginning again.
			if (currentTime.isAfter(endTime)) {
				currentTime = startTime.plus(0);
			}
		}

		noStroke();
		fill(0, 200);
		rect(10, 10, 270, 20);
		fill(255);
		text("Time: " + currentTime, 13, 24);
	}

	public void drawEarthquakeMarker(PVector pos) {
		fill(255, 0, 0, 100);
		stroke(255, 0, 0, 200);
		strokeWeight(1);
		ellipse(pos.x, pos.y, 15, 15);
	}

}
