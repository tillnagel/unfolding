package de.fhpotsdam.generics;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;

public class MarkerTestApp extends PApplet {

	ClassHavingMarkerManager classHavingMarkerManager;

	MarkerManager<SimplePointMarker> markerManager;

	public void setup() {
		classHavingMarkerManager = new ClassHavingMarkerManager();
		classHavingMarkerManager.markerManager = new MarkerManager<SimplePointMarker>();

		SimplePointMarker marker = new SimplePointMarker();
		markerManager.addMarker(marker);
	}

	public void draw() {
		for (SimplePointMarker marker : markerManager.getMarkers()) {
			marker.getId();
		}
	}

}
