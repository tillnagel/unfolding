package de.fhpotsdam.unfolding.examples.threed;

import java.util.ArrayList;
import java.util.List;

import processing.core.PVector;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class ArcsWithAnimatedParticlesMap3DApp extends Map3DApp {

	public static final int NUM_STEPS = 50;

	protected Location berlinLocation = new Location(52.5, 13.4);
	protected Location hamburgLocation = new Location(53.5505f, 9.993f);
	protected Location warsawLocation = new Location(52.2166f, 21.03333f);

	List<AnimatedParticle> animatedParticles = new ArrayList<AnimatedParticle>();
	List<AnimatedParticle> animatedParticles2 = new ArrayList<AnimatedParticle>();

	public void setup() {
		size(1024, 768, GLConstants.GLGRAPHICS);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(berlinLocation, 5);

		this.init3D();

		for (int i = 0; i < 10; i++) {
			animatedParticles.add(new AnimatedParticle(this));
			animatedParticles2.add(new AnimatedParticle(this));
		}
	}

	float rotateZValue = 0;

	public void draw() {
		background(0);

		pushMatrix();
		rotateX(0.7f);
		translate(0, -160, -100);
		translate(width/2, height/2);
		rotateZ(rotateZValue+=0.01f);
		translate(-width/2, -height/2);
		map.draw();

		mousePos = getMouse3D();

		ScreenPosition pos1 = map.getScreenPosition(berlinLocation);
		ScreenPosition pos2 = map.getScreenPosition(hamburgLocation);
		ScreenPosition pos4 = map.getScreenPosition(warsawLocation);

		noFill();
		stroke(20, 20, 200, 50);
		strokeWeight(2);
		// Draw full bezier
		drawBezier(pos1, pos2);
		drawBezier(pos1, pos4);

		// Animate sphere tweening over one bezier
		for (AnimatedParticle animatedParticle : animatedParticles) {
			animatedParticle.setPositions(pos1, pos2);
			animatedParticle.update();
			animatedParticle.draw();
		}
		for (AnimatedParticle animatedParticle : animatedParticles2) {
			animatedParticle.setPositions(pos1, pos4);
			animatedParticle.update();
			animatedParticle.draw();
		}

		popMatrix();

		fill(255);
		noStroke();
		rect(5, 5, 180, 20);
		fill(0);
		text("fps: " + nfs(frameRate, 0, 2), 10, 20);
	}

	public void drawBezier(PVector pos1, PVector pos2) {
		float height = pos1.dist(pos2);
		bezier(pos1.x, pos1.y, 0, pos1.x, pos1.y, height, pos2.x, pos2.y, height, pos2.x, pos2.y, 0);
	}

}
