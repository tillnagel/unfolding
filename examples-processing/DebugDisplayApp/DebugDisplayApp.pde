import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;

/**
 * Shows basic information about the map. Can be used for debugging purposes.
 */

UnfoldingMap map;
DebugDisplay debugDisplay;

void setup() {
  size(1024, 768, P2D);

  map = new UnfoldingMap(this, "myMap");
  map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
  MapUtils.createDefaultEventDispatcher(this, map);

  // Create debug display (optional: specify position and size)
  debugDisplay = new DebugDisplay(this, map);
}

void draw() {
  map.draw();
  debugDisplay.draw();
}

