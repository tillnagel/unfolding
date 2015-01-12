package de.fhpotsdam.unfolding.marker.overlapping;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class OverlappingMarkerTestApp extends PApplet {

	UnfoldingMap map;

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this, new Microsoft.AerialProvider());
		MapUtils.createDefaultEventDispatcher(this, map);

		List<Location> locationList1 = new ArrayList<Location>();
		locationList1.add(new Location(0, 0));
		locationList1.add(new Location(10, 0));
		locationList1.add(new Location(10, 10));
		locationList1.add(new Location(0, 10));
		Marker marker1 = new SimplePolygonMarker(locationList1);
		marker1.setId("1");

		List<Location> locationList2 = new ArrayList<Location>();
		locationList2.add(new Location(20, 0));
		locationList2.add(new Location(30, 0));
		locationList2.add(new Location(30, 10));
		locationList2.add(new Location(20, 10));
		Marker marker2 = new SimplePolygonMarker(locationList2);
		marker2.setId("2");
		map.addMarker(marker2);

		List<Location> locationList3 = new ArrayList<Location>();
		locationList3.add(new Location(5, 5));
		locationList3.add(new Location(15, 5));
		locationList3.add(new Location(15, 15));
		locationList3.add(new Location(5, 15));
		Marker marker3 = new SimplePolygonMarker(locationList3);
		marker3.setId("3");
		map.addMarker(marker3);

		map.addMarker(marker1);


		for (Marker m : map.getMarkers()) {
			println(m.getId());
		}
	}

	public void draw() {
		map.draw();
	}
	
	public void mouseMoved() {
		for (Marker marker : map.getMarkers()) {
			marker.setSelected(false);
		}
		Marker marker = map.getFirstHitMarker(mouseX, mouseY);
		if (marker != null)
			marker.setSelected(true);
	}

}
