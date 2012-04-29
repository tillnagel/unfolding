package de.fhpotsdam.unfolding.examples.interaction;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import codeanticode.glgraphics.GLGraphicsOffScreen;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;

public class MapSnapshot {

	private static final float THUMBNAIL_SCALE = 0.2f;

	private PApplet p;

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

	public MapSnapshot(PApplet p, Map map) {
		this.p = p;
		snapshot(map);
	}

	public void snapshot(Map map) {
		this.location = map.getCenter();
		this.zoomLevel = map.getZoomLevel();

		PGraphics pg = map.mapDisplay.getPG();
		if (pg instanceof GLGraphicsOffScreen) {
			thumbnail = new PImage((int) map.mapDisplay.getWidth(), (int) map.mapDisplay.getHeight());
			((GLGraphicsOffScreen) pg).getTexture().getImage(thumbnail);
		} else {
			thumbnail = pg.get();
		}
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
		p.fill(0, 100);
		p.noStroke();
		p.rect(x + 2, y + 2, width, height);
		p.image(thumbnail, x, y, width, height);
	}

	public boolean isInside(float checkX, float checkY) {
		return checkX > x && checkX < x + width && checkY > y && checkY < y + height;
	}

}
