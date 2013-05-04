import processing.opengl.*;
import codeanticode.glgraphics.*;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.utils.*;
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;

de.fhpotsdam.unfolding.Map map1;
de.fhpotsdam.unfolding.Map map2;

public void setup() {
  size(800, 600, GLConstants.GLGRAPHICS);

  map1 = new de.fhpotsdam.unfolding.Map(this, 10, 10, 385, 580, new Microsoft.AerialProvider());
  map2 = new de.fhpotsdam.unfolding.Map(this, 405, 10, 385, 580, 
  	new OpenStreetMap.CloudmadeProvider(MapDisplayFactory.OSM_API_KEY, 30635));
  MapUtils.createDefaultEventDispatcher(this, map1, map2);
}

public void draw() {
  background(0);

  map1.draw();
  map2.draw();
}
