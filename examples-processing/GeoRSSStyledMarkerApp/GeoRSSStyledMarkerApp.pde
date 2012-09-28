import de.fhpotsdam.unfolding.mapdisplay.*;
import de.fhpotsdam.unfolding.utils.*;
import de.fhpotsdam.unfolding.marker.*;
import de.fhpotsdam.unfolding.tiles.*;
import de.fhpotsdam.unfolding.interactions.*;
import de.fhpotsdam.unfolding.ui.*;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.core.*;
import de.fhpotsdam.unfolding.data.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.texture.*;
import de.fhpotsdam.unfolding.events.*;
import de.fhpotsdam.utils.*;
import de.fhpotsdam.unfolding.providers.*;

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
  MarkerFactory markerFactory = new MarkerFactory();
  markerFactory.setPointClass(EarthquakeMarker.class);
  List<Marker> markers = markerFactory.createMarkers(features);
  map.addMarkers(markers);
}

public void draw() {
  map.draw();
}

