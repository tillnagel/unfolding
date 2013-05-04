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

void setup() {
  size(800, 600);
  smooth();

  map = new UnfoldingMap(this);
  MapUtils.createDefaultEventDispatcher(this, map);


  Location berlinLocation = new Location(52.5, 13.4);
  Location dublinLocation = new Location(53.35, -6.26);

  // Create point markers for locations
  SimplePointMarker berlinMarker = new SimplePointMarker(berlinLocation);
  SimplePointMarker dublinMarker = new SimplePointMarker(dublinLocation);

  // Add markers to the maps 
  map.addMarkers(berlinMarker, dublinMarker);
}

void draw() {
  map.draw();

  //Location location = map.getLocation(mouseX, mouseY);
  //fill(0);
  //text(location.getLat() + ", " + location.getLon(), mouseX, mouseY);
  //text(location.toString(), mouseX, mouseY);
}

