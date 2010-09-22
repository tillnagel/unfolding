package de.fhpotsdam.unfolding.marker;

import processing.core.PGraphics;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * Marker interface for all markers to be drawn on to maps.
 * 
 */
public interface Marker {

	/**
	 * Gets the location of this marker.
	 * 
	 * @return The location with lat, lng.
	 */
	public Location getLocation();

	/**
	 * Checks whether given position is inside the map.
	 * 
	 * @param map
	 *            The map to test for.
	 * @param checkX
	 *            The x position in screen coordinates.
	 * @param checkY
	 *            The y position in screen coordinates.
	 * @return true if inside, false otherwise.
	 */
	public boolean isInside(Map map, float checkX, float checkY);

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
	public void draw(PGraphics pg, float x, float y);

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
	public void drawOuter(PGraphics pg, float x, float y);

	// For drawing onto the texture, i.e. after distortion, etc.
	// public void drawTexture(PGraphics pg);

}
