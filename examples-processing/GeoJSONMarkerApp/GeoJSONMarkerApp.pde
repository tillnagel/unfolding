/**
 * Displays countries of the world as simple polygons.
 * 
 * Reads from a GeoJSON file, and uses default marker creation. Features are polygons.
 * 
 * Press SPACE to toggle visibility of the polygons.
 */

import de.fhpotsdam.unfolding.utils.*;
import de.fhpotsdam.unfolding.marker.*;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.data.*;
import de.fhpotsdam.unfolding.geo.*;
import java.util.List;

UnfoldingMap map;

void setup() {
  size(800, 600, P2D);
  smooth();

  map = new UnfoldingMap(this);
  map.zoomToLevel(2);
  MapUtils.createDefaultEventDispatcher(this, map);

  List<Feature> countries = GeoJSONReader.loadData(this, "countries.geo.json");
  List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);
  map.addMarkers(countryMarkers);
}

void draw() {
  map.draw();
}

void keyPressed() {
  if (key == ' ') {
    map.getDefaultMarkerManager().toggleDrawing();
  }
}

