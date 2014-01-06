/**
 * Displays earthquake markers from an RSS feed, but with own markers.
 * 
 * Uses MarkerFactory (as in the default marker creation way), but uses own styled EarthquakeMarker.
 */

import de.fhpotsdam.unfolding.utils.*;
import de.fhpotsdam.unfolding.marker.*;
import de.fhpotsdam.unfolding.ui.*;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.data.*;
import de.fhpotsdam.unfolding.geo.*;
import java.util.List;

String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_week.atom";

UnfoldingMap map;

public void setup() {
  size(800, 600, P2D);
  smooth();

  map = new UnfoldingMap(this);
  map.zoomToLevel(2);
  MapUtils.createDefaultEventDispatcher(this, map);

  List<Feature> features = GeoRSSReader.loadDataGeoRSS(this, earthquakesURL);
  MarkerFactory markerFactory = new MarkerFactory();
  markerFactory.setPointClass(EarthquakeMarker.class);
  List<Marker> markers = markerFactory.createMarkers(features);
  map.addMarkers(markers);
}

public void draw() {
  map.draw();
}

