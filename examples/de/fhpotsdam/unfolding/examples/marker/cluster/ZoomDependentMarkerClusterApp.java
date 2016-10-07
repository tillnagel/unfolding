package de.fhpotsdam.unfolding.examples.marker.cluster;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Shows different set of markers depending on the zoom level.
 * 
 * <p>
 * Zoom in twice to see the detail markers.
 * </p>
 * 
 * This is one way of handling this, via different MarkerManager. You could also simply switch visibility of the markers
 * and use only the default MarkerManager. Which to prefer depends on your use case, and your markers.
 */
public class ZoomDependentMarkerClusterApp extends PApplet {

	UnfoldingMap map;
	MarkerManager<Marker> markerManager;
	MarkerManager<Marker> detailsMarkerManager;

	float oldZoomLevel = 0;

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this);
		map.zoomAndPanTo(5, new Location(41.50, -72.38));

		MapUtils.createDefaultEventDispatcher(this, map);

		markerManager = populateMarkerManager();
		detailsMarkerManager = populateDetailsMarkerManager();

		map.addMarkerManager(markerManager);
		map.addMarkerManager(detailsMarkerManager);
	}

	public void draw() {
		background(0);

		float zoomLevel = map.getZoomLevel();
		if (oldZoomLevel != zoomLevel) {
			if (zoomLevel >= 7) {
				markerManager.disableDrawing();
				detailsMarkerManager.enableDrawing();
			} else {
				markerManager.enableDrawing();
				detailsMarkerManager.disableDrawing();
			}

			oldZoomLevel = zoomLevel;
		}

		map.draw();
	}

	private MarkerManager<Marker> populateMarkerManager() {
		MarkerManager<Marker> markerManager = new MarkerManager<Marker>();

		SimplePointMarker nycMarker = new SimplePointMarker(new Location(40.71, -73.99));
		nycMarker.setDiameter(20);
		markerManager.addMarker(nycMarker);

		SimplePointMarker bostonMarker = new SimplePointMarker(new Location(42.35, -71.04));
		bostonMarker.setDiameter(20);
		markerManager.addMarker(bostonMarker);

		return markerManager;
	}

	private MarkerManager<Marker> populateDetailsMarkerManager() {
		MarkerManager<Marker> markerManager = new MarkerManager<Marker>();

		Marker nycMarker1 = new SimplePointMarker(new Location(40.763, -73.979));
		markerManager.addMarker(nycMarker1);
		Marker nycMarker2 = new SimplePointMarker(new Location(40.852, -73.882));
		markerManager.addMarker(nycMarker2);
		Marker nycMarker3 = new SimplePointMarker(new Location(40.656, -73.944));
		markerManager.addMarker(nycMarker3);
		Marker nycMarker4 = new SimplePointMarker(new Location(40.739, -73.802));
		markerManager.addMarker(nycMarker4);

		Marker bostonMarker1 = new SimplePointMarker(new Location(42.3603, -71.060));
		markerManager.addMarker(bostonMarker1);
		Marker bostonMarker2 = new SimplePointMarker(new Location(42.3689, -71.097));
		markerManager.addMarker(bostonMarker2);

		return markerManager;
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { ZoomDependentMarkerClusterApp.class.getName() });
	}

}
