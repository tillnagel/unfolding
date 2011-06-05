package de.fhpotsdam.unfolding.providers;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.AbstractProjection;
import de.fhpotsdam.unfolding.geo.Location;

public abstract class AbstractMapProvider {

	public static String[] ids = { "MICROSOFT_ROAD", "MICROSOFT_AERIAL", "MICROSOFT_HYBRID", "YAHOO_ROAD",
			"YAHOO_AERIAL", "YAHOO_HYBRID", "BLUE_MARBLE", "OPEN_STREET_MAP" };

	public AbstractProjection projection;

	public AbstractMapProvider(AbstractProjection projection) {
		this.projection = projection;
	}

	/**
	 * Gets tile URLs for coordinate. May return multiple URLs, if provider handles multiple layers.
	 * 
	 * @param coordinate
	 *            The position and zoom to get tile for.
	 * @return An array with tile URLs (mostly just one)
	 */
	public abstract String[] getTileUrls(Coordinate coordinate);

	public abstract int tileWidth();

	public abstract int tileHeight();

	public Coordinate locationCoordinate(Location location) {
		return projection.locationCoordinate(location);
	}

	public Location coordinateLocation(Coordinate coordinate) {
		return projection.coordinateLocation(coordinate);
	}

	public Coordinate sourceCoordinate(Coordinate coordinate) {
		float gridSize = PApplet.pow(2, coordinate.zoom);

		float wrappedColumn = coordinate.column % gridSize;
		while (wrappedColumn < 0) {
			wrappedColumn += gridSize;
		}

		float wrappedRow = coordinate.row % gridSize;
		while (wrappedRow < 0) {
			wrappedRow += gridSize;
		}

		return new Coordinate(wrappedRow, wrappedColumn, coordinate.zoom);
	}

	/** since we're often given four tile servers to pick from */
	public static float random(int lower, int higher) {
		return (float) ((double) lower + Math.random() * (double) (higher - lower));
	}

}
