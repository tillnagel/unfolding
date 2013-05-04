package de.fhpotsdam.unfolding.examples.misc;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Animates smoothly between zoom levels or positions.
 * 
 * FIXME Does not work at the moment. Internal map tweening behavior needs to be fixed.
 *
 */
public class TweenMap extends PApplet {

	UnfoldingMap map;
	Location loc1 = new Location(50, 10);
	Location loc2 = new Location(40, 20);

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this);
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
