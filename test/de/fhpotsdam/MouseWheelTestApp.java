package de.fhpotsdam;

import processing.core.PApplet;
import processing.event.MouseEvent;

/**
 * To test P3 beta 6's bug of mouse wheel direction in P2D/P3D. Fixed now.
 * 
 * https://github.com/processing/processing/issues/3840
 *
 */

public class MouseWheelTestApp extends PApplet {

	public void settings() {
		size(400, 400, JAVA2D); // P2D
	}

	public void setup() {
		registerMethod("mouseEvent", this);
	}

	public void draw() {
	}

	public void mouseWheel(MouseEvent e) {
		println("mouseWheel.c:" + e.getCount());
	}

	public void mouseEvent(MouseEvent e) {
		if (e.getAction() == MouseEvent.WHEEL) {
			println("mouseEvent.c:" + e.getCount());
		}
	}

	// non-natural
	// Google Maps: down is zoom-out
	// Browser: down is page-down
	// Count: down is positive

	// natural
	// Google Maps: down is zoom-in
	// Browser: down is page-up
	// Count: down is negative

	public static void main(String args[]) {
		PApplet.main(new String[] { MouseWheelTestApp.class.getName() });
	}
}
