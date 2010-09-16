package de.fhpotsdam.unfolding;

import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import processing.core.PApplet;

public class MapContainer extends Map {

	public MapContainer(PApplet p, String id) {
		super(p, id);
	}

	public MapContainer(PApplet p, String id, float x, float y, float width, float height,
			boolean useMask, boolean useDistortion, AbstractMapProvider provider) {
		super(p, id, x, y, width, height, useMask, useDistortion, provider);
	}

	public MapContainer(PApplet p, String id, float x, float y, float width, float height,
			boolean useDistortion) {
		super(p, id, x, y, width, height, useDistortion);
	}

	public MapContainer(PApplet p, String id, float x, float y, float width, float height) {
		super(p, id, x, y, width, height);
	}
  
	
	
}
