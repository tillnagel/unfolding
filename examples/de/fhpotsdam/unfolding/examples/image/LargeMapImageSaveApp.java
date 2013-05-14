package de.fhpotsdam.unfolding.examples.image;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import codeanticode.glgraphics.GLConstants;
import codeanticode.glgraphics.GLGraphicsOffScreen;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Creates large map images, stitched together from multiple tiles. Saves a single large image with a map around the
 * given location at the given zoom level. Initializes map, makes screen-shot, pans automatically, and repeats until it
 * is finished.
 * 
 * To customize, set location, zoomLevel, and totalWidth and totalHeight. The latter two should be multiples of xStep
 * and yStep. Please note the respective terms of service of the map provider.
 * 
 */
public class LargeMapImageSaveApp extends PApplet {

	// Set to the center location you want to grab the map for.
	Location location = new Location(52.5f, 13.4f);
	// Set to zoom level you want to grab.
	int zoomLevel = 9;

	int xStep = 500;
	int yStep = 500;
	int totalWidth = xStep * 4;
	int totalHeight = yStep * 4;

	int shotX = 0;
	int shotY = 0;
	UnfoldingMap map;
	PGraphics largeImage;
	boolean tilesLoaded = false;

	public void setup() {
		size(xStep, yStep, GLConstants.GLGRAPHICS);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(location, zoomLevel);
		MapUtils.createDefaultEventDispatcher(this, map);

		println("Init large map image.");
		largeImage = createGraphics(totalWidth, totalHeight, P2D);

		map.panBy(totalWidth / 2 - xStep / 2, totalHeight / 2 - yStep / 2);
	}

	public void draw() {
		map.draw();

		if (tilesLoaded) {
			renderAndMakeSnapshot(shotX, shotY);

			shotX += xStep;
			if (shotX >= totalWidth) {
				shotX = 0;
				shotY += yStep;
				// Return back to the right, and go one down
				map.panBy(totalWidth - xStep, -yStep);
			}
			else {
				// Go left
				map.panBy(-xStep, 0);
			}

			if (shotY >= totalHeight) {
				saveLargeImage();
				exit();
			}

			tilesLoaded = false;
		}
	}

	public void saveLargeImage() {
		largeImage.save("largeMap.png");
		println("Saving large map image.");
	}

	public void renderAndMakeSnapshot(int shotX, int shotY) {
		println("Making snapshot for " + shotX + ", " + shotY);
		PImage currentImage = makeSnapshot();
		largeImage.image(currentImage, shotX, shotY);
	}

	public void tilesLoaded() {
		println("All tiles loaded.");
		tilesLoaded = true;
	}

	public PImage makeSnapshot() {
		return makeSnapshot((int) map.mapDisplay.getWidth(), (int) map.mapDisplay.getHeight());
	}

	public PImage makeSnapshot(int width, int height) {
		PImage thumbnail;

		PGraphics pg = map.mapDisplay.getInnerPG();
		if (pg instanceof GLGraphicsOffScreen) {
			println("Creating new thumbnail");
			thumbnail = new PImage(width, height);
			((GLGraphicsOffScreen) pg).getTexture().getImage(thumbnail);
		} else {
			thumbnail = pg.get();
		}

		return thumbnail;
	}
}
