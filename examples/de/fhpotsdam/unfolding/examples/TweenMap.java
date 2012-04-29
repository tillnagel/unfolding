package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class TweenMap extends PApplet {

	Map map;
	Location loc1 = new Location(50, 10);
	Location loc2 = new Location(40, 20);

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		
		// FIXME Tweening does not work for panning, at the moment!
		map.setTweening(true);

		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		map.draw();
	}

	public void keyPressed() {
		if (key == '1') {
			map.panTo(loc1);
		}
		if (key == '2') {
			map.panTo(loc2);
		}
	}
	
	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.SimpleMapApp" });
	}

}
