/**
 * Displays the subway lines of Boston, read from a GeoJSON file.
 * 
 * This example shows how to load data features and create markers manually in order to map specific properties; in this
 * case the colors according to the MBTA schema.
 */
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.data.*;
import de.fhpotsdam.unfolding.marker.*;
import java.util.List;

Location bostonLocation = new Location(42.357778f, -71.061667f);

UnfoldingMap map;

void setup() {
  size(800, 600, OPENGL);
  smooth();

  map = new UnfoldingMap(this, new StamenMapProvider.TonerBackground());
  map.zoomToLevel(11);
  map.panTo(bostonLocation);
  map.setZoomRange(9, 17); // prevent zooming too far out
  map.setPanningRestriction(bostonLocation, 50);
  MapUtils.createDefaultEventDispatcher(this, map);

  List<Feature> transitLines = GeoJSONReader.loadData(this, "data/MBTARapidTransitLines.json");

  // Create marker from features, and use LINE property to color the markers.
  List<Marker> transitMarkers = new ArrayList<Marker>();
  for (Feature feature : transitLines) {
    ShapeFeature lineFeature = (ShapeFeature) feature;

    SimpleLinesMarker m = new SimpleLinesMarker(lineFeature.getLocations());
    String lineColor = lineFeature.getStringProperty("LINE");
    color col = 0;
    // Original MBTA colors
    if (lineColor.equals("BLUE")) {
      col = color(44, 91, 167);
    }
    if (lineColor.equals("RED")) {
      col = color(233, 57, 35);
    }
    if (lineColor.equals("GREEN")) {
      col = color(59, 130, 79);
    }
    if (lineColor.equals("SILVER")) {
      col = color(154, 156, 157);
    }
    if (lineColor.equals("ORANGE")) {
      col = color(238, 137, 40);
    }
    m.setColor(col);
    m.setStrokeWeight(5);
    transitMarkers.add(m);
  }

  map.addMarkers(transitMarkers);
}

void draw() {
  map.draw();
}

