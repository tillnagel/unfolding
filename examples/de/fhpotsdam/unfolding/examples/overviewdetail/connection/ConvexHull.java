package de.fhpotsdam.unfolding.examples.overviewdetail.connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class ConvexHull {

	final int POINT_SIZE = 10;
	final float HULL_SIZE = POINT_SIZE * 0.2f;

	public int pointSize = POINT_SIZE;
	public float hullSize = HULL_SIZE;
	public int pointColor = 0;
	public int hullStrokeColor = 0;
	public int hullFillColor = -256;

	public boolean showDebugPoints = false;

	protected PApplet p;

	List<PVector> points;

	public ConvexHull(PApplet p) {
		this.p = p;
		points = new ArrayList<PVector>();
	}

	public void draw() {
		drawHull(buildHull(points));

		if (showDebugPoints) {
			for (int i = 0; i < points.size(); i++) {
				drawPoint(points.get(i));
			}
		}
	}

	public void addPoint(PVector point) {
		points.add(point);
	}

	public void clearPoints() {
		points.clear();
	}

	// builds a convex hull around the given points
	// using the Graham scan algorithm
	public static List<PVector> buildHull(List<PVector> points) {
		if (points.size() < 3)
			return null;

		// Find the point with the least y, then x coordinate
		PVector p0 = null;
		for (int i = 0; i < points.size(); ++i) {
			PVector curr = points.get(i);
			if (p0 == null || curr.y < p0.y || (curr.y == p0.y && curr.x < p0.x))
				p0 = curr;
		}

		// Sort the points by angle around p0
		class PointAngleComparator implements Comparator<PVector> {
			private PVector p0;

			public PointAngleComparator(PVector p0) {
				this.p0 = p0;
			}

			private float angle(PVector pt) {
				return PApplet.atan2(pt.y - p0.y, pt.x - p0.x);
			}

			public int compare(PVector p1, PVector p2) {
				float a1 = angle(p1), a2 = angle(p2);
				if (a1 > a2)
					return 1;
				if (a1 < a2)
					return -1;
				return Float.compare(PApplet.dist(p0.x, p0.y, p1.x, p1.y),
						PApplet.dist(p0.x, p0.y, p2.x, p2.y));
			}
		}
		Collections.sort(points, new PointAngleComparator(p0));

		// build the hull
		Stack<PVector> hull = new Stack<PVector>();
		hull.push(points.get(0));
		hull.push(points.get(1));
		hull.add(points.get(2));
		for (int i = 3; i < points.size(); ++i) {
			PVector cur = points.get(i);
			while (hull.size() >= 3) {
				PVector snd = hull.get(hull.size() - 2);
				PVector top = hull.peek();
				float crossproduct = (top.x - snd.x) * (cur.y - snd.y) - (cur.x - snd.x)
						* (top.y - snd.y);
				if (crossproduct > 0)
					break;
				hull.pop();
			}
			hull.push(cur);
		}

		return hull;
	}

	protected void drawPoint(PVector pt) {
		p.stroke(pointColor);
		p.strokeWeight(pointSize);
		p.point(pt.x, pt.y);
	}

	protected void drawHull(List<PVector> hull) {
		if (hull == null)
			return;

		p.fill(hullFillColor);
		p.stroke(hullStrokeColor);
		p.strokeWeight(hullSize);

		p.beginShape();
		for (int i = 0; i < hull.size(); ++i) {
			PVector pt = (PVector) hull.get(i);
			p.vertex(pt.x, pt.y);
		}
		p.endShape(PConstants.CLOSE);
	}

}
