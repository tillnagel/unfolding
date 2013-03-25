package de.fhpotsdam.unfolding.marker;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import de.fhpotsdam.unfolding.UnfoldingMap;

/**
 * Manages markers of different types. Is always connected to one map (for
 * location to screen coordinate conversion).
 */
public class MarkerManager<E extends Marker> extends AbstractMarkerManager<E> implements Iterable<E> {

	public static Logger log = Logger.getLogger(MarkerManager.class);
	
	private List<E> markers;

	public MarkerManager() {
		markers = new CopyOnWriteArrayList<E>();
	}

	public MarkerManager(List<E> markers) {
		this();
		addMarkers(markers);
	}

	@Override
	public void setMarkers(Collection<E> markers) {
		if (markers != null) {
			this.markers.clear();
			this.markers.addAll(markers);
		} else {
			// Convenient method. Users should use clearMarkers() directly.
			clearMarkers();
		}
	}

	@Override
	public boolean removeMarker(E marker) {
		return markers.remove(marker);
	}

	@Override
	public void clearMarkers() {
		markers.clear();
	}

	@Override
	public boolean addMarker(E marker) {
		if (markers.contains(marker))
			return false;

		markers.add(marker);
		return true;
	}

	@Override
	public void addMarkers(Collection<E> markers) {
		this.markers.addAll(markers);
	}

	@Override
	public List<E> getMarkers() {
		return markers;
	}

	@Override
	public Iterator<E> iterator() {
		return markers.iterator();
	}

}
