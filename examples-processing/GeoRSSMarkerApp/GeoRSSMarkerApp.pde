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

import processing.opengl.*;
import codeanticode.glgraphics.*;

String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/catalogs/eqs7day-M5.xml";

UnfoldingMap map;

public void setup() {
  size(800, 600, GLConstants.GLGRAPHICS);
  smooth();

  map = new UnfoldingMap(this);
  map.zoomToLevel(2);
  MapUtils.createDefaultEventDispatcher(this, map);

  List<Feature> features = GeoRSSReader.loadData(this, earthquakesURL);
  List<Marker> markers = MapUtils.createSimpleMarkers(features);
  map.addMarkers(markers);
}

public void draw() {
  background(0);
  map.draw();
}
