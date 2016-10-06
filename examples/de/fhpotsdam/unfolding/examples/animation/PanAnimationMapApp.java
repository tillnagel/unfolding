package de.fhpotsdam.unfolding.examples.animation;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.examples.interaction.NaviButtonMapApp;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Pans smoothly between three locations, in an endless loop.
 * 
 * Press SPACE to switch tweening off (and on again).
 * 
 * See {@link NaviButtonMapApp} for an interactive example on how to animate between two locations.
 */
public class PanAnimationMapApp extends PApplet {

	private static final int ZOOM_LEVEL = 8;
	private final static Location[] LOCATIONS = new Location[] { 
			new Location(52.5, 13.4), 
			new Location(53.6f, 10),
			new Location(51.34, 12.37) };
	private int currentLocation = 0;
	private UnfoldingMap map;
	
	@Override
	public void settings() {
		size(800, 600, P2D);
	}

	@Override
	public void setup() {
		map = new UnfoldingMap(this);
		map.setTweening(true);
		map.zoomAndPanTo(ZOOM_LEVEL, LOCATIONS[currentLocation]);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	@Override
	public void draw() {
		background(0);
		map.draw();
		if (frameCount % 120 == 0) {
			map.panTo(LOCATIONS[currentLocation]);
			currentLocation++;
			if (currentLocation >= LOCATIONS.length) {
				currentLocation = 0;
			}
		}
	}

	@Override
	public void keyPressed() {
		if (key == ' ') {
			map.switchTweening();
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { PanAnimationMapApp.class.getName() });
	}

}
