package de.fhpotsdam.unfolding.providers;

import processing.core.PConstants;
import processing.core.PImage;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.MercatorProjection;
import de.fhpotsdam.unfolding.geo.Transformation;

/**
 * Returns empty images. Can be used to show no map, but still manages markers, etc.
 */
public class EmptyMapProvider extends AbstractMapTileProvider {

	private PImage cachedEmpyImage = null;

	public EmptyMapProvider() {
		super(new MercatorProjection(26, new Transformation(1.068070779e7, 0.0, 3.355443185e7, 0.0, -1.068070890e7,
				3.355443057e7)));
		cachedEmpyImage = new PImage(tileWidth(), tileHeight(), PConstants.ARGB);
	}

	@Override
	public PImage getTile(Coordinate coordinate) {
		return cachedEmpyImage;
	}

	@Override
	public int tileWidth() {
		return 256;
	}

	@Override
	public int tileHeight() {
		return 256;
	}

}
