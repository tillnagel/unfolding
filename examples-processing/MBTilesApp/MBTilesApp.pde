
/**
 * YOU NEED TO download the sqlitejdbc driver from https://bitbucket.org/xerial/sqlite-jdbc/
 * and put the jar file into the 'code' directory of this sketch.
 *
 *
 * This example uses a local MBTiles file. Thus, it does not need an Internet connection to load tiles.
 * 
 * For testing purposes and to keep the file size small, this example supports only three zoom levels.
 */
 
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

