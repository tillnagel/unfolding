package de.fhpotsdam.unfolding.ui;

import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.mapdisplay.AbstractMapDisplay;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class CompassUi {
	public static final float SIZE_DEFAULT = 75;
	public static final float X_DEFAULT = 100;
	public static final float Y_DEFAULT = 100;

	PApplet p;
	AbstractMapDisplay mapDisplay;

	float x;
	float y;
	float size;
	
	private PImage img;
	
	public CompassUi(PApplet p, AbstractMapDisplay mapDisplay, float x, float y, float size) {
		this.p = p;
		this.mapDisplay = mapDisplay;
		this.size = size;
		this.x = x;
		this.y = y;
		img=p.loadImage("data/compass_white.png");
	}
	public CompassUi(PApplet p, AbstractMapDisplay mapDisplay) {
		this(p, mapDisplay, X_DEFAULT, Y_DEFAULT, SIZE_DEFAULT);
	}
	public CompassUi(PApplet p, Map map) {
		this(p, map.mapDisplay, X_DEFAULT, Y_DEFAULT, SIZE_DEFAULT);
	}
	public void draw() {
		//	int zoomLevel = Map.getZoomLevelFromScale(mapDisplay.innerScale);
		//float zoom = Map.getZoomFromScale(mapDisplay.innerScale);
		
		float dir = mapDisplay.innerAngle;
		
		if(img!=null){
			p.pushMatrix();
			p.translate(x, y);
			p.rotate(dir);
			//p.scale(scale);
			p.image(img, -img.width/2, -img.height/2);
			p.popMatrix();
			}
	}
	
	
}
