package de.fhpotsdam.unfolding.marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import processing.core.PApplet;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapPosition;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public abstract class AbstractShapeMarker extends AbstractMarker {

	protected List<Location> locations;

	public AbstractShapeMarker() {
		this(new ArrayList<Location>(), null);
	}

	public AbstractShapeMarker(List<Location> locations) {
		this(locations, null);
	}

	public AbstractShapeMarker(List<Location> locations, HashMap<String, Object> properties) {
		this.locations = locations;
		setProps(properties);
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void addLocations(Location... locations) {
		for (Location loc : locations) {
			this.locations.add(loc);
		}
	}

	public void addLocations(List<Location> locations) {
		this.locations.addAll(locations);
	}

	public Location getLocation(int index) {
		return locations.get(index);
	}

	public void removeLocation(Location location) {
		locations.remove(location);
	}

	public void removeLocation(int index) {
		locations.remove(index);
	}

	public void draw(Map map) {
		super.draw(map);

		PGraphics pg = map.mapDisplay.getPG();

		List<MapPosition> mapPositions = new ArrayList<MapPosition>();

		for (Location loc : getLocations()) {
			float[] xy = map.mapDisplay.getInnerObjectFromLocation(loc);
			mapPositions.add(new MapPosition(xy));
		}

		draw(pg, mapPositions, properties, map);
	}

	@Override
	public void drawOuter(Map map) {
		super.drawOuter(map);

		PGraphics pg = map.mapDisplay.getOuterPG();

		List<MapPosition> mapPositions = new ArrayList<MapPosition>();

		for (Location loc : getLocations()) {
			float[] xy = map.mapDisplay.getObjectFromLocation(loc);
			mapPositions.add(new MapPosition(xy));
		}

		drawOuter(pg, mapPositions, properties, map);
	}

	/* override these methods to draw your marker dependent of properties and map attributes */
	protected void draw(PGraphics pg, List<MapPosition> mapPositions, HashMap<String, Object> properties, Map map) {
		draw(pg, mapPositions);
	}

	protected void drawOuter(PGraphics pg, List<MapPosition> mapPositions, HashMap<String, Object> properties, Map map) {
		drawOuter(pg, mapPositions);
	}

	/**
	 * Draws these markers in inner object coordinate system.
	 * 
	 * e.g. for labels oriented to the map
	 * 
	 * @param pg
	 *            The PGraphics to draw on
	 * @param objectPositions
	 *            The positions in inner object coordinates.
	 */

	public abstract void draw(PGraphics pg, List<MapPosition> objectPositions);

	/**
	 * Draws these markers in outer object coordinate system.
	 * 
	 * e.g. for labels oriented to the map
	 * 
	 * @param pg
	 *            The PGraphics to draw on
	 * @param objectPositions
	 *            The positions in outer object coordinates.
	 */
	public abstract void drawOuter(PGraphics pg, List<MapPosition> objectPositions);

	// REVISIT default behavior for getLocation(), draw(location),
	// drawOuter(location)?

	@Override
	public void draw(PGraphics pg, float x, float y) {
	}

	@Override
	public void drawOuter(PGraphics pg, float x, float y) {
	}

	@Override
	public boolean isInside(Map map, float checkX, float checkY) {
		PApplet.println("AbstractShape.isInside(m, cx, cy)");
		List<ScreenPosition> positions = new ArrayList<ScreenPosition>();
		for (Location location : locations) {
			ScreenPosition pos = map.getScreenPosition(location);
			positions.add(pos);
		}
		return isInside(checkX, checkY, positions);
	}

	protected boolean isInside(float checkX, float checkY, List<ScreenPosition> positions) {
		PApplet.println("AbstractShape.isInside(cx, cy, positions)");
		boolean inside = false;
		for (int i = 0, j = positions.size() - 1; i < positions.size(); j = i++) {
			ScreenPosition pi = positions.get(i);
			ScreenPosition pj = positions.get(j);
			if ((((pi.y <= checkY) && (checkY < pj.y)) || ((pj.y <= checkY) && (checkY < pi.y)))
					&& (checkX < (pj.x - pi.x) * (checkY - pi.y) / (pj.y - pi.y) + pi.x)) {
				inside = !inside;
			}
		}
		return inside;
	}

	@Override
	protected boolean isInside(float checkX, float checkY, float x, float y) {
		// TODO Simply return false?
		throw new RuntimeException("Check for a single positon is not implemented for polygons.");
	}

}
