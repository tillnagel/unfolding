/**
 * Displays earthquake markers from an RSS feed for the last 7 days.
 * 
 * Reads from GeoRSS file, and uses default marker creation. 
 * 
 * Features are points (positions of earthquakes).
 */

import de.fhpotsdam.unfolding.utils.*;
import de.fhpotsdam.unfolding.marker.*;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.core.*;
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
  List<Marker> markers = MapUtils.createSimpleMarkers(features);
  map.addMarkers(markers);
}

public void draw() {
  background(0);
  map.draw();
}
