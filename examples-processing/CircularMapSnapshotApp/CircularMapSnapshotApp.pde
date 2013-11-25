/**
 * Users can save snapshots of the current map, which are then shown as radial thumbnails. By clicking on one of those
 * thumbnails the map pans and zooms to the stored location.
 * 
 * Press 's' to take a snapshot of the current map. Click on its thumbnail to restore.
 * 
 * See MapSnapshot.java and CircularMapSnapshot.java for creating the actual snapshot and storage of metadata.
 */
 
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;
import de.fhpotsdam.unfolding.providers.*;
import java.util.List;

UnfoldingMap map;

List<MapSnapshot> mapSnapshots = new ArrayList<MapSnapshot>();

void setup() {
  size(600, 400, P2D);

  map = new UnfoldingMap(this, 0, 0, 400, 400, new StamenMapProvider.WaterColor());
  map.zoomAndPanTo(new Location(51.507222, -0.1275), 10);

  MapUtils.createDefaultEventDispatcher(this, map);
}

void draw() {
  background(0);
  map.draw();

  int x = 415;
  int y = 20;
  for (MapSnapshot mapSnapshot : mapSnapshots) {
    mapSnapshot.draw(x, y, 80, 80);
    x += 90;
    if (x > width - 90) {
      x = 415;
      y += 90;
    }
  }
}

void mouseClicked() {
  for (MapSnapshot mapSnapshot : mapSnapshots) {
    if (mapSnapshot.isInside(mouseX, mouseY)) {
      map.zoomAndPanTo(mapSnapshot.location, mapSnapshot.zoomLevel);
    }
  }
}

void keyPressed() {
  if (key == 's') {
    MapSnapshot mapSnapshot = new CircularMapSnapshot(this, map);
    println("Bookmarked map at " + mapSnapshot.location + " with " + mapSnapshot.zoomLevel);
    mapSnapshots.add(mapSnapshot);
  }
}

