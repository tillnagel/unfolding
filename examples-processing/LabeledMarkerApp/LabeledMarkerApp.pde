import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.utils.*;
import de.fhpotsdam.unfolding.marker.*;
import de.fhpotsdam.unfolding.data.*;
import de.fhpotsdam.unfolding.geo.*;

UnfoldingMap map;

public void setup() {
  size(800, 600);
  smooth();

  map = new UnfoldingMap(this, "map");
  map.zoomToLevel(2);
  MapUtils.createDefaultEventDispatcher(this, map);
  
  // Load from GeoRSS file
  List<Feature> features = GeoRSSReader.loadData(this, "http://earthquake.usgs.gov/earthquakes/catalogs/eqs7day-M5.xml");
  // Create (visible) markers from (data) features
  MarkerFactory markerFactory = new MarkerFactory();
  markerFactory.setPointClass(LabeledMarker.class);
  List<Marker> markers = markerFactory.createMarkers(features);
  // Add markers to map
  map.addMarkers(markers);
}

public void draw() {
  map.draw();
}

public void mouseMoved() {
  // Deselect all marker
  for (Marker marker : map.getMarkers()) {
    marker.setSelected(false);
  }

  // Select hit marker
  Marker marker = map.getFirstHitMarker(mouseX, mouseY);
  // NB: Use mm.getHitMarkers(x, y) for multi-selection.
  if (marker != null) {
    marker.setSelected(true);
  }
}
