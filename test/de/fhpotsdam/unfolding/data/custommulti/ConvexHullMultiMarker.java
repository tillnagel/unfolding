package de.fhpotsdam.unfolding.data.custommulti;

import java.util.ArrayList;
import java.util.List;

import processing.core.PVector;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.examples.overviewdetail.connection.ConvexHull;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.GeoUtils;

public class ConvexHullMultiMarker extends MultiMarker {

	private SimplePolygonMarker convexHullMarker = null;

	@Override
	public void setMarkers(List<Marker> markers) {
		super.setMarkers(markers);
		createConvexHull();
	}

	@Override
	public void addMarkers(Marker... markers) {
		super.addMarkers(markers);
		createConvexHull();
	}

	protected void createConvexHull() {
		List<PVector> points = new ArrayList<PVector>();
		for (PVector v : GeoUtils.getLocations(this)) {
			points.add(v);
		}
		List<PVector> convexHullPoints = ConvexHull.buildHull(points);
		List<Location> convexHullLocations = new ArrayList<Location>();
		for (PVector p : convexHullPoints) {
			convexHullLocations.add((Location) p);
		}
		convexHullMarker = new SimplePolygonMarker(convexHullLocations);
	}

	@Override
	public void draw(UnfoldingMap map) {
		if (convexHullMarker != null) {
			convexHullMarker.draw(map);
		}
	}

	@Override
	public void setSelected(boolean selected) {
		super.setSelected(selected);
		convexHullMarker.setSelected(selected);
	}

	@Override
	public boolean isInside(UnfoldingMap map, float checkX, float checkY) {
		return convexHullMarker.isInside(map, checkX, checkY);
	}

}
