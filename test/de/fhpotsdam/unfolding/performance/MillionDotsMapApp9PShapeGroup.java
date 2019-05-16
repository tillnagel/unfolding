package de.fhpotsdam.unfolding.performance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class MillionDotsMapApp9PShapeGroup extends PApplet {

	int dotNumber = 60000;

	UnfoldingMap map;
	// Original dots (loc + time)
	List<Dot> dots = new ArrayList<Dot>();

	PShape shapeGroup;

	public void settings() {
		size(800, 600, P2D);

	}

	public void setup() {
		// smooth();

		dots = createRandomDots(dotNumber);

		map = new UnfoldingMap(this);
		map.zoomToLevel(3);
		MapUtils.createDefaultEventDispatcher(this, map);

		shapeGroup = createShapeGroup();
		updateShapeGroup();
	}

	public PShape createShapeGroup() {
		shapeGroup = createShape(PShape.GROUP);
		for (int i = 0; i < dotNumber; i++) {
			PShape shape = createShape();
			shape.beginShape(QUAD);
			shape.noStroke();
			shape.fill(255, 0, 0, 100);
			PVector pos = new PVector();
			shape.vertex(pos.x, pos.y);
			shape.vertex(pos.x + 4, pos.y);
			shape.vertex(pos.x + 4, pos.y + 4);
			shape.vertex(pos.x, pos.y + 4);
			shape.endShape();
			shapeGroup.addChild(shape);
		}
		return shapeGroup;
	}

	public void draw() {
		background(0);
		map.draw();

		updateShapeGroup();

		shape(shapeGroup);

		fill(255);
		rect(5, 5, 180, 20);
		fill(0);
		text("fps: " + nfs(frameRate, 0, 2) + " (" + dotNumber + " dots)", 10, 20);
	}

	public void updateShapeGroup() {
		for (int i = 0; i < shapeGroup.getChildCount(); i++) {
			PShape shape = shapeGroup.getChild(i);
			PVector pos = map.getScreenPosition(dots.get(i).location);
			shape.setVertex(0, pos.x, pos.y);
			shape.setVertex(1, pos.x - 4, pos.y);
			shape.setVertex(2, pos.x - 4, pos.y - 4);
			shape.setVertex(3, pos.x, pos.y - 4);
		}
	}

	private List<Dot> createRandomDots(int dotNumbers) {
		List<Dot> dots = new ArrayList<Dot>();
		for (int i = 0; i < dotNumbers; i++) {
			Dot dot = new Dot(new Location(random(-85, 85), random(-180, 180)), new Date());
			dots.add(dot);
		}
		return dots;
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { MillionDotsMapApp9PShapeGroup.class.getName() });
	}
}
