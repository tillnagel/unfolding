package de.fhpotsdam.unfolding.examples;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import processing.core.PVector;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.DebugDisplay;

public class MapInteractionTestApp extends PApplet {

	public static Logger log = Logger.getLogger(MapInteractionTestApp.class);

	Map map;
	DebugDisplay debugDisplay;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this, "map1", 0, 0, 800, 600);
		map.setTweening(false);
		// MapUtils.createDefaultEventDispatcher(this, map);

		debugDisplay = new DebugDisplay(this, map.mapDisplay, 0, 0, 250, 200);

		addMouseWheelListener(new java.awt.event.MouseWheelListener() {
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
				mouseWheel(evt.getWheelRotation());
			}
		});
	}

	Location berlinLocation = new Location(52.439046f, 13.447266f);

	public void draw() {
		background(0);

		map.draw();
		debugDisplay.draw();

		// Show location from mouse
		Location location = map.mapDisplay.getLocationFromScreenPosition(mouseX, mouseY);
		fill(215, 0, 0);
		text(location + "", mouseX, mouseY);

		// Show marker at location
		float[] xy = map.mapDisplay.getScreenPositionFromLocation(berlinLocation);
		ellipse(xy[0], xy[1], 10, 10);
	}

	public void keyPressed() {
		if (key == '+') {
			// TODO Integrate object center as default innerTransCenter in public methods
			float[] xy = map.mapDisplay.getScreenFromObjectPosition(map.mapDisplay.getWidth()/2, map.mapDisplay.getHeight()/2);
			PVector transCenter = new PVector(xy[0], xy[1]);
			map.mapDisplay.setInnerTransformationCenter(transCenter);
			map.zoomLevelIn();
		}
		if (key == '-') {
			map.zoomLevelOut();
		}
		// Needs proper SHIFT+key handling. (Currently, works only on German keyboards)
		if (key == '*') {
			map.zoomIn();
		}
		if (key == '_') {
			map.zoomOut();
		}

		if (key == 'c') {
			map.panTo(berlinLocation);
		}

		// set outer transformation center for (outer) rotation
		if (key == 'i') {
			PVector m = new PVector(mouseX, mouseY);
			map.mapDisplay.setTransformationCenter(m);
		}

		// rotate
		if (key == 'r') {
			map.rotate(PI / 20);
			//map.innerRotate(PI / 20);
		}
		if (key == 'l') {
			map.rotate(PI / 20);
			//map.innerRotate(-PI / 20);
		}

		if (key == 'o') {
			map.move(100, 100);
		}

		// pan
		if (key == CODED) {
			if (keyCode == RIGHT) {
				map.panRight();
			}
			if (keyCode == LEFT) {
				map.panLeft();
			}
			if (keyCode == DOWN) {
				map.panDown();
			}
			if (keyCode == UP) {
				map.panUp();
			}
		}

		if (key == '1') {
			map.zoomToLevel(1);
		}
		if (key == '2') {
			map.zoomToLevel(2);
		}
		if (key == '3') {
			map.zoomToLevel(3);
		}
		if (key == '4') {
			map.zoomToLevel(4);
		}
	}

	public void mouseWheel(float delta) {
		PVector itc = new PVector(mouseX, mouseY);
		map.mapDisplay.setInnerTransformationCenter(itc);
		
		if (delta < 0) {
			map.zoomIn();
		} else if (delta > 0) {
			map.zoomOut();
		}
	}

	public void mousePressed() {
		if (mouseEvent.getClickCount() == 2) {
			map.zoomAndPanTo(mouseX, mouseY, 1);
		}
	}
	
	public void mouseDragged() {
		map.pan(pmouseX, pmouseY, mouseX, mouseY);
	}

}
