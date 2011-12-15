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

	public Location location;
	public int zoomLevel;
	public PImage thumbnail;

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

	public void draw(float x, float y) {
		draw(x, y, thumbnail.width * THUMBNAIL_SCALE, thumbnail.height * THUMBNAIL_SCALE);
	}

	public void draw(float x, float y, float w, float h) {
		p.fill(0, 100);
		p.noStroke();
		p.rect(x + 2, y + 2, w, h);
		p.image(thumbnail, x, y, w, h);
	}

}
