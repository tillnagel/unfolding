package de.fhpotsdam.unfolding.examples.data;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Displays a track loaded from a GPX file containing a bike tour in Berlin.
 * The current speed is shown as the color of the path segment.
 */
public class GPXTrackApp extends PApplet {

	Map map;

	Location startLocation = new Location(52.5f, 13.4f);

	// track points
	List<GPXUtils.TrackPoint> trackPoints = new ArrayList<GPXUtils.TrackPoint>();

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this);
		MapUtils.createDefaultEventDispatcher(this, map);
		map.zoomAndPanTo(startLocation, 13);

		trackPoints = GPXUtils.loadGPXTrack(this, "bike-tour.gpx");
		println("Loaded " + trackPoints.size() + " track points");
	}

	public void draw() {
		map.draw();

		float oldXY[] = new float[2];
		int i = 0;
		for (GPXUtils.TrackPoint trackPoint : trackPoints) {
			float xy[] = map.getScreenPositionFromLocation(trackPoint.location);
			if (i == 0) {
				// Draw starting point
				stroke(20, 150);
				strokeWeight(1);
				fill(0, 255, 0);
				ellipse(xy[0], xy[1], 10, 10);
			} else {
				// Draw a line
				strokeWeight(4);
				// Map speed to color of line
				stroke(map((float) trackPoint.speed, 0, 30, 50, 255), 0, 0);
				line(oldXY[0], oldXY[1], xy[0], xy[1]);
			}
			oldXY = xy;
			i++;
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.data.GPXTrackApp" });
	}
}