package de.fhpotsdam.unfolding.examples.interaction.snapshot;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import de.fhpotsdam.unfolding.UnfoldingMap;

/**
 * Stores a circular thumbnail of the current map, and current map status data (from MapSnapshot).
 */
public class CircularMapSnapshot extends MapSnapshot {

	private static final int DEFAULT_FEATHER = 5;
	private static final int DEFAULT_RADIUS = 200;

	protected int radius;
	protected int feather;

	public CircularMapSnapshot(PApplet p, UnfoldingMap map) {
		this(p, map, DEFAULT_RADIUS, DEFAULT_FEATHER);
	}

	public CircularMapSnapshot(PApplet p, UnfoldingMap map, int radius, int feather) {
		this(p);
		this.radius = radius;
		this.feather = feather;
		snapshot(map);
	}

	public CircularMapSnapshot(PApplet p) {
		this(p, DEFAULT_RADIUS, DEFAULT_FEATHER);
	}

	public CircularMapSnapshot(PApplet p, int radius, int feather) {
		super(p);
		this.radius = radius;
		this.feather = feather;
	}

	@Override
	public void snapshot(UnfoldingMap map) {
		super.snapshot(map);

		thumbnail = getCircularImage(thumbnail, radius, feather);
	}

	@Override
	public void snapshot(UnfoldingMap map, int x, int y, int width, int height) {
		super.snapshot(map, x, y, width, height);

		thumbnail = getCircularImage(thumbnail, radius, feather);
	}

	// By amnon.owed, http://forum.processing.org/topic/extract-circle-texture-from-background-with-alpha-channel
	public PImage getCircularImage(PImage img, int radius, int feather) {
		PGraphics temp = p.createGraphics(img.width, img.height, PConstants.JAVA2D);
		temp.beginDraw();
		temp.smooth();
		temp.translate(temp.width / 2, temp.height / 2);
		temp.imageMode(PConstants.CENTER);
		temp.image(img, 0, 0);
		temp.endDraw();
		PImage saveArea = p.createImage(temp.width, temp.height, PConstants.ARGB);
		for (int y = 0; y < saveArea.height; y++) {
			for (int x = 0; x < saveArea.width - 1; x++) {
				int index = x + y * saveArea.width; // Fixed. Original code by amon.owed only worked for squares
				float d = PApplet.dist(x, y, radius, radius);
				if (d > radius) {
					saveArea.pixels[index] = 0;
				} else if (d >= radius - feather) {
					int c = temp.pixels[index];
					int r = (c >> 16) & 0xff;
					int g = (c >> 8) & 0xff;
					int b = (c) & 0xff;
					c = p.color(r, g, b, PApplet.map(d, radius - feather, radius, 255, 0));
					saveArea.pixels[index] = c;
				} else {
					saveArea.pixels[index] = temp.pixels[index];
				}
			}
		}
		saveArea.updatePixels();
		return saveArea;
	}

}
