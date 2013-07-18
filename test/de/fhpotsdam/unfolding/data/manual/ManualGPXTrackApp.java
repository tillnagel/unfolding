package de.fhpotsdam.unfolding.data.manual;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Displays a track loaded from a GPX file containing a bike tour in Berlin. The current speed is shown as the color of
 * the path segment, with red representing slow and green representing fast speeds.
 */
public class ManualGPXTrackApp extends PApplet {

	UnfoldingMap map;

	Location startLocation = new Location(52.492f, 13.435f);

	// track points
	List<GPXUtils.TrackPoint> trackPoints = new ArrayList<GPXUtils.TrackPoint>();

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this);
		MapUtils.createDefaultEventDispatcher(this, map);
		map.zoomAndPanTo(startLocation, 15);

		trackPoints = GPXUtils.loadGPXTrack(this, "data/bike-tour.gpx");
		println("Loaded " + trackPoints.size() + " track points");
	}

	public void draw() {
		map.draw();

		noStroke();
		fill(255, 170);
		rect(0, 0, width, height);

		ScreenPosition oldPos = null;
		int i = 0;
		for (GPXUtils.TrackPoint trackPoint : trackPoints) {
			ScreenPosition pos = map.getScreenPosition(trackPoint.location);
			if (i == 0) {
				// Draw starting point
				// stroke(20, 150);
				// strokeWeight(1);
				// fill(0, 255, 0);
				// ellipse(pos.x, pos.y, 10, 10);
			} else {

				// Draw glow and line with speed mapped as color

				strokeWeight(8);
				stroke(255 - map((float) trackPoint.speed, 0, 30, 0, 255),
						PApplet.map((float) trackPoint.speed, 0, 30, 0, 255), 0, 50);
				line(oldPos.x, oldPos.y, pos.x, pos.y);

				strokeWeight(2);
				stroke(255 - map((float) trackPoint.speed, 0, 30, 0, 255),
						PApplet.map((float) trackPoint.speed, 0, 30, 0, 255), 0, 200);
				line(oldPos.x, oldPos.y, pos.x, pos.y);
			}
			oldPos = pos;
			i++;
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.data.GPXTrackApp" });
	}
}