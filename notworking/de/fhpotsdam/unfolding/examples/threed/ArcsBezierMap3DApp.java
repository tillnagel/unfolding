package de.fhpotsdam.unfolding.examples.threed;

import java.util.ArrayList;
import java.util.List;

import processing.core.PVector;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class ArcsBezierMap3DApp extends Map3DApp {

	public static final int NUM_STEPS = 50;

	protected Location berlinLocation = new Location(52.5, 13.4);
	protected Location hamburgLocation = new Location(53.5505f, 9.993f);
	protected Location munichLocation = new Location(48.1369f, 11.5752f);
	protected Location warsawLocation = new Location(52.2166f, 21.03333f);

	public void setup() {
		size(1024, 768, GLConstants.GLGRAPHICS);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(berlinLocation, 5);

		this.init3D();
	}

	public void draw() {
		background(0);

		pushMatrix();
		rotateX(0.7f);
		translate(0, -160, -100);
		map.draw();

		mousePos = getMouse3D();

		ScreenPosition pos1 = map.getScreenPosition(berlinLocation);
		ScreenPosition pos2 = map.getScreenPosition(hamburgLocation);
		ScreenPosition pos3 = map.getScreenPosition(munichLocation);
		ScreenPosition warsawPos = map.getScreenPosition(warsawLocation);

		noFill();
		stroke(255, 0, 0, 100);
		strokeWeight(6);
		
		// Draw full bezier
		drawBezier(pos1, pos2);
		drawBezier(pos1, pos3);
		drawBezier(pos1, warsawPos);
		
		
		// Draw spheres each step on one bezier
		stroke(0, 255, 0, 10);
		List<PVector> bezierPoints = getBezierPoints(pos1, pos2);
		for (PVector v : bezierPoints) {
			pushMatrix();
			translate(v.x, v.y, v.z);
			sphere(2);
			popMatrix();
		}
		
		
		// Animate sphere tweening over one bezier
		stroke(0, 0, 255);
		float height = pos1.dist(pos2);
		PVector v = getBezierPoint3D(pos1,  pos2, height, t);
		pushMatrix();
		translate(v.x, v.y, v.z);
		sphere(2);
		popMatrix();
			t += 0.04;
		if (t > 1) {
			t = 0;
		}
	

		popMatrix();

		fill(255);
		noStroke();
		rect(5, 5, 180, 20);
		fill(0);
		text("fps: " + nfs(frameRate, 0, 2), 10, 20);
	}
	
	float t = 0;

	public void drawBezier(ScreenPosition pos1, ScreenPosition pos2) {
		float height = pos1.dist(pos2);
		bezier(pos1.x, pos1.y, 0, pos1.x, pos1.y, height, pos2.x, pos2.y, height, pos2.x, pos2.y, 0);

	}

	public List<PVector> getBezierPoints(ScreenPosition pos1, ScreenPosition pos2) {
		List<PVector> points = new ArrayList<PVector>();

		float height = pos1.dist(pos2);

		int steps = 10;
		for (int i = 0; i <= steps; i++) {
			float t = i / (float) steps;
			PVector v = getBezierPoint3D(pos1, pos2, height, t);
			points.add(v);
		}
		return points;
	}

	private PVector getBezierPoint3D(ScreenPosition pos1, ScreenPosition pos2, float height, float t) {
		float x = bezierPoint(pos1.x, pos1.x, pos2.x, pos2.x, t);
		float y = bezierPoint(pos1.y, pos1.y, pos2.y, pos2.y, t);
		float z = bezierPoint(0, height, height, 0, t);
		return new PVector(x, y, z);
	}
	
	
	
}
