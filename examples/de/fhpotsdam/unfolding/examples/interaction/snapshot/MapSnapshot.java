package de.fhpotsdam.unfolding.examples.interaction.snapshot;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * Stores a thumbnail, and additional information of the current state of the map.
 */
public class MapSnapshot {

	private static final float THUMBNAIL_SCALE = 0.2f;

	protected PApplet p;

	/** Stored map location. */
	public Location location;
	/** Stored map zoom level. */
	public int zoomLevel;

	/** Thumbnail image of the map. */
	public PImage thumbnail;
	// Position and size of thumbnail.
	public float x;
	public float y;
	public float width;
	public float height;

	/**
	 * Creates a MapSnapshot.
	 * 
	 * @param p
	 *            The PApplet.
	 */
	public MapSnapshot(PApplet p) {
		this.p = p;
	}

	/**
	 * Creates a MapSnapshot and automatically snaps the current map.
	 * 
	 * @param p
	 *            The PApplet.
	 * @param map
	 *            The map to take a snapshot from.
	 */
	public MapSnapshot(PApplet p, UnfoldingMap map) {
		this.p = p;
		snapshot(map);
	}

	public void snapshot(UnfoldingMap map) {
		// Stores information of the current state of the map
		this.location = map.getCenter();
		this.zoomLevel = map.getZoomLevel();

		// Stores image data of the current map
		PGraphics pg = map.mapDisplay.getOuterPG();
		thumbnail = pg.get();
	}

	public void snapshot(UnfoldingMap map, int x, int y, int width, int height) {
		// Stores information of the current state of the map
		this.location = map.getCenter();
		this.zoomLevel = map.getZoomLevel();

		// Stores image data of the map area
		PGraphics pg = map.mapDisplay.getOuterPG();
		thumbnail = pg.get(x, y, width, height);
	}

	public void set(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void draw() {
		draw(x, y, width, height);
	}

	public void draw(float x, float y) {
		draw(x, y, thumbnail.width * THUMBNAIL_SCALE, thumbnail.height * THUMBNAIL_SCALE);
	}

	public void draw(float x, float y, float width, float height) {
		set(x, y, width, height);
		p.image(thumbnail, x, y, width, height);
	}

	public boolean isInside(float checkX, float checkY) {
		return checkX > x && checkX < x + width && checkY > y && checkY < y + height;
	}

}
