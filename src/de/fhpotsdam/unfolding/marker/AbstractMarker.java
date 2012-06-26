package de.fhpotsdam.unfolding.marker;

import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import de.fhpotsdam.unfolding.utils.StyleConstants;

/**
 * Marker handling location to appropriate coordinate system. Also it provides the correct PGraphics.
 * 
 * The given x,y coordinates are already converted into the local coordinate system, so no need for further conversion.
 * 
 */
public abstract class AbstractMarker implements Marker {

	protected int color = StyleConstants.DEFAULT_FILL_COLOR;
	protected int strokeColor = StyleConstants.DEFAULT_STROKE_COLOR;
	protected int strokeWeight = StyleConstants.DEFAULT_STROKE_WEIGHT;
	protected int highlightColor = StyleConstants.HIGHLIGHTED_FILL_COLOR;
	protected int highlightStrokeColor = StyleConstants.HIGHLIGHTED_STROKE_COLOR;

	public Location location;
	public HashMap<String, Object> properties;
	public boolean selected;
	public String id;

	public AbstractMarker() {
		this(new Location(0, 0), null);
	}

	public AbstractMarker(Location location) {
		this(location, null);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AbstractMarker(Location location, HashMap<String, Object> props) {
		this.location = location;
		setProperties(props);
	}

	public void setProperties(HashMap<String, Object> props) {
		this.properties = props;
	}

	public HashMap<String, Object> getProperties() {
		return properties;
	}

	@Override
	// REVISIT rethink visibility of draw(Map)
	public void draw(UnfoldingMap map) {
		PGraphics pg = map.mapDisplay.getPG();
		float[] xy = map.mapDisplay.getInnerObjectFromLocation(getLocation());
		float x = xy[0];
		float y = xy[1];
		draw(pg, x, y, map);
	}

	@Override
	public void drawOuter(UnfoldingMap map) {
		PGraphics pg = map.mapDisplay.getOuterPG();
		float[] xy = map.mapDisplay.getObjectFromLocation(getLocation());
		float x = xy[0];
		float y = xy[1];
		drawOuter(pg, x, y, map);
	}

	/* override these methods to draw your marker dependent of map attributes */
	protected void draw(PGraphics pg, float x, float y, UnfoldingMap map) {
		draw(pg, x, y);
	}

	protected void drawOuter(PGraphics pg, float x, float y, UnfoldingMap map) {
		drawOuter(pg, x, y);
	}

	/**
	 * Checks whether given position is inside this marker, according to the maps coordinate system.
	 * 
	 * Uses internal implemented {@link #isInside(float, float, float, float)} of the sub class.
	 */
	@Override
	public boolean isInside(UnfoldingMap map, float checkX, float checkY) {
		PApplet.println("AbstractMarker.isInside(m, cx, cy)");
		ScreenPosition pos = getScreenPosition(map);
		return isInside(checkX, checkY, pos.x, pos.y);
	}

	public ScreenPosition getScreenPosition(UnfoldingMap map) {
		return map.mapDisplay.getScreenPosition(getLocation());
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public void setLocation(float lat, float lon) {
		setLocation(new Location(lat, lon));
	}

	@Override
	public double getDistanceTo(Location location) {
		return GeoUtils.getDistance(getLocation(), location);
	}

	/**
	 * Draws this marker in inner object coordinate system.
	 * 
	 * e.g. markers oriented to the tiles
	 * 
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
	 * Checks whether given position is inside the marker.
	 * 
	 * TODO Keep isInside(cx, cy, x, y) also for AbstractShapeMarker?
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

	/**
	 * Changes the select status of this marker. Sub-classes need to use the selection status.
	 * 
	 * @param selected
	 *            Whether this marker is selected or not.
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setStrokeWeight(int strokeWeight) {
		this.strokeWeight = strokeWeight;
	}

	public void setHighlightColor(int highlightColor) {
		this.highlightColor = highlightColor;
	}

	@Override
	public void setStrokeColor(int color) {
		this.strokeColor = color;
	}

}
