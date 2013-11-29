/**
 * Loads country markers, and highlights a polygon when the user hovers over it.
 * 
 * This example starts in Southeast Asia to demonstrate hovering multi-marker polygons
 * such as Indonesia, Phillipines, etc.
 */

import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.data.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.marker.*;
import de.fhpotsdam.unfolding.utils.*;
import java.util.*;

UnfoldingMap map;
List countryMarkers = new ArrayList();
Location indonesiaLocation = new Location(-6.175, 106.82);

void setup() {
  size(800, 600, P2D);

  map = new UnfoldingMap(this);
  map.zoomAndPanTo(indonesiaLocation, 3);
  MapUtils.createDefaultEventDispatcher(this, map);

  List countries = GeoJSONReader.loadData(this, "countries.geo.json");
  List countryMarkers = MapUtils.createSimpleMarkers(countries);
  map.addMarkers(countryMarkers);
}

void draw() {
  background(240);
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
