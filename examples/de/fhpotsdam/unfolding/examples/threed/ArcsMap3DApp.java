package de.fhpotsdam.unfolding.examples.threed;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import toxi.geom.Line3D;
import toxi.geom.Spline3D;
import toxi.geom.Vec3D;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class ArcsMap3DApp extends Map3DApp {

	public static final int NUM_STEPS = 50;

	protected Location berlinLocation = new Location(52.5, 13.4);
	protected Location hamburgLocation = new Location(53.5505f, 9.993f);
	protected Location munichLocation = new Location(48.1369f, 11.5752f);
	protected Location warsawLocation = new Location(52.2166f, 21.03333f);

	public void setup() {
		size(1024, 768, OPENGL);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(berlinLocation, 3);

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
		drawBezier(pos1, pos2);
		// drawBezier(pos1, pos3);
		// drawBezier(pos1, warsawPos);

		popMatrix();

		fill(255);
		noStroke();
		rect(5, 5, 180, 20);
		fill(0);
		text("fps: " + nfs(frameRate, 0, 2), 10, 20);
	}

	public void drawBezier(ScreenPosition pos1, ScreenPosition pos2) {
		float height = pos1.dist(pos2);
		bezier(pos1.x, pos1.y, 0, pos1.x, pos1.y, height, pos2.x, pos2.y, height, pos2.x, pos2.y, 0);

	}

	// OUTDATED EXPERIMENTS TO DRAW ARCS ---------------------------------------------

	public void drawArcs(ScreenPosition pos1, ScreenPosition pos2) {

		Vec3D a = new Vec3D(pos1.x, pos1.y, 0);
		Vec3D b = new Vec3D(pos2.x, pos2.y, 0);

		boolean showArc = false;
		List<Vec3D> arcPoints;
		if (showArc) {
			arcPoints = createNewArc(a, b);
		} else {
			arcPoints = getHalfCircle(a, b, 10, false);
		}

		beginShape();
		for (int i = 1; i < arcPoints.size(); i++) {
			Vec3D v = arcPoints.get(i);
			vertex(v.x, v.y, v.z);
		}
		endShape();

	}

	public List<Vec3D> createNewArc(Vec3D a, Vec3D b) {
		// place zenith at mid point in XZ plane
		Vec3D zenith = a.interpolateTo(b, 0.5f);
		// set Y axis to half distance
		zenith.y = a.distanceTo(b) / 20;
		// put points into spline
		Spline3D s = new Spline3D();
		s.add(a);
		s.add(zenith);
		s.add(b);
		// sample curve
		return s.computeVertices(20);
	}

	public List<Vec3D> getHalfCircle(Vec3D p1, Vec3D p2, float weight, boolean upside) {
		Line3D line = new Line3D(p1, p2);
		Vec3D midPoint = line.getMidPoint();

		float dist = line.getLength();
		float startAngle = p1.sub(p2).headingXY();

		float radius = dist / 2;
		List<Vec3D> vertices = new ArrayList<Vec3D>();
		float step = PApplet.PI / NUM_STEPS;
		for (int i = 0; i < NUM_STEPS; i++) {
			float theta = startAngle + (upside ? (i * step) : (-i * step));
			vertices.add(Vec3D.fromXYTheta(theta).scaleSelf(radius).addSelf(midPoint));
		}

		return vertices;
	}

}
