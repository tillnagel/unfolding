
// YOU NEED TO download the sqlitejdbc driver from http://code.google.com/p/sqlite-jdbc/
// and put the jar file into the 'code' directory.

import processing.opengl.*;
import codeanticode.glgraphics.*;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;
import de.fhpotsdam.unfolding.providers.*;

UnfoldingMap map;

void setup() {
  size(800, 600, GLConstants.GLGRAPHICS);
  
  String mbTilesConnectionString = "jdbc:sqlite:";
  mbTilesConnectionString += sketchPath("data/blank-1-3.mbtiles");

  map = new UnfoldingMap(this, new MBTilesMapProvider(mbTilesConnectionString));
  MapUtils.createDefaultEventDispatcher(this, map);
  map.setZoomRange(1, 3);
}

void draw() {
  map.draw();
}

