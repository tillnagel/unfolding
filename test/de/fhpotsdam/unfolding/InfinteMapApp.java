package de.fhpotsdam.unfolding;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.mapdisplay.AbstractMapDisplay;
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.*;
import de.fhpotsdam.unfolding.tiles.*;


public class InfinteMapApp extends PApplet {

	Map map;
	DebugDisplay debug;
	Location cursorLocation;

	public void setup() {
		size(800, 640, GLConstants.GLGRAPHICS);
		map = new Map(this);
		MapUtils.createDefaultEventDispatcher(this, map);
		cursorLocation  = map.getLocation(new ScreenPosition(this.mouseX, this.mouseY));		
		map.minScale=map.getZoom();
		map.setInfiniteMap(true);
		//map.setTweening(true);
	}

	public void draw() {
	
		map.draw();
		//auto-pan of the map
		//map.panBy(.5f,0);
		
		//shows map coordinates of the current mouse position
		cursorLocation = map.getLocation(new ScreenPosition(this.mouseX, this.mouseY));
		fill(0);
		text("("+cursorLocation.x+", "+cursorLocation.y+")", mouseX, mouseY);
	}

	public static void main(String[] args) {
		// Here we start the actual Unfolding part
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.test.InfiniteMapApp" });
	}
}