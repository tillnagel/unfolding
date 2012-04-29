package de.fhpotsdam.unfolding.texture;

import processing.core.PApplet;

public class PAppletFactory {

	private static PApplet p;

	public PAppletFactory(PApplet p) {
		PAppletFactory.p = p;
	}

	public static PApplet getInstance() {
		return p;
	}

}
