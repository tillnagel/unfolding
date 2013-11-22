package de.fhpotsdam.unfolding;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;

@Deprecated
public class Map extends UnfoldingMap {

	@Deprecated
	public Map(PApplet p) {
		super(p, generateId(), 0, 0, p.width, p.height, true, false, null, null);
	}

	@Deprecated
	public Map(PApplet p, AbstractMapProvider provider) {
		super(p, generateId(), 0, 0, p.width, p.height, true, false, provider, null);
	}

	@Deprecated
	public Map(PApplet p, float x, float y, float width, float height) {
		super(p, generateId(), x, y, width, height, true, false, null, null);
	}

	@Deprecated
	public Map(PApplet p, String id, float x, float y, float width, float height) {
		super(p, id, x, y, width, height, true, false, null, null);
	}

	@Deprecated
	public Map(PApplet p, String id, float x, float y, float width, float height, boolean useDistortion) {
		super(p, id, x, y, width, height, true, useDistortion, null, null);
	}

	@Deprecated
	public Map(PApplet p, float x, float y, float width, float height, AbstractMapProvider provider) {
		super(p, generateId(), x, y, width, height, true, false, provider, null);
	}
}
