package de.fhpotsdam.unfolding.providers;

import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.AbstractProjection;

public abstract class AbstractMapTileProvider extends AbstractMapProvider {

	public AbstractMapTileProvider(AbstractProjection projection) {
		super(projection);
	}

	@Override
	public String[] getTileUrls(Coordinate coordinate) {
		return null;
	}

}
