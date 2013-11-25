/**
 * Shows two independent maps side by side, with own interactions and different providers.
 */

import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.utils.*;
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;

UnfoldingMap map1;
UnfoldingMap map2;

public void setup() {
  size(800, 600, P2D);

  map1 = new UnfoldingMap(this, "map1", 10, 10, 385, 580, true, false, new Microsoft.AerialProvider());
  map2 = new UnfoldingMap(this, "map2", 405, 10, 385, 580, true, false, new OpenStreetMap.OSMGrayProvider());
  MapUtils.createDefaultEventDispatcher(this, map1, map2);
}

public void draw() {
  background(0);

  map1.draw();
  map2.draw();
}
