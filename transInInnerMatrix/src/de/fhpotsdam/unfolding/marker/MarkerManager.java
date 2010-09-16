package de.fhpotsdam.unfolding.marker;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import processing.core.PGraphics;
import processing.core.PVector;
import de.fhpotsdam.unfolding.mapdisplay.AbstractMapDisplay;

public class MarkerManager {

	public static Logger log = Logger.getLogger(MarkerManager.class);

	List<Marker> markers;

	public MarkerManager(List<Marker> markers) {
		this.markers = markers;
	}

	public void setMarkers(List<Marker> markers) {
		this.markers = markers;
	}

	public void addMarker(Marker marker) {
		if (markers == null) {
			markers = new ArrayList<Marker>();
		}
		markers.add(marker);
	}

	public void draw(AbstractMapDisplay mapDisplay, PGraphics pg) {
		for (Marker marker : markers) {
			
//			PVector v = mapDisplay.getObjectPosForLocation(marker.getLocation());
//			marker.update(v);
//			marker.draw(pg);
		}
	}

}
