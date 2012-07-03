package de.fhpotsdam.unfolding.examples.marker.multimarker;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.MarkerFactory;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Shows two polygons for France and Corsica to demonstrate a multi marker. By clicking on one of the two areas the whole MultiMarker gets selected, thus both areas are highlighted.
 * 
 * Set the boolean useMultiMarker to false to see the same areas as independent markers.
 */
@SuppressWarnings("serial")
public class GetNearestMultiMarkerApp extends PApplet {

	UnfoldingMap map;
	List<Marker> countryMarkers = new ArrayList<Marker>();

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new UnfoldingMap(this, "map");
		map.zoomToLevel(4);
		map.panTo(new Location(50f, 12f));
		MapUtils.createDefaultEventDispatcher(this, map);

		initPolygons();
	}

	public void draw() {
		background(240);
		map.draw();
	}

	public void mouseMoved() {
		Marker marker = map.getDefaultMarkerManager().getNearestMarker(mouseX, mouseY);
		if (marker != null) {
			for(Marker countryMarker : countryMarkers){
				countryMarker.setSelected(countryMarker == marker);
				PApplet.println(countryMarker.getDistanceTo(map.getLocation(mouseX,mouseY)));
			}
		}
	}

	private void initPolygons() {
		List<Feature> countries = GeoJSONReader.loadData(this, "countries.geo.json");
		List<Feature> selectedCountries = new ArrayList<Feature>();
		for (Feature feature : countries) {
			if (feature.getId().equalsIgnoreCase("CAN") || feature.getId().equalsIgnoreCase("USA")) {
				selectedCountries.add(feature);
			}
		}
		MarkerFactory markerFactory = new MarkerFactory();
		markerFactory.setPolygonClass(DistancePerLocationPolygonMarker.class);
		countryMarkers = markerFactory.createMarkers(selectedCountries);
		map.addMarkers(countryMarkers);

//		// Crude shape of Alaska
//		alaskaLocations.add(new Location(69.778952, -141.679687));
//		alaskaLocations.add(new Location(71.074056, -156.09375));
//		alaskaLocations.add(new Location(68.656555, -165.585937));
//		alaskaLocations.add(new Location(59.888937, -165.9375));
//		alaskaLocations.add(new Location(56.559482, -156.445312));
//		alaskaLocations.add(new Location(60.239811, -140.976562));
//
//		// Crude shape of USA
//		usaLocations.add(new Location(47.872144, -124.365234));
//		usaLocations.add(new Location(48.458352, -94.658203));
//		usaLocations.add(new Location(41.508577, -83.144531));
//		usaLocations.add(new Location(46.377254, -68.642578));
//		usaLocations.add(new Location(44.465151, -67.675781));
//		usaLocations.add(new Location(25.720735, -80.683594));
//		usaLocations.add(new Location(29.688053, -83.847656));
//		usaLocations.add(new Location(29.840644, -94.042969));
//		usaLocations.add(new Location(26.74561, -97.294922));
//		usaLocations.add(new Location(33.063924, -117.861328));
//
//		// Crude shape of Canada
//		canadaLocations.add(new Location(49.382373, -125.507812));
//		canadaLocations.add(new Location(49.267805, -90.175781));
//		canadaLocations.add(new Location(44.21371, -79.980469));
//		canadaLocations.add(new Location(47.635784, -68.027344));
//		canadaLocations.add(new Location(44.024422, -64.6875));
//		canadaLocations.add(new Location(47.100045, -52.558594));
//		canadaLocations.add(new Location(74.68325, -78.574219));
//		canadaLocations.add(new Location(82.765373, -63.632812));
//		canadaLocations.add(new Location(82.896987, -81.5625));
//		canadaLocations.add(new Location(76.268695, -121.289062));
//		canadaLocations.add(new Location(70.140364, -127.96875));
//		canadaLocations.add(new Location(69.037142, -138.164062));
//		canadaLocations.add(new Location(59.888937, -137.460937));
	}

}
