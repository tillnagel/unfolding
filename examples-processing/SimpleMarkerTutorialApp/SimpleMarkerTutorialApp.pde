import de.fhpotsdam.unfolding.mapdisplay.*;
import de.fhpotsdam.unfolding.utils.*;
import de.fhpotsdam.unfolding.marker.*;
import de.fhpotsdam.unfolding.tiles.*;
import de.fhpotsdam.unfolding.interactions.*;
import de.fhpotsdam.unfolding.ui.*;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.core.*;
import de.fhpotsdam.unfolding.data.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.texture.*;
import de.fhpotsdam.unfolding.events.*;
import de.fhpotsdam.utils.*;
import de.fhpotsdam.unfolding.providers.*;

import processing.opengl.*;
import codeanticode.glgraphics.*;


UnfoldingMap map;

Location berlinLocation = new Location(52.5, 13.4);
Location dublinLocation = new Location(53.35, -6.26);
SimplePointMarker berlinMarker = new SimplePointMarker(berlinLocation);
SimplePointMarker dublinMarker = new SimplePointMarker(dublinLocation);

void setup() {
  size(800, 600);
  smooth();

  map = new UnfoldingMap(this);
  MapUtils.createDefaultEventDispatcher(this, map);

  // Do not add markers to the map
  berlinMarker.setColor(color(255, 0, 0));
}

void draw() {
  map.draw();

  // Fixed-size marker
  ScreenPosition berlinPos = berlinMarker.getScreenPosition(map);
  strokeWeight(16);
  stroke(67, 211, 227, 100);
  noFill();
  ellipse(berlinPos.x, berlinPos.y, 36, 36);
}

