import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;

UnfoldingMap map;

Location locationBerlin = new Location(52.5f, 13.4f);
Location locationLondon = new Location(51.5f, 0f);

public void setup() {
  size(800, 600, P2D);
  noStroke();

  map = new UnfoldingMap(this);
  map.setTweening(true);
  map.zoomToLevel(3);
  map.panTo(new Location(40f, 8f));
  MapUtils.createDefaultEventDispatcher(this, map);
}

public void draw() {
  background(0);
  map.draw();

  // Draws locations on screen positions according to their geo-locations.

  // Fixed-size marker
  ScreenPosition posBerlin = map.getScreenPosition(locationBerlin);
  fill(0, 200, 0, 100);
  ellipse(posBerlin.x, posBerlin.y, 20, 20);

  // Zoom dependent marker size
  ScreenPosition posLondon = map.getScreenPosition(locationLondon);
  fill(200, 0, 0, 100);
  float s = map.getZoom();
  ellipse(posLondon.x, posLondon.y, s, s);
}
