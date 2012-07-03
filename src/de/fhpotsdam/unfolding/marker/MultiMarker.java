package de.fhpotsdam.unfolding.marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * A MultiMarker enables handling of multiple, logically grouped markers. Properties and display states are the same for
 * all its markers.
 */
public class MultiMarker implements Marker {

	protected List<Marker> markers = new ArrayList<Marker>();
	public HashMap<String, Object> properties;
	protected boolean selected;
	protected String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMarkers(List<Marker> markers) {
		this.markers = markers;
	}

	public List<Marker> getMarkers() {
		return markers;
	}

	public void addMarkers(Marker... markers) {
		for (Marker marker : markers) {
			this.markers.add(marker);
		}
	}

	/**
	 * return center of all markers
	 */
	@Override
	public Location getLocation() {
		Location center = new Location(0,0);
		for(Marker marker : markers){
			center.add(marker.getLocation());
		}
		center.div((float)markers.size());
		return center;
	}

	@Override
	public void setLocation(float lat, float lng) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLocation(Location location) {
		// TODO Auto-generated method stub

	}

	/**
	 * return distance between location and the (to the location) closest marker
	 */
	//REVISIT alternatively method could return distance to the of all markers
	//implement both in different methods? examples needed!
	@Override
	public double getDistanceTo(Location location) {
		double minDistance = Float.MAX_VALUE;
		for(Marker marker : markers){
			double dist = marker.getDistanceTo(location);
			minDistance =  dist < minDistance ? dist : minDistance;
		}
		return minDistance;
	}

	@Override
	public void setProperties(HashMap<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	public HashMap<String, Object> getProperties() {
		return properties;
	}

	/**
	 * Returns true if at least one marker is hit.
	 */
	@Override
	public boolean isInside(UnfoldingMap map, float checkX, float checkY) {
		boolean inside = false;

		for (Marker marker : markers) {
			inside |= marker.isInside(map, checkX, checkY);
		}

		return inside;
	}

	@Override
	public void draw(UnfoldingMap map) {
		for (Marker marker : markers) {
			marker.draw(map);
		}
	}

	@Override
	public void drawOuter(UnfoldingMap map) {
		for (Marker marker : markers) {
			marker.drawOuter(map);
		}
	}

	/**
	 * Sets the selected status of all its markers.
	 */
	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;

		for (Marker marker : markers) {
			marker.setSelected(selected);
		}
	}

	/**
	 * Indicates whether this multi marker is selected. This does not necessarily reflect the selected states of all its
	 * markers (i.e. a marker of a MultiMarker can have a different selection status):
	 */
	@Override
	public boolean isSelected() {
		return selected;
	}

	public void setColor(int color) {
		for (Marker marker : markers) {
			marker.setColor(color);
		}
	}

	@Override
	public void setStrokeColor(int color) {
		for (Marker marker : markers) {
			marker.setStrokeColor(color);
		}
	}

	@Override
	public void setStrokeWeight(int weight) {
		for (Marker marker : markers) {
			marker.setStrokeWeight(weight);
		}
	}
	
	

}
