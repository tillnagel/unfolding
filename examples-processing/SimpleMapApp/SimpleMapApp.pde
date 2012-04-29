import processing.opengl.*;
import codeanticode.glgraphics.*;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;

de.fhpotsdam.unfolding.Map map;

void setup() {
  size(800, 600, GLConstants.GLGRAPHICS);

  map = new de.fhpotsdam.unfolding.Map(this);
  map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);

  MapUtils.createDefaultEventDispatcher(this, map);
}

void draw() {
  background(0);
  map.draw();
}
