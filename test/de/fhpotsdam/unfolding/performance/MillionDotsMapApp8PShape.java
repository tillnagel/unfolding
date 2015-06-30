package de.fhpotsdam.unfolding.performance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.MapEvent;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class MillionDotsMapApp8PShape extends PApplet {

	int dotNumber = 2000;

	UnfoldingMap map;
	// Original dots (loc + time)
	List<Dot> dots = new ArrayList<Dot>();

	List<PShape> shapes = new ArrayList<PShape>();

	public void setup() {
		size(800, 600, P2D);
		// smooth();

		dots = createRandomDots(dotNumber);

		map = new UnfoldingMap(this);
		map.zoomToLevel(3);
		MapUtils.createDefaultEventDispatcher(this, map);

		shapes = createShapes();

	}

	public List<PShape> createShapes() {
		List<PShape> shapes = new ArrayList<PShape>();
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
			shapes.add(shape);
		}
		return shapes;
	}

	public void draw() {
		background(0);
		map.draw();
		
		for (PShape shape : shapes) {
			shape(shape);
		}

		fill(255);
		rect(5, 5, 180, 20);
		fill(0);
		text("fps: " + nfs(frameRate, 0, 2) + " (" + dotNumber + " dots)", 10, 20);
	}

	public void mapChanged(MapEvent mapEvent) {
		// Check map area only once after user interaction.

		int i = 0;
		for (PShape shape : shapes) {
			PVector pos = map.getScreenPosition(dots.get(i).location);
			shape.setVertex(0, pos.x, pos.y);
			shape.setVertex(1, pos.x - 4, pos.y);
			shape.setVertex(2, pos.x - 4, pos.y - 4);
			shape.setVertex(3, pos.x, pos.y - 4);
			//shape.translate(random(-1, 1), random(-1,1));
			i++;
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
}
