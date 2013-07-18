package de.fhpotsdam.unfolding.examples.fun;

import java.util.ArrayList;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.MapEvent;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Some simple animated circle experiment.
 */
public class MovingCirclesMap extends PApplet {

	UnfoldingMap map1;

	ArrayList circles = new ArrayList();
	int circleNumber = 1200;

	int reInitCounter = 0;
	boolean reInit = false;
	
	public void setup() {
		size(800, 600, OPENGL);

		map1 = new UnfoldingMap(this, "map1", 0, 0, 800, 600, true, false, new Microsoft.AerialProvider());
		MapUtils.createDefaultEventDispatcher(this, map1);

		initCircles(circles);
	}

	public void draw() {
		background(0, 0, 30);

		// draw all circles
		drawCircles(0, circles.size());
		// move all circles
		updateCircles(0, circles.size());

		// if not interacted with the map restart after a while
		reInitCounter++;
		if (reInitCounter > 100 || reInit) {
			reInitCircles();
			reInit = false;
			reInitCounter = 0;
		}
	}

	public void mapChanged(MapEvent mapEvent) {
		println("mapevent " + mapEvent.getScopeId());
		reInit = true;
	}

	void initCircles(ArrayList circles) {
		map1.draw();
		loadPixels();
		background(0);

		float start = millis();
		while (circles.size() < circleNumber) {
			// The more circles already drawn the smaller the maxSize is,
			// to fasten the filling towards the end.
			int maxSize = max(10, 35 - (circles.size() / 8));

			// A new circle is only added if it does not intersect with any other circle
			Circle newCircle = new Circle(this, random(width), random(height), random(4, maxSize));
			if (isFree(newCircle, circles)) {
				circles.add(newCircle);
			}
		}
		// println("init=" + (millis() - start));
	}

	void reInitCircles() {

		ArrayList newCircles = new ArrayList();
		initCircles(newCircles);

		// Set all circles to non-moved
		for (int i = 0; i < circles.size(); i++) {
			Circle circle = (Circle) circles.get(i);
			circle.moved = false;
		}

		float start = millis();
		// For each new circle move nearest old circle to new circle's position
		for (int i = 0; i < circles.size(); i++) {
			Circle newCircle = (Circle) newCircles.get(i);
			int col = getVideoColor(newCircle.x, newCircle.y);

			Circle circle = foundNearestCircle(circles, newCircle.x + random(-50, 50), newCircle.y + random(-50, 50));
			circle.moveTo(newCircle.x, newCircle.y, newCircle.radius, col);
		}
		// println("moveTo=" + (millis() - start));

		newCircles = null;
	}

	/**
	 * Returns a nearest not already moved circle. Does not return always the nearest, but one which is in proximity of
	 * the position (x,y) and not moved. (As more circles are already moved the farer "nearest" could be)
	 */
	Circle foundNearestCircle(ArrayList circles, float x, float y) {
		float minDist = width + height;
		Circle foundCircle = null;

		for (int i = 0; i < circles.size(); i++) {
			Circle circle = (Circle) circles.get(i);
			float d = dist(x, y, circle.x, circle.y);
			if (minDist > d && !circle.isMoved()) {
				minDist = d;
				foundCircle = circle;
			}
		}

		return foundCircle;
	}

	void drawCircles(int start, int end) {
		for (int i = start; i < end; i++) {
			Circle circle = (Circle) circles.get(i);
			circle.draw();
		}
	}

	void updateCircles(int start, int end) {
		for (int i = start; i < end; i++) {
			Circle circle = (Circle) circles.get(i);
			circle.update();
		}
	}

	int getVideoColor(float x, float y) {
		return pixels[(int) x + (int) y * width];
	}

	/**
	 * Checks whether the given circle is not intersecting with any other.
	 */
	boolean isFree(Circle aCircle, ArrayList circles) {
		boolean intersect = false;
		int i = 0;
		while (i < circles.size() && !intersect) {
			Circle circle = (Circle) circles.get(i);
			intersect = circle.intersect(aCircle);
			i++;
		}
		return !intersect;
	}

	public void keyPressed() {
		println("fps:" + frameRate);
	}

	public void drawPoint() {
		float pSize = 2.0f + (mouseX / (float) width) * 16.0f;
		int x = (int) random(width);
		int y = (int) random(height);
		int c = pixels[x + y * width];
		fill(pixels[x + y * width], 127);
		noStroke();
		ellipse(x, y, pSize, pSize);
	}

}
