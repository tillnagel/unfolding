/**
 * Pans smoothly between three locations, in an endless loop.
 * 
 * Press SPACE to switch tweening off (and on again).
 */
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;

UnfoldingMap map;

Location[] locations = new Location[] {
  new Location(52.5, 13.4), new Location(53.6f, 10), new Location(51.34, 12.37)
};
int currentLocation = 0;

void setup() {
  size(800, 600, P2D);

  map = new UnfoldingMap(this);
  map.setTweening(true);
  map.zoomAndPanTo(locations[currentLocation], 8);

  MapUtils.createDefaultEventDispatcher(this, map);
}

void draw() {
  background(0);
  map.draw();

  if (frameCount % 120 == 0) {
    map.panTo(locations[currentLocation]);
    currentLocation++;
    if (currentLocation >= locations.length) {
      currentLocation = 0;
    }
  }
}

void keyPressed() {
  if (key == ' ') {
    map.switchTweening();
  }
}

