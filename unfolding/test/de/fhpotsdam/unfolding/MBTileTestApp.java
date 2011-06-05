package de.fhpotsdam.unfolding;

import de.fhpotsdam.unfolding.mapdisplay.MBTilesLoaderUtils;
import processing.core.PApplet;
import processing.core.PImage;

public class MBTileTestApp extends PApplet {

	PImage tile;
	
	public void setup() {
		size(600, 600);
		
		tile = MBTilesLoaderUtils.getMBTile(15, 10, 4);
	}
	
	public void draw() {
		background(240);
		
		image(tile, mouseX, mouseY);
		
	}
	
}
