/**
 * Displays markers for earthquake items, read from an RSS stream. Highlights one of the markers and shows its
 * label when user hovers over the mouse. 
 * 
 * The highlight check is done manually for all markers in mouseMoved().
 * 
 * If you want to prevent label overlapping, you either need to re-sort the markers, or create two marker sets, one for
 * the dots, and one for the labels.
 * 
 */

import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.utils.*;
import de.fhpotsdam.unfolding.marker.*;
import de.fhpotsdam.unfolding.data.*;
import de.fhpotsdam.unfolding.geo.*;
import java.util.List;

String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_week.atom";

UnfoldingMap map;

void setup() {
  size(800, 600);
  smooth();

  map = new UnfoldingMap(this, "map");
  map.zoomToLevel(2);
  MapUtils.createDefaultEventDispatcher(this, map);

  // Load from GeoRSS file
  List<Feature> features = GeoRSSReader.loadDataGeoRSS(this, earthquakesURL);
  // Create (visible) markers from (data) features
  MarkerFactory markerFactory = new MarkerFactory();
  markerFactory.setPointClass(LabeledMarker.class);
  List<Marker> markers = markerFactory.createMarkers(features);
  // Add markers to map
  map.addMarkers(markers);
}

void draw() {
  map.draw();
}

void mouseMoved() {
  // Deselect all marker
  for (Marker marker : map.getMarkers()) {
    marker.setSelected(false);
  }

  // Select hit marker
  // Note: Use getHitMarkers(x, y) if you want to allow multiple selection.
  Marker marker = map.getFirstHitMarker(mouseX, mouseY);
  if (marker != null) {
    marker.setSelected(true);
  }
}

