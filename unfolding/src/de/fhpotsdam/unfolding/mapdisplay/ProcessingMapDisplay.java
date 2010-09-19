package de.fhpotsdam.unfolding.mapdisplay;

import java.util.Collections;
import java.util.Vector;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PMatrix3D;
import processing.core.PVector;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;

@SuppressWarnings("unchecked")
public class ProcessingMapDisplay extends AbstractMapDisplay implements PConstants {

	public static Logger log = Logger.getLogger(ProcessingMapDisplay.class);

	/** Shows borders around each tile. */
	private static final boolean SHOW_DEBUG_BORDER = true;
	/** Shows coordinate infos on real tile, i.e. placed atop of the original tile image. */
	private static final boolean USE_DEBUG_TILES = false;
	/** Creates and shows debug tiles, i.e. own images with debug information. */
	private static final boolean USE_OFFLINE_MODE = false;

	// Used for loadImage and float maths
	public PApplet papplet;

	/** The transformation matrix. */
	protected PMatrix3D matrix = new PMatrix3D();

	protected PMatrix3D innerMatrix = new PMatrix3D();

	/** Only for debugging */
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
		font = papplet.loadFont("Miso-Light-12.vlw");

		this.offsetX = offsetX;
		this.offsetY = offsetY;

		this.innerOffsetX = width / 2 - TILE_WIDTH / 2;
		this.innerOffsetY = height / 2 - TILE_HEIGHT / 2;

		setInnerTransformationCenter(new PVector(width / 2 + offsetX, height / 2 + offsetY));

		calculateMatrix();

		calculateInnerMatrix();
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
	public void calculateMatrix() {
		PMatrix3D invMatrix = new PMatrix3D();
		invMatrix.apply(matrix);
		invMatrix.invert();

		float originalCenterX = invMatrix.multX(transformationCenter.x, transformationCenter.y);
		float originalCenterY = invMatrix.multY(transformationCenter.x, transformationCenter.y);

		matrix = new PMatrix3D();
		matrix.translate(transformationCenter.x, transformationCenter.y);
		matrix.scale(scale);
		matrix.rotateZ(angle);
		matrix.translate(-originalCenterX, -originalCenterY);
	}

	public void calculateInnerMatrix() {
		PMatrix3D invMatrix = new PMatrix3D();
		invMatrix.apply(innerMatrix);
		invMatrix.invert();

		float originalCenterX = invMatrix.multX(innerTransformationCenter.x,
				innerTransformationCenter.y);
		float originalCenterY = invMatrix.multY(innerTransformationCenter.x,
				innerTransformationCenter.y);

		innerMatrix = new PMatrix3D();
		innerMatrix.translate(innerTransformationCenter.x, innerTransformationCenter.y);
		innerMatrix.scale(innerScale);
		innerMatrix.rotateZ(innerAngle);
		innerMatrix.translate(-originalCenterX, -originalCenterY);
	}

	@Override
	public float[] getObjectFromScreenPosition(float x, float y) {
		return getTransformedPosition(x, y, true);
	}

	@Override
	public float[] getScreenFromObjectPosition(float x, float y) {
		return getTransformedPosition(x, y, false);
	}

	@Override
	protected float[] getTransformedPosition(float x, float y, boolean inverse) {
		if (inverse) {
			x -= offsetX;
			y -= offsetY;
		}

		float[] xyz = new float[3];
		PMatrix3D m = new PMatrix3D();
		m.apply(matrix);
		if (inverse) {
			m.invert();
		}
		m.mult(new float[] { x, y, 0 }, xyz);

		if (!inverse) {
			xyz[0] += offsetX;
			xyz[1] += offsetY;
		}

		return xyz;
	}

	public float[] getObjectFromInnerObjectPosition(float x, float y) {
		return getInnerTransformedPosition(x, y, false);
	}

	public float[] getInnerObjectFromObjectPosition(float x, float y) {
		return getInnerTransformedPosition(x, y, true);
	}

	public float[] getScreenFromInnerObjectPosition(float x, float y) {
		float objectXY[] = getObjectFromInnerObjectPosition(x, y);
		float screenXY[] = getScreenFromObjectPosition(objectXY[0], objectXY[1]);
		return screenXY;
	}

	public float[] getInnerObjectFromScreenPosition(float x, float y) {
		float objectXY[] = getObjectFromScreenPosition(x, y);
		float innerObjectXY[] = getInnerObjectFromObjectPosition(objectXY[0], objectXY[1]);
		return innerObjectXY;
	}

	@Override
	public float[] getObjectFromLocation(Location location) {
		float[] ixy = getInnerObjectFromLocation(location);
		float[] xy = getObjectFromInnerObjectPosition(ixy[0], ixy[1]);
		return xy;
	}

	private float[] getInnerTransformedPosition(float x, float y, boolean inverse) {
		if (inverse) {
			x -= innerOffsetX;
			y -= innerOffsetY;
		}

		float[] xyz = new float[3];
		PMatrix3D m = new PMatrix3D();
		m.apply(innerMatrix);
		if (inverse) {
			m.invert();
		}
		m.mult(new float[] { x, y, 0 }, xyz);

		if (!inverse) {
			xyz[0] += innerOffsetX;
			xyz[1] += innerOffsetY;
		}

		return xyz;
	}

	// Location (instead of innerObj) methods ---------------
	public Location getLocationFromInnerObjectPosition(float x, float y) {
		PMatrix3D m = new PMatrix3D();
		float tl[] = new float[3];
		m.mult(new float[] { 0, 0, 0 }, tl);
		float br[] = new float[3];
		m.mult(new float[] { TILE_WIDTH, TILE_HEIGHT, 0 }, br);

		float col = (x - tl[0]) / (br[0] - tl[0]);
		float row = (y - tl[1]) / (br[1] - tl[1]);
		Coordinate coord = new Coordinate(row, col, 0);

		return provider.coordinateLocation(coord);
	}

	public Location getLocationFromScreenPosition(float x, float y) {
		float innerObjectXY[] = getInnerObjectFromScreenPosition(x, y);
		return getLocationFromInnerObjectPosition(innerObjectXY[0], innerObjectXY[1]);
	}

	public float[] getInnerObjectFromLocation(Location location) {
		PMatrix3D m = new PMatrix3D();
		Coordinate coord = provider.locationCoordinate(location).zoomTo(0);
		float[] out = new float[3];
		m.mult(new float[] { coord.column * TILE_WIDTH, coord.row * TILE_HEIGHT, 0 }, out);
		return out;
	}

	public float[] getScreenPositionFromLocation(Location location) {
		float innerObjectXY[] = getInnerObjectFromLocation(location);
		return getScreenFromInnerObjectPosition(innerObjectXY[0], innerObjectXY[1]);
	}

	// @Override
	// public Location getCenterLocation() {
	// Location location = getLocationForObjectPosition(width / 2, height / 2);
	// return location;
	// }
	//
	// @Override
	// public Location getLocationForScreenPosition(float x, float y) {
	// // float objectPosition[] = getObjectFromScreenPosition(x, y);
	// // return getLocationForObjectPosition(objectPosition[0], objectPosition[1]);
	//		
	// float innerObjPos[] = getInnerObjectFromScreenPosition(x, y);
	// return getLocationForInnerObjectPosition(innerObjPos[0], innerObjPos[1]);
	// }
	//
	// // This is the inverse method for getObjPosForLoc
	// public Location getLocationForObjectPosition(float x, float y) {
	// PMatrix3D m = innerMatrix;
	//
	// PMatrix3D m2 = new PMatrix3D();
	// m2.apply(m);
	// // FIXME innerFix: Remove tx, scale, innerTransCenter
	// // m2.translate((float) tx, (float) ty);
	//
	// // Find top left and bottom right positions of mapDisplay in screenspace:
	// // REVISIT Above comment: Is this in screenspace?
	// float tl[] = new float[3];
	// m2.mult(new float[] { 0, 0, 0 }, tl);
	// float br[] = new float[3];
	// m2.mult(new float[] { TILE_WIDTH, TILE_HEIGHT, 0 }, br);
	//
	// float col = (x - tl[0]) / (br[0] - tl[0]);
	// float row = (y - tl[1]) / (br[1] - tl[1]);
	// Coordinate coord = new Coordinate(row, col, 0);
	//
	// return provider.coordinateLocation(coord);
	// }
	//
	// @Override
	// public PVector getScreenPosForLocation(Location location) {
	// PVector v = getObjectPosForLocation(location);
	// float[] out = getScreenFromObjectPosition(v.x, v.y);
	// return new PVector(out[0], out[1]);
	// }
	//
	// public PVector getObjectPosForLocation(Location location) {
	// PMatrix3D m = innerMatrix;
	//
	// PMatrix3D m2 = new PMatrix3D();
	// m2.apply(m);
	// // FIXME innerFix: Remove tx, scale, innerTransCenter
	// // m2.translate((float) tx, (float) ty);
	//
	// Coordinate coord = provider.locationCoordinate(location).zoomTo(0);
	// float[] out = new float[3];
	// m2.mult(new float[] { coord.column * TILE_WIDTH, coord.row * TILE_HEIGHT, 0 }, out);
	//
	// return new PVector(out[0], out[1]);
	// }

	// DRAWING --------------------------------------------------

	public PGraphics getPG() {
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
		pg.background(0);

		// translate and scale, from the middle
		pg.pushMatrix();
		pg.translate((float) innerOffsetX, (float) innerOffsetY);
		pg.applyMatrix(innerMatrix);

		Vector visibleKeys = getVisibleKeys(pg);

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

	protected Vector getVisibleKeys(PGraphics pg) {

		// FIXME Fix tile loading for inner rotate
		// 

		// find the bounds of the ur-tile in screen-space:
		float minX = pg.screenX(0, 0);
		float minY = pg.screenY(0, 0);
		float maxX = pg.screenX(TILE_WIDTH, TILE_HEIGHT);
		float maxY = pg.screenY(TILE_WIDTH, TILE_HEIGHT);

		// float[] minXY = getScreenFromInnerObjectPosition(0, 0);
		// float[] maxXY = getScreenFromInnerObjectPosition(TILE_WIDTH, TILE_HEIGHT);
		// minX = minXY[0];
		// minY = minXY[1];
		// maxX = maxXY[0];
		// maxY = maxXY[1];
		
		
		// what power of 2 are we at?
		// 0 when scale is around 1, 1 when scale is around 2,
		// 2 when scale is around 4, 3 when scale is around 8, etc.
		// Till: NB Using int zoom levels to use correct tile (between-zoom values are scaled in
		// Processing)
		int zoomLevel = Map.getZoomLevelFromScale((float) innerScale);

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

		if (minX > maxX) {
			float t = minX;
			minX = maxX;
			maxX = t;
		}
		if (minY > maxY) {
			float t = minY;
			minY = maxY;
			maxY = t;
		}

		//log.debug("(" + screenMinX + " - " + minX + ") / (" + maxX + " - " + minX + ")");

		// find start and end columns
		int minCol = (int) PApplet.floor(cols * (screenMinX - minX) / (maxX - minX));
		int maxCol = (int) PApplet.ceil(cols * (screenMaxX - minX) / (maxX - minX));
		int minRow = (int) PApplet.floor(rows * (screenMinY - minY) / (maxY - minY));
		int maxRow = (int) PApplet.ceil(rows * (screenMaxY - minY) / (maxY - minY));

		//log.debug("col " + minCol + "," + maxCol + "; row " + minRow + "," + maxRow);

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
