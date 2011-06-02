package de.fhpotsdam.unfolding.marker;

import processing.core.PGraphics;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * Marker handling location to appropriate coordinate system. Also it provides the correct
 * PGraphics.
 * 
 */
public abstract class AbstractMarker implements Marker {

	@Override
	public void draw(Map map) {
		PGraphics pg = map.mapDisplay.getPG();
		float[] xy = map.mapDisplay.getInnerObjectFromLocation(getLocation());
		float x = xy[0];
		float y = xy[1];
		draw(pg, x, y);
	}

	@Override
	public void drawOuter(Map map) {
		PGraphics pg = map.mapDisplay.getOuterPG();
		float[] xy = map.mapDisplay.getObjectFromLocation(getLocation());
		float x = xy[0];
		float y = xy[1];
		drawOuter(pg, x, y);
	}

	/**
	 * Checks whether given position is inside this marker, according to the maps coordinate system.
	 * 
	 * Uses internal implemented {@link #isInside(float, float, float, float)} of the sub class.
	 */
	@Override
	public boolean isInside(Map map, float checkX, float checkY) {
		float[] xy = map.mapDisplay.getScreenPositionFromLocation(getLocation());
		return isInside(checkX, checkY, xy[0], xy[1]);
	}

	@Override
	public abstract Location getLocation();

	/**
	 * Draws this marker in inner object coordinate system.
	 * 
	 * e.g. markers oriented to the tiles
	 * 
	 * @param pg
	 *            The PGraphics to draw on
	 * @param x
	 *            The x position in inner object coordinates.
	 * @param y
	 *            The y position in inner object coordinates.
	 */
	public abstract void draw(PGraphics pg, float x, float y);

	/**
	 * Draws this marker in outer object coordinate system.
	 * 
	 * e.g. for labels oriented to the map
	 * 
	 * @param pg
	 *            The PGraphics to draw on
	 * @param x
	 *            The x position in outer object coordinates.
	 * @param y
	 *            The y position in outer object coordinates.
	 */
	public abstract void drawOuter(PGraphics pg, float x, float y);

	/**
	 * Checks whether given position is inside the map.
	 * 
	 * @param checkX
	 *            The x position to check in screen coordinates.
	 * @param checkY
	 *            The y position to check in screen coordinates.
	 * @param x
	 *            The x position of this marker in screen coordinates.
	 * @param y
	 *            The y position of this marker in screen coordinates.
	 * @return true if inside, false otherwise.
	 */
	protected abstract boolean isInside(float checkX, float checkY, float x, float y);
}
