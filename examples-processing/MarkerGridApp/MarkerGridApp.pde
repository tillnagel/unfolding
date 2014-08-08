/**
 * Simple interactive grid marker example.
 * Counts all markers within a grid region, and colors the rectangle in proportion.
 */ 

import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.data.*;
import de.fhpotsdam.unfolding.marker.*;
import de.fhpotsdam.unfolding.utils.*;
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

  int gridWidth = 100;
  int gridHeight = 100;
  for (int x = 0; x < width; x += gridWidth) {
    for (int y = 0; y < height; y += gridHeight) {
      int insideMarkerNumber = 0;

      // Count markers inside the current grid region
      MarkerManager<Marker> markerManager = map.getDefaultMarkerManager();
      for (Marker m : markerManager.getMarkers ()) {
        ScreenPosition pos = map.getScreenPosition(m.getLocation());
        if (pos.x > x && pos.x < x + gridWidth && pos.y > y && pos.y < y + gridHeight) {
          insideMarkerNumber++;
        }
      }

      // Map number to color
      float alpha = map(insideMarkerNumber, 0, 10, 0, 255);

      // Draw current grid region
      fill(255, 0, 0, alpha);
      rect(x, y, gridWidth, gridHeight);
    }
  }
}

