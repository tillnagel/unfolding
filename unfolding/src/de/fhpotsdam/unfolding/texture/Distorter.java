package de.fhpotsdam.unfolding.texture;

import processing.core.PVector;

public interface Distorter {

	public void distort(PVector origCoord, PVector distCoord);

	public void distort(PVector origCoord, PVector distCoord, int col);

}
