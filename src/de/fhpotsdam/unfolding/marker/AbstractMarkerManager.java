package de.fhpotsdam.unfolding.marker;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;

public abstract class AbstractMarkerManager<E extends Marker> implements Iterable<E> {

	protected UnfoldingMap map;

	protected boolean bEnableDrawing;

	public AbstractMarkerManager() {
		bEnableDrawing = true;
	}

	public abstract Collection<E> getMarkers();

	public abstract void addMarkers(Collection<E> markers);

	public abstract boolean addMarker(E marker);

	public abstract void clearMarkers();

	public abstract boolean removeMarker(E marker);

	public abstract void setMarkers(Collection<E> markers);

	public void setMap(UnfoldingMap map) {
		this.map = map;
	}	

	public boolean isDrawingEnabled() {
		return bEnableDrawing;
	}

	public void enableDrawing() {
		bEnableDrawing = true;
	}

	public void disableDrawing() {
		bEnableDrawing = false;
	}

	public void toggleDrawing() {
		bEnableDrawing = !bEnableDrawing;
	}

	/**
	 * @deprecated Replaced by {@link #getFirstHitMarker(float, float)}
	 */
	@Deprecated
	public Marker isInside(float checkX, float checkY) {
		return getFirstHitMarker(checkX, checkY);
	}

	public E getNearestMarker(float checkX, float checkY) {
		E foundMarker = null;
		double minDist = Double.MAX_VALUE;
		for (E marker : this) {
			double dist = marker.getDistanceTo(map.getLocation(checkX, checkY));
			if (minDist == dist) {
				if (marker.isInside(map, checkX, checkY)) {
					foundMarker = marker;
				}
			} else if (minDist > dist) {
				minDist = dist;
				foundMarker = marker;
			}
		}
		return foundMarker;
	}

	public E getFirstHitMarker(float checkX, float checkY) {

		E foundMarker = null;
		// NB: Markers should be ordered, e.g. by size ascending, i.e. big,
		// medium, small
		for (E marker : this) {

			// NB: If markers are order by size descending, i.e. small, medium,
			// big
			// for (int i = markers.size() - 1; i >= 0; i--) {
			// Marker marker = markers.get(i);

			if (marker.isInside(map, checkX, checkY)) {
				foundMarker = marker;
				break;
			}
		}
		return foundMarker;
	}

	public List<E> getHitMarkers(float checkX, float checkY) {
		List<E> hitMarkers = new LinkedList<E>();
		for (E marker : this) {
			if (marker.isInside(map, checkX, checkY)) {
				hitMarkers.add(marker);
			}
		}
		return hitMarkers;
	}

	@Override
	public abstract Iterator<E> iterator();

	public void draw() {
		if (!bEnableDrawing)
			return;

		for (Marker marker : this) {
			marker.draw(map);
		}
	}

}