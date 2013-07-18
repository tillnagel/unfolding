package de.fhpotsdam.unfolding.data.manual;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Tracks a path by animating the map along its locations. Centers the map around the current location.
 * 
 * Press SPACE to start/stop animation.
 * 
 * The path is loaded from a GPX file containing a bike tour in Berlin. Uses GPXUtils for creating TrackPoints, and
 * displays them manually.
 */
public class CenteredTrackApp extends PApplet {

	UnfoldingMap map;

	Location startLocation = new Location(52.5f, 13.4f);

	// track points
	List<GPXUtils.TrackPoint> trackPoints = new ArrayList<GPXUtils.TrackPoint>();

	boolean animating = false;
	int trackPointIndex = 0;
	int trailNumber = 10;

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this);
		MapUtils.createDefaultEventDispatcher(this, map);
		map.zoomAndPanTo(startLocation, 16);

		trackPoints = GPXUtils.loadGPXTrack(this, "data/bike-tour.gpx");
	}

	public void draw() {
		map.draw();

		if (animating) {
			trackPointIndex++;
			if (trackPointIndex >= trackPoints.size()) {
				trackPointIndex = 0;
			}
		}

		GPXUtils.TrackPoint currentTrackPoint = trackPoints.get(trackPointIndex);
		map.panTo(currentTrackPoint.location);

		// Draws trail
		noStroke();
		for (int i = trackPointIndex; i > trackPointIndex - trailNumber; i--) {
			if (i > 0) {
				GPXUtils.TrackPoint tp = trackPoints.get(i);
				ScreenPosition pos = map.getScreenPosition(tp.location);

				float alpha = Math.round(PApplet.map(i, trackPointIndex, trackPointIndex - trailNumber, 255, 0));
				fill(255, 0, 255, alpha);
				ellipse(pos.x, pos.y, 12, 12);
			}
		}

		// Draws current position
		ScreenPosition currentPos = map.getScreenPosition(currentTrackPoint.location);
		stroke(255, 120);
		strokeWeight(2);
		fill(255, 0, 255, 200);
		ellipse(currentPos.x, currentPos.y, 16, 16);

	}

	public void keyPressed() {
		if (key == ' ') {
			animating = !animating;
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.data.manual.CenteredTrackApp" });
	}
}