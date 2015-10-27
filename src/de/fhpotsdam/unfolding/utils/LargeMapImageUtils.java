package de.fhpotsdam.unfolding.utils;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * Utility class to save large map images. Can be used to create high resolution screenshots for print. Everything
 * currently visible on the map (tiles and markers) will be in the image.
 * 
 * This process happens on-screen, thus the map can not be used while this tool is running. It will automatically pan to
 * different areas of the map, stitch them together, and save them in one single image.
 * 
 * To customize, set location, zoomLevel, and totalWidth and totalHeight. The latter two should be multiples of xStep
 * and yStep. You can also set file name, or file prefix and file suffix of the large image to be saved.
 * 
 * Please note the respective terms of service of the map provider!
 * 
 */
public class LargeMapImageUtils {

	protected PApplet p;

	protected int xStep = 500;
	protected int yStep = 500;
	protected int totalWidth = xStep * 2;
	protected int totalHeight = yStep * 2;

	int shotX = 0;
	int shotY = 0;

	UnfoldingMap map;
	PGraphics largeImage;
	boolean running = false;

	String imageFilePrefix = "largeMap-";
	String imageFileSuffix = ".png";
	int imageFileNumber = 0;
	String imageFileName = null;

	/**
	 * Creates a new LargeMapImageUtils which stores a large image double the size of the canvas.
	 * 
	 * @param p
	 *            The PApplet
	 * @param map
	 *            The map to take screenshots from.
	 */
	public LargeMapImageUtils(PApplet p, UnfoldingMap map) {
		this(p, map, 2);
	}

	public LargeMapImageUtils(PApplet p, UnfoldingMap map, int stitchNumber) {
		this(p, map, p.width, p.height, p.width * stitchNumber, p.height * stitchNumber);
	}

	public LargeMapImageUtils(PApplet p, UnfoldingMap map, Location location, int zoomLevel) {
		this(p, map, location, zoomLevel, 2);
	}

	public LargeMapImageUtils(PApplet p, UnfoldingMap map, Location location, int zoomLevel, int stitchNumber) {
		this(p, map, location, zoomLevel, p.width, p.height, p.width * stitchNumber, p.height * stitchNumber);
	}

	public LargeMapImageUtils(PApplet p, UnfoldingMap map, Location location, int zoomLevel, int xStep, int yStep,
			int totalWidth, int totalHeight) {
		this(p, map, xStep, yStep, totalWidth, totalHeight);
		init(location, zoomLevel);
	}

	public LargeMapImageUtils(PApplet p, UnfoldingMap map, int xStep, int yStep, int totalWidth, int totalHeight) {
		this.p = p;
		this.map = map;

		this.xStep = xStep;
		this.yStep = yStep;
		this.totalWidth = totalWidth;
		this.totalHeight = totalHeight;

		largeImage = p.createGraphics(totalWidth, totalHeight, PApplet.P2D);
	}

	/**
	 * Initializes a new large map image around the current map center. Pans to the upper left corner, and start the
	 * recording process.
	 */
	public void init() {
		running = true;
		shotX = 0;
		shotY = 0;
		PApplet.println("Init. running=" + running);

		// Set map to start position
		map.panBy(totalWidth / 2 - xStep / 2, totalHeight / 2 - yStep / 2);
	}

	/**
	 * Initializes a new large map image around the location. Pans to the upper left corner, and start the recording
	 * process.
	 * 
	 * @param location
	 *            The location to center around.
	 * @param zoomLevel
	 *            The zoom level to use.
	 */
	public void init(Location location, int zoomLevel) {
		map.zoomAndPanTo(zoomLevel, location);
		init();
	}

	/**
	 * Runs the recording process. It will automatically pan to different areas of the map, stitch them together, and
	 * save them in one single image, at the end.
	 */
	public void run() {
		if (map.allTilesLoaded() && running) {
			renderAndMakeSnapshot(shotX, shotY);

			shotX += xStep;
			if (shotX >= totalWidth) {
				shotX = 0;
				shotY += yStep;
				// Return back to the right, and go one down
				map.panBy(totalWidth - xStep, -yStep);
			} else {
				// Go left
				map.panBy(-xStep, 0);
			}

			if (shotY >= totalHeight) {
				saveLargeImage();
			}
		}
	}

	/**
	 * Internal method to save the actual large stitched-together image in one file.
	 */
	protected void saveLargeImage() {
		String fn = imageFileName;
		if (imageFileName == null) {
			fn = imageFilePrefix + imageFileNumber++ + imageFileSuffix;
		}
		PApplet.println("Saving large map image: " + fn);
		largeImage.save(fn);

		running = false;
	}

	/**
	 * Internal method to get the current map canvas (incl markers) and stores it in an buffer image.
	 * 
	 * @param shotX
	 *            Current x position of the large image to store the current screenshot to.
	 * @param shotY
	 *            Current y position of the large image to store the current screenshot to.
	 */
	protected void renderAndMakeSnapshot(int shotX, int shotY) {
		PApplet.println("Making snapshot for " + shotX + ", " + shotY);
		PImage currentImage = makeSnapshot();
		largeImage.beginDraw();
		largeImage.image(currentImage, shotX, shotY);
		largeImage.endDraw();
	}

	public PImage makeSnapshot() {
		return map.mapDisplay.getOuterPG();
	}

	/**
	 * Sets the prefix of the final large image file. Actual file name will be prefix + number + suffix.
	 * 
	 * @param imageFilePrefix
	 *            A image file prefix (supported by Processing, i.e. '.tif', '.png', '.jpg').
	 */
	public void setImageFilePrefix(String imageFilePrefix) {
		this.imageFilePrefix = imageFilePrefix;
	}

	/**
	 * Sets the suffix of the final large image file. Actual file name will be prefix + number + suffix.
	 * 
	 * @param imageFileSuffix
	 *            A image file suffix, e.g. 'largeImage-'.
	 */
	public void setImageFileSuffix(String imageFileSuffix) {
		this.imageFileSuffix = imageFileSuffix;
	}

	/**
	 * The full name of the final large image. If set, the LargeMapImageUtils won't automatically include a number.
	 * 
	 * @param imageFileName
	 *            A file name.
	 */
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

}
