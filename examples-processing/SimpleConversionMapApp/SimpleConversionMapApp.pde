import processing.opengl.*;
import codeanticode.glgraphics.*;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;

de.fhpotsdam.unfolding.Map map;

void setup() {
  size(800, 600, GLConstants.GLGRAPHICS);

  map = new de.fhpotsdam.unfolding.Map(this);
  MapUtils.createDefaultEventDispatcher(this, map);
}

void draw() {
  background(0);

  map.draw();

  noStroke();
  fill(215, 0, 0, 100);

  // Shows geo-location at mouse position
  Location location = map.getLocationFromScreenPosition(mouseX, mouseY);
  text(location.toString(), mouseX, mouseY);

  // Shows marker at Berlin location
  Location loc = new Location(52.5f, 13.4f);
  float xy[] = map.getScreenPositionFromLocation(loc);
  ellipse(xy[0], xy[1], 20, 20);
}
