package de.fhpotsdam.unfolding.examples.animation;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.looksgood.ani.Ani;


public class AnimationWithAniLibApp extends PApplet {
	
	UnfoldingMap map;
	
	// Ani variables
	float lat, lon;
	
	
	public void setup() {
		size(1200, 600);
		
		map = new UnfoldingMap(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// You have to call always Ani.init() first!
		Ani.init(this);
		lat = 100;
		lon = 100;
	}
	
	
	public void draw() {
		background(255);
		
		map.draw();
		
		// mouse ellipse ///////////////////////////////////////////////////////
		float[] pos = map.getScreenPositionFromLocation(new Location(lat, lon));
		fill(255, 0, 0);
		ellipse(pos[0], pos[1], 20, 20);
	}
	
	
	public void mouseReleased(){
		Location targetLocation = map.getLocationFromScreenPosition(mouseX, mouseY);
		
		// animate the variables x and y in 1.5 sec to targetLocation position.
	    Ani.to(this, 1.5f, "lat", targetLocation.getLat(), Ani.CUBIC_IN_OUT);
	    Ani.to(this, 1.5f, "lon", targetLocation.getLon(), Ani.CUBIC_IN_OUT);
	}
	
	
	public static void main(String[] args) {
		PApplet.main(new String[] {"de.fhpotsdam.unfolding.examples.animation.AniLibApp"});
	}
	
	
}
