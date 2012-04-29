import processing.opengl.*;
import codeanticode.glgraphics.*;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;

de.fhpotsdam.unfolding.Map map;

List<Location> rssGeoLocations = new ArrayList<Location>();

public void setup() {
  size(800, 600, GLConstants.GLGRAPHICS);
  smooth();

  map = new de.fhpotsdam.unfolding.Map(this);
  map.zoomToLevel(2);
  MapUtils.createDefaultEventDispatcher(this, map);

  loadRSSGeoLocations();
}

public void loadRSSGeoLocations() {
  // Load RSS feed
  String url = "http://earthquake.usgs.gov/earthquakes/catalogs/eqs7day-M5.xml";
  XMLElement rss = new XMLElement(this, url);
  // Get all items
  XMLElement[] itemXMLElements = rss.getChildren("channel/item");
  for (int i = 0; i < itemXMLElements.length; i++) {
    // Adds lat,lon as locations for each item
    XMLElement latXML = itemXMLElements[i].getChild("geo:lat");
    XMLElement lonXML = itemXMLElements[i].getChild("geo:long");
    float lat = Float.valueOf(latXML.getContent());
    float lon = Float.valueOf(lonXML.getContent());

    rssGeoLocations.add(new Location(lat, lon));
  }
}

public void draw() {
  background(0);
  map.draw();

  for (Location location : rssGeoLocations) {
    float xy[] = map.getScreenPositionFromLocation(location);
    drawEarthquakeMarker(xy[0], xy[1]);
  }
}

public void drawEarthquakeMarker(float x, float y) {
  noStroke();
  fill(200, 200, 0, 100);
  ellipse(x, y, 40, 40);
  fill(255, 100);
  ellipse(x, y, 30, 30);
  fill(200, 200, 0, 100);
  ellipse(x, y, 20, 20);
  fill(255, 200);
  ellipse(x, y, 10, 10);
}
