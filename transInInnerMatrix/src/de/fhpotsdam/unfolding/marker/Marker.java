package de.fhpotsdam.unfolding.marker;

import processing.core.PGraphics;
import processing.core.PVector;
import de.fhpotsdam.unfolding.geo.Location;

public interface Marker {
	
	public Location getLocation();
	
	public void draw();

	public void draw(PGraphics pg);
	
	public void update(PVector v);
	
}
