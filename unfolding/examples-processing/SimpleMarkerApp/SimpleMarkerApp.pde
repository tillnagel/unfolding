import processing.opengl.*;
import codeanticode.glgraphics.*;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;

de.fhpotsdam.unfolding.Map map;

Location locationBerlin = new Location(52.5f, 13.4f);
Location locationLondon = new Location(51.5f, 0f);

public void setup() {
  size(800, 600, GLConstants.GLGRAPHICS);
  noStroke();

  map = new de.fhpotsdam.unfolding.Map(this);
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
  float xyBerlin[] = map.getScreenPositionFromLocation(locationBerlin);
  fill(0, 200, 0, 100);
  ellipse(xyBerlin[0], xyBerlin[1], 20, 20);

  // Zoom dependent marker size
  float xyLondon[] = map.getScreenPositionFromLocation(locationLondon);
  fill(200, 0, 0, 100);
  float s = map.getZoom();
  ellipse(xyLondon[0], xyLondon[1], s, s);
}
