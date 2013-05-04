package de.fhpotsdam.unfolding.examples.animation;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.examples.data.manual.GPXUtils;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Tracks a path by animating the map along its locations. Centers the map around the current location.
 * 
 * The path is loaded from a GPX file containing a bike tour in Berlin.
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

		trackPoints = GPXUtils.loadGPXTrack(this, "bike-tour.gpx");
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
				float tpxy[] = map.getScreenPositionFromLocation(tp.location);

				float alpha = Math.round(PApplet.map(i, trackPointIndex, trackPointIndex - trailNumber, 255, 0));
				fill(255, 0, 255, alpha);
				ellipse(tpxy[0], tpxy[1], 12, 12);
			}
		}

		// Draws current position
		float xy[] = map.getScreenPositionFromLocation(currentTrackPoint.location);
		stroke(255, 120);
		strokeWeight(2);
		fill(255, 0, 255, 200);
		ellipse(xy[0], xy[1], 16, 16);

	}

	public void keyPressed() {
		if (key == ' ') {
			animating = !animating;
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.animation.CenteredTrackApp" });
	}
}