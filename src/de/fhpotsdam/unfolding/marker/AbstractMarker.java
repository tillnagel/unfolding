package de.fhpotsdam.unfolding.marker;

import java.util.HashMap;

import processing.core.PGraphics;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import de.fhpotsdam.unfolding.utils.StyleConstants;

/**
 * Abstract marker handling conversion of location to appropriate coordinate system, and handles basic marker
 * functionality.
 * 
 * All default marker of Unfolding implement this. Your own class can extend this AbstractMarker or implement the Marker
 * interface, directly.
 */
public abstract class AbstractMarker implements Marker {

	protected int color = StyleConstants.DEFAULT_FILL_COLOR;
	protected int strokeColor = StyleConstants.DEFAULT_STROKE_COLOR;
	protected int strokeWeight = StyleConstants.DEFAULT_STROKE_WEIGHT;
	protected int highlightColor = StyleConstants.HIGHLIGHTED_FILL_COLOR;
	protected int highlightStrokeColor = StyleConstants.HIGHLIGHTED_STROKE_COLOR;

	/** The location of this marker. */
	protected Location location;
	/** Optional data properties. */
	protected HashMap<String, Object> properties;
	/** Indicates whether this marker is selected. */
	protected boolean selected;
	/** Indicates whether this marker is hidden. */
	protected boolean hidden;
	/** The ID of this marker. */
	protected String id;

	public AbstractMarker() {
		this(new Location(0, 0), null);
	}

	public AbstractMarker(Location location) {
		this(location, null);
	}

	public AbstractMarker(Location location, HashMap<String, Object> props) {
		this.location = location;
		setProperties(props);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void setProperties(HashMap<String, Object> props) {
		this.properties = props;
	}

	@Override
	public Object setProperty(String key, Object value) {
		return properties.put(key, value);
	}
	
	@Override
	public HashMap<String, Object> getProperties() {
		return properties;
	}

	@Override
	public Object getProperty(String key) {
		return properties.get(key);
	}
	
	@Override
	public String getStringProperty(String key) {
		Object value = properties.get(key);
		if (value != null && value instanceof String) {
			return (String) value;
		} else {
			return null;
		}
	}

	@Override
	public Integer getIntegerProperty(String key) {
		Object value = properties.get(key);
		if (value != null && value instanceof Integer) {
			return (Integer) value;
		} else {
			return null;
		}
	}

	/**
	 * Draws this marker onto the map. Converts the geo-location to object position, and calls
	 * {@link #draw(PGraphics, float, float, UnfoldingMap)}.
	 */
	@Override
	public void draw(UnfoldingMap map) {
		PGraphics pg = map.mapDisplay.getOuterPG();
		float[] xy = map.mapDisplay.getObjectFromLocation(getLocation());
		float x = xy[0];
		float y = xy[1];
		draw(pg, x, y, map);
	}

	/**
	 * Draws a visual representation of this marker. The given x,y coordinates are already converted into the local
	 * coordinate system, so no need for further conversion. That is, a position of (0, 0) is the origin of this marker.
	 * 
	 * Subclasses <em>may</em> override this method to draw a marker depending on map properties.
	 * 
	 * @param pg
	 *            The PGraphics to draw on.
	 * @param x
	 *            The x position in object coordinates.
	 * @param y
	 *            The y position in object coordinates.
	 * @param map
	 *            The map to draw on. Can be used to draw a marker which depends on other properties of the map.
	 */
	protected void draw(PGraphics pg, float x, float y, UnfoldingMap map) {
		draw(pg, x, y);
	}

	/**
	 * Draws a visual representation of this marker. The given x,y coordinates are already converted into the local
	 * coordinate system, so no need for further conversion. That is, a position of (0, 0) is the origin of this marker.
	 * 
	 * Subclasses <em>must</em> override this method to draw a marker.
	 * 
	 * @param pg
	 *            The PGraphics to draw on
	 * @param x
	 *            The x position in outer object coordinates.
	 * @param y
	 *            The y position in outer object coordinates.
	 */
	public abstract void draw(PGraphics pg, float x, float y);

	/**
	 * Checks whether given position is inside this marker, according to the maps coordinate system.
	 * 
	 * Uses internal implemented {@link #isInside(float, float, float, float)} of the sub class.
	 */
	@Override
	public boolean isInside(UnfoldingMap map, float checkX, float checkY) {
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

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	@Override
	public boolean isHidden() {
		return hidden;
	}

	@Override
	public void setColor(int color) {
		this.color = color;
	}

	@Override
	public void setStrokeWeight(int strokeWeight) {
		this.strokeWeight = strokeWeight;
	}

	public void setHighlightColor(int highlightColor) {
		this.highlightColor = highlightColor;
	}

	public void setHighlightStrokeColor(int highlightStrokeColor) {
		this.highlightStrokeColor = highlightStrokeColor;
	}

	@Override
	public void setStrokeColor(int color) {
		this.strokeColor = color;
	}

}
