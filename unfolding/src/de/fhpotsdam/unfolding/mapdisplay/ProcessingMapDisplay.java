package de.fhpotsdam.unfolding.mapdisplay;

import java.util.Collections;
import java.util.Vector;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PMatrix2D;
import processing.core.PMatrix3D;
import processing.core.PVector;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;

public class ProcessingMapDisplay extends AbstractMapDisplay implements PConstants {

	private static final boolean SHOW_DEBUG_BORDER = true;
	private static final boolean USE_DEBUG_TILES = true;
	private static final boolean USE_OFFLINE_MODE = false;


	// Used for loadImage and float maths
	public PApplet papplet;

	/** The transformation matrix. */
	protected PMatrix3D matrix = new PMatrix3D();
	/** Used internally to not subtract the offset translation in first frame. */
	private boolean firstMatrixCalc = true;

	private PFont font;

	/**
	 * Creates a new MapDisplay with full canvas size, and given provider
	 */
	public ProcessingMapDisplay(PApplet papplet, AbstractMapProvider provider) {
		this(papplet, provider, 0, 0, papplet.width, papplet.height);
	}

	/**
	 * Creates a new MapDisplay.
	 */
	public ProcessingMapDisplay(PApplet papplet, AbstractMapProvider provider, float offsetX,
			float offsetY, float width, float height) {
		super(provider, width, height);
		this.papplet = papplet;
		this.offsetX = offsetX;
		this.offsetY = offsetY;

		font = papplet.loadFont("Miso-Light-12.vlw");

		calculateMatrix();
	}

	// TRANSFORMATION --------------------------------------------------

	/**
	 * Updates the matrix to transform the map with.
	 * 
	 * For the rotation the matrix has to be temporarily translated to the transformation center.
	 * Thus, it has to be reset with the original position, afterwards. Original position is
	 * calculated by inverting the current matrix. (As the matrix incorporates that position, it
	 * stores every transformation, even though the matrix is created anew.)
	 * 
	 * @param x
	 *            The transformation center x
	 * @param y
	 *            The transformation center y
	 */
	public void calculateMatrix(float x, float y) {
		PMatrix3D invMatrix = new PMatrix3D();
		if (!firstMatrixCalc) {
			matrix.translate(-offsetX, -offsetY);
		} else {
			firstMatrixCalc = false;
		}
		invMatrix.apply(matrix);
		invMatrix.invert();
		float origX = invMatrix.multX(x, y);
		float origY = invMatrix.multY(x, y);

		matrix = new PMatrix3D();
		matrix.translate(x, y);
		matrix.scale(scale);
		matrix.rotate(angle);
		matrix.translate(-origX, -origY);
		matrix.translate(offsetX, offsetY);
	}

	/**
	 * Updates the matrix to transform the map with with the current transformation center.
	 * 
	 * @see #calculateMatrix(float, float)
	 */
	@Override
	public void calculateMatrix() {
		calculateMatrix(transformationCenter.x, transformationCenter.y);
	}

	@Override
	public float[] getTransformedPosition(float x, float y, boolean inverse) {
		float[] preXY = new float[3];
		PMatrix3D m = new PMatrix3D();
		m.apply(matrix);
		if (inverse) {
			m.invert();
		}
		m.mult(new float[] { x, y, 0 }, preXY);
		return preXY;
	}

	@Override
	public Location getCenterLocation() {
		return getInternalLocationForPoint(width / 2, height / 2);
	}

	@Override
	public Location getLocationForPoint(float x, float y) {
		float transPoint[] = getTransformedPosition(x, y, true);
		x = transPoint[0];
		y = transPoint[1];
		return getInternalLocationForPoint(x, y);
	}

	private Location getInternalLocationForPoint(float x, float y) {
		PMatrix2D m = getInternalTransformationMatrix();

		// Find top left and bottom right positions of mapDisplay in screenspace:
		float tl[] = new float[2];
		m.mult(new float[] { 0, 0 }, tl);
		float br[] = new float[2];
		m.mult(new float[] { TILE_WIDTH, TILE_HEIGHT }, br);

		float col = (x - tl[0]) / (br[0] - tl[0]);
		float row = (y - tl[1]) / (br[1] - tl[1]);
		Coordinate coord = new Coordinate(row, col, 0);

		return provider.coordinateLocation(coord);
	}

	@Override
	public PVector getPointForLocation(Location location) {
		PMatrix2D m = getInternalTransformationMatrix();

		Coordinate coord = provider.locationCoordinate(location).zoomTo(0);
		float[] out = new float[2];
		m.mult(new float[] { coord.column * TILE_WIDTH, coord.row * TILE_HEIGHT }, out);

		out = getTransformedPosition(out[0], out[1], false);

		return new PVector(out[0], out[1]);
	}

	/**
	 * Creates the matrix used for tiles and coordinates.
	 * 
	 * @return The internal matrix.
	 */
	private PMatrix2D getInternalTransformationMatrix() {
		PMatrix2D m = new PMatrix2D();
		m.translate(width / 2, height / 2);
		m.scale((float) sc);
		m.translate((float) tx, (float) ty);
		return m;
	}

	// DRAWING --------------------------------------------------

	protected PGraphics getPG() {
		return papplet.g;
	}

	protected void postDraw() {
		// May be implemented in sub-classes.
	}

	/**
	 * Draws the on the PGraphics canvas.
	 */
	public void draw() {
		PGraphics pg = getPG();

		// Store and switch off smooth (OpenGL cannot handle it)
		boolean smooth = papplet.g.smooth;
		pg.noSmooth();

		// translate and scale, from the middle
		pg.pushMatrix();
		pg.translate(width/2, height/2);
		pg.scale((float) sc);
		pg.translate((float) tx, (float) ty);

		// find the bounds of the ur-tile in screen-space:
		float minX = pg.screenX(0, 0);
		float minY = pg.screenY(0, 0);
		float maxX = pg.screenX(TILE_WIDTH, TILE_HEIGHT);
		float maxY = pg.screenY(TILE_WIDTH, TILE_HEIGHT);

		Vector visibleKeys = getVisibleKeys(minX, minY, maxX, maxY);

		if (visibleKeys.size() > 0) {
			Coordinate previous = (Coordinate) visibleKeys.get(0);
			pg.pushMatrix();
			pg.scale(1.0f / PApplet.pow(2, previous.zoom));

			for (int i = 0; i < visibleKeys.size(); i++) {
				Coordinate coord = (Coordinate) visibleKeys.get(i);
				if (coord.zoom != previous.zoom) {
					pg.popMatrix();
					pg.pushMatrix();
					pg.scale(1.0f / PApplet.pow(2, coord.zoom));
				}

				if (images.containsKey(coord)) {
					PImage tile = (PImage) images.get(coord);

					float x = coord.column * TILE_WIDTH;
					float y = coord.row * TILE_HEIGHT;

					if (SHOW_DEBUG_BORDER) {
						pg.strokeWeight(2);
						pg.stroke(255, 0, 0, 100);
						pg.rect(x, y, TILE_WIDTH, TILE_HEIGHT);
						pg.noStroke();
					}
					pg.image(tile, x, y, TILE_WIDTH, TILE_HEIGHT);

					if (recent_images.contains(tile)) {
						recent_images.remove(tile);
					}
					recent_images.add(tile);
				}
			}
			pg.popMatrix();
		}
		pg.popMatrix();

		cleanupImageBuffer();

		postDraw();

		// Restore smoothing, if needed
		if (smooth) {
			papplet.smooth();
		}
	}

	protected Vector getVisibleKeys(float minX, float minY, float maxX, float maxY) {

		// what power of 2 are we at?
		// 0 when scale is around 1, 1 when scale is around 2,
		// 2 when scale is around 4, 3 when scale is around 8, etc.
		// Till: NB Using int zoom levels to use correct tile (between-zoom values are scaled in
		// Processing)
		int zoomLevel = Map.getZoomLevelFromScale((float) sc);

		// how many columns and rows of tiles at this zoom?
		// (this is basically (int)sc, but let's derive from zoom to be sure
		int cols = (int) Map.getScaleFromZoom(zoomLevel);
		int rows = (int) Map.getScaleFromZoom(zoomLevel);

		// find the biggest box the screen would fit in:, aligned with the mapDisplay:
		float screenMinX = 0;
		float screenMinY = 0;
		float screenMaxX = width - TILE_WIDTH;
		float screenMaxY = height - TILE_HEIGHT;

		// TODO: align this, and fix the next bit to work with rotated maps

		// find start and end columns
		int minCol = (int) PApplet.floor(cols * (screenMinX - minX) / (maxX - minX));
		int maxCol = (int) PApplet.ceil(cols * (screenMaxX - minX) / (maxX - minX));
		int minRow = (int) PApplet.floor(rows * (screenMinY - minY) / (maxY - minY));
		int maxRow = (int) PApplet.ceil(rows * (screenMaxY - minY) / (maxY - minY));

		// pad a bit, for luck (well, because we might be zooming out between
		// zoom levels)
		minCol -= grid_padding;
		minRow -= grid_padding;
		maxCol += grid_padding;
		maxRow += grid_padding;

		// we don't wrap around the world yet, so:
		minCol = PApplet.constrain(minCol, 0, cols);
		maxCol = PApplet.constrain(maxCol, 0, cols);
		minRow = PApplet.constrain(minRow, 0, rows);
		maxRow = PApplet.constrain(maxRow, 0, rows);

		// keep track of what we can see already:
		Vector visibleKeys = new Vector();

		// grab coords for visible tiles
		for (int col = minCol; col <= maxCol; col++) {
			for (int row = minRow; row <= maxRow; row++) {

				// source coordinate wraps around the world:
				Coordinate coord = provider.sourceCoordinate(new Coordinate(row, col, zoomLevel));
				// make sure we still have ints:
				coord.roundValues();

				// keep this for later:
				visibleKeys.add(coord);

				if (!images.containsKey(coord)) {
					// fetch it if we don't have it
					grabTile(coord);

					// see if we have a parent coord for this tile?
					boolean gotParent = false;
					for (int i = (int) coord.zoom; i > 0; i--) {
						Coordinate zoomed = coord.zoomTo(i).container();
						zoomed.roundValues();
						if (images.containsKey(zoomed)) {
							visibleKeys.add(zoomed);
							gotParent = true;
							break;
						}
					}

					// or if we have any of the children
					if (!gotParent) {
						Coordinate zoomed = coord.zoomBy(1).container();
						Coordinate[] kids = { zoomed, zoomed.right(), zoomed.down(),
								zoomed.right().down() };
						for (int i = 0; i < kids.length; i++) {
							zoomed = kids[i];
							// make sure we still have ints:
							zoomed.row = PApplet.round(zoomed.row);
							zoomed.column = PApplet.round(zoomed.column);
							zoomed.zoom = PApplet.round(zoomed.zoom);
							if (images.containsKey(zoomed)) {
								visibleKeys.add(zoomed);
							}
						}
					}

				}

			} // rows
		} // columns

		// sort by zoom so we draw small zoom levels (big tiles) first:
		Collections.sort(visibleKeys, zoomComparator);

		// stop fetching things we can't see:
		// (visibleKeys also has the parents and children, if needed, but that
		// shouldn't matter)
		queue.retainAll(visibleKeys);

		// sort what's left by distance from center:
		queueSorter.setCenter(new Coordinate((minRow + maxRow) / 2.0f, (minCol + maxCol) / 2.0f,
				zoomLevel));
		Collections.sort(queue, queueSorter);

		// load up to 4 more things:
		processQueue();

		return visibleKeys;
	}

	// TILE LOADING ---------------------------------------

	protected TileLoader createTileLoader(Coordinate coord) {
		return new ProcessingTileLoader(coord);
	}

	/**
	 * for tile loader threads to load PImages
	 */
	public class ProcessingTileLoader implements TileLoader, Runnable {
		Coordinate coord;

		ProcessingTileLoader(Coordinate coord) {
			this.coord = coord;
		}

		public void run() {
			String[] urls = provider.getTileUrls(coord);

			PImage img = null;

			if (USE_OFFLINE_MODE) {
				// Create image tile with coordinate information.
				img = getDebugTile(coord, null);
			} else {
				// Use 'unknown' as content-type to let loadImage decide
				img = papplet.loadImage(urls[0], "unknown");

				if (USE_DEBUG_TILES) {
					img = getDebugTile(coord, img);
				}

				if (img != null) {
					for (int i = 1; i < urls.length; i++) {
						PImage img2 = papplet.loadImage(urls[i], "unknown");
						if (img2 != null) {
							img.blend(img2, 0, 0, img.width, img.height, 0, 0, img.width,
									img.height, BLEND);
						}
					}
				}
			}

			tileDone(coord, img);
		}

		private PImage getDebugTile(Coordinate coord, PImage tileImage) {
			PGraphics pg = papplet.createGraphics(TILE_WIDTH, TILE_HEIGHT, P2D);
			pg.beginDraw();
			pg.textFont(font);

			if (tileImage != null) {
				pg.image(tileImage, 0, 0);
			} else {
				pg.background(250);
				pg.stroke(0);
				pg.rect(0, 0, pg.width, pg.height);
				pg.ellipse(pg.width / 2, pg.height / 2, pg.width - 5, pg.height - 5);
			}

			pg.fill(0);
			String infoText = coord.column + ", " + coord.row + "\nz: " + coord.zoom;
			pg.text(infoText, pg.width / 2 - pg.textWidth(infoText) / 2, pg.height / 2 - 10);
			pg.endDraw();
			String tempName = coord.column + "-" + coord.row + "-" + coord.zoom + ".png";
			pg.save(tempName);

			PImage img = papplet.loadImage(tempName);
			return img;
		}
	}

}
