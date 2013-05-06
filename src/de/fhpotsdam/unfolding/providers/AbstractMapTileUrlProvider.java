package de.fhpotsdam.unfolding.providers;

import processing.core.PImage;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.AbstractProjection;

/**
 * Handles tiles from URLs, such as web map services, etc.
 */
public abstract class AbstractMapTileUrlProvider extends AbstractMapProvider {

	public AbstractMapTileUrlProvider(AbstractProjection projection) {
		super(projection);
	}

	@Override
	public PImage getTile(Coordinate coordinate) {
		return null;
	}

}
