package de.fhpotsdam.unfolding.interaction;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import processing.core.PVector;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Various map interactions are applied.
 * 
 * Instead of using Unfolding's event mechanism, all user interactions (e.g. mouse-wheel, or
 * key-pressed) are mapped onto map interactions (e.g. pan, and zoom).
 * 
 */
public class ManualMapInteractionsTestApp extends PApplet {

	public static Logger log = Logger.getLogger(ManualMapInteractionsTestApp.class);

	/** The interactive map. */
	UnfoldingMap map;
	/** Debug display for information about the map's current state. */ 
	DebugDisplay debugDisplay;
	
	Location berlinLocation = new Location(52.439046f, 13.447266f);

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this, "map1", 50, 50, 700, 500);
		map.setTweening(false);
		// MapUtils.createDefaultEventDispatcher(this, map);

		debugDisplay = new DebugDisplay(this, map, 0, 0);
	}

	public void draw() {
		background(0);

		map.draw();
		debugDisplay.draw();

		noFill();
		strokeWeight(4);
		stroke(0, 0, 250, 150);
		float[] tl = map.mapDisplay.getScreenFromObjectPosition(0, 0);
		float[] tr = map.mapDisplay.getScreenFromObjectPosition(map.mapDisplay.getWidth(), 0);
		float[] br = map.mapDisplay.getScreenFromObjectPosition(map.mapDisplay.getWidth(), map.mapDisplay.getHeight());
		float[] bl = map.mapDisplay.getScreenFromObjectPosition(0, map.mapDisplay.getHeight());
		beginShape();
		vertex(tl[0], tl[1]);
		vertex(tr[0], tr[1]);
		vertex(br[0], br[1]);
		vertex(bl[0], bl[1]);
		endShape(CLOSE);

		noStroke();
		// Show location from mouse
		Location location = map.mapDisplay.getLocation(mouseX, mouseY);
		fill(215, 0, 0);
		text(location + "", mouseX, mouseY);

		// Show marker at location
		ScreenPosition pos = map.mapDisplay.getScreenPosition(berlinLocation);
		ellipse(pos.x, pos.y, 10, 10);
	}

	public void keyPressed() {
		if (key == '+') {
			// TODO Integrate object center as default innerTransCenter in public methods
			float[] xy = map.mapDisplay.getScreenFromObjectPosition(map.mapDisplay.getWidth() / 2,
					map.mapDisplay.getHeight() / 2);
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
			// map.innerRotate(PI / 20);
		}
		if (key == 'l') {
			map.rotate(-PI / 20);
			// map.innerRotate(-PI / 20);
		}

		if (key == 'R') {
			map.outerRotate(PI / 20);
		}
		if (key == 'L') {
			map.outerRotate(-PI / 20);
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
			map.zoomAndPanTo(1, mouseX, mouseY);
		}
	}

	public void mouseDragged() {
		map.pan(pmouseX, pmouseY, mouseX, mouseY);
	}

}
