package de.fhpotsdam.unfolding.mapdisplay;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Vector;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PMatrix3D;
import processing.core.PVector;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.tiles.TileLoader;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * The internal MapDisplay to handle the map display in Processing 2D (and used as super class for 3D)
 */
@SuppressWarnings("unchecked")
public class Java2DMapDisplay extends AbstractMapDisplay implements PConstants {

	public static Logger log = Logger.getLogger(Java2DMapDisplay.class);

	// Used for loadImage and float maths
	public PApplet papplet;

	/** The inner transformation matrix. Scales and rotates the map. */
	protected PMatrix3D innerMatrix = new PMatrix3D();

	/** The outer transformation matrix. Rotates the map pane. */
	protected PMatrix3D matrix = new PMatrix3D();

	// Background color
	protected Integer bgColor = null;

	// To notify client app when all tiles have been loaded
	private static final String TILESLOADED_METHOD_NAME = "tilesLoaded";
	private Method tilesLoadedMethod = null;

	/**
	 * Creates a new MapDisplay with full canvas size, and given provider
	 */
	public Java2DMapDisplay(PApplet papplet, AbstractMapProvider provider) {
		this(papplet, provider, 0, 0, papplet.width, papplet.height);
	}

	/**
	 * Creates a new MapDisplay.
	 */
	public Java2DMapDisplay(PApplet papplet, AbstractMapProvider provider, float offsetX, float offsetY, float width,
			float height) {
		super(provider, width, height);
		this.papplet = papplet;

		this.offsetX = offsetX;
		this.offsetY = offsetY;

		this.innerOffsetX = width / 2 - TILE_WIDTH / 2;
		this.innerOffsetY = height / 2 - TILE_HEIGHT / 2;

		registerTilesLoadedMethod();

		setInnerTransformationCenter(new PVector(width / 2 + offsetX, height / 2 + offsetY));

		calculateMatrix();

		calculateInnerMatrix();
	}

	private void registerTilesLoadedMethod() {
		try {
			Class<? extends PApplet> appletClass = papplet.getClass();
			tilesLoadedMethod = appletClass.getMethod(TILESLOADED_METHOD_NAME);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		}
	}

	/**
	 * Gets called if all tiles have been loaded. Invokes the tilesLoaded method in the client application if existing.
	 * 
	 * TODO Pass the ID of this map to the method to enable handling multiple maps.
	 */
	@Override
	public void tilesLoaded() {
		if (tilesLoadedMethod != null) {
			try {
				tilesLoadedMethod.invoke(papplet);
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}

	// TRANSFORMATION --------------------------------------------------

	/**
	 * Updates the matrix to transform the map with.
	 * 
	 * For the rotation the matrix has to be temporarily translated to the transformation center. Thus, it has to be
	 * reset with the original position, afterwards. Original position is calculated by inverting the current matrix.
	 * (As the matrix incorporates that position, it stores every transformation, even though the matrix is created
	 * anew.)
	 */
	public void calculateMatrix() {
		synchronized (this) {
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
	}

	public void calculateInnerMatrix() {
		// Synchronize on this to not interfere with tile loading (see getVisibleKeys)
		synchronized (this) {
			PMatrix3D invMatrix = new PMatrix3D();
			invMatrix.apply(innerMatrix);
			invMatrix.invert();

			float originalCenterX = invMatrix.multX(innerTransformationCenter.x, innerTransformationCenter.y);
			float originalCenterY = invMatrix.multY(innerTransformationCenter.x, innerTransformationCenter.y);

			innerMatrix = new PMatrix3D();
			innerMatrix.translate(innerTransformationCenter.x, innerTransformationCenter.y);
			innerMatrix.scale(innerScale);
			innerMatrix.rotateZ(innerAngle);
			innerMatrix.translate(-originalCenterX, -originalCenterY);
		}
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

	@Deprecated
	public float[] getInnerObjectFromScreenPosition(float x, float y) {
		float objectXY[] = getObjectFromScreenPosition(x, y);
		float innerObjectXY[] = getInnerObjectFromObjectPosition(objectXY[0], objectXY[1]);
		return innerObjectXY;
	}

	public float[] getInnerObject(ScreenPosition screenPosition) {
		float objectXY[] = getObjectFromScreenPosition(screenPosition.x, screenPosition.y);
		float innerObjectXY[] = getInnerObjectFromObjectPosition(objectXY[0], objectXY[1]);
		return innerObjectXY;
	}

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
		Coordinate coord = getCoordinateFromInnerPosition(x, y);
		return provider.coordinateLocation(coord);
	}

	private Coordinate getCoordinateFromInnerPosition(float x, float y) {
		PMatrix3D m = new PMatrix3D();
		float tl[] = new float[3];
		m.mult(new float[] { 0, 0, 0 }, tl);
		float br[] = new float[3];
		m.mult(new float[] { TILE_WIDTH, TILE_HEIGHT, 0 }, br);

		float col = (x - tl[0]) / (br[0] - tl[0]);
		float row = (y - tl[1]) / (br[1] - tl[1]);
		Coordinate coord = new Coordinate(row, col, 0);
		return coord;
	}

	@Deprecated
	@Override
	public Location getLocationFromScreenPosition(float x, float y) {
		return getLocation(x, y);
	}

	@Override
	public Location getLocation(float x, float y) {
		synchronized (this) {
			float innerObjectXY[] = getInnerObjectFromScreenPosition(x, y);
			return getLocationFromInnerObjectPosition(innerObjectXY[0], innerObjectXY[1]);
		}
	}

	@Override
	public Location getLocation(ScreenPosition screenPosition) {
		synchronized (this) {
			float innerObjectXY[] = getInnerObjectFromScreenPosition(screenPosition.x, screenPosition.y);
			return getLocationFromInnerObjectPosition(innerObjectXY[0], innerObjectXY[1]);
		}
	}

	@Override
	public Location getLocationFromObjectPosition(float x, float y) {
		synchronized (this) {
			float innerObjectXY[] = getInnerObjectFromObjectPosition(x, y);
			return getLocationFromInnerObjectPosition(innerObjectXY[0], innerObjectXY[1]);
		}
	}

	public float[] getInnerObjectFromLocation(Location location) {
		Coordinate coord = provider.locationCoordinate(location).zoomTo(0);
		return new float[] { coord.column * TILE_WIDTH, coord.row * TILE_HEIGHT, 0 };

		// PMatrix3D m = new PMatrix3D();
		// Coordinate coord = provider.locationCoordinate(location).zoomTo(0);
		// float[] out = new float[3];
		// m.mult(new float[] { coord.column * TILE_WIDTH, coord.row * TILE_HEIGHT, 0 }, out);
		// log.debug("getInnerObjectFromLocation. " + (coord.column * TILE_WIDTH) + ", " + out[0]);
		// return out;
	}

	@Override
	public ScreenPosition getScreenPosition(Location location) {
		synchronized (this) {
			float innerObjectXY[] = getInnerObjectFromLocation(location);
			return new ScreenPosition(getScreenFromInnerObjectPosition(innerObjectXY[0], innerObjectXY[1]));
		}
	}
	
	@Override
	public ScreenPosition getScreenPositionFloat(Location location) {
		synchronized (this) {
			float innerObjectXY[] = getInnerObjectFromLocation(location);
			return new ScreenPosition(getScreenFromInnerObjectPosition(innerObjectXY[0], innerObjectXY[1]), true);
		}
	}

	@Deprecated
	public float[] getScreenPositionFromLocation(Location location) {
		synchronized (this) {
			float innerObjectXY[] = getInnerObjectFromLocation(location);
			return getScreenFromInnerObjectPosition(innerObjectXY[0], innerObjectXY[1]);
		}
	}

	// DRAWING --------------------------------------------------

	public PGraphics getInnerPG() {
		// NB Always inner graphics, this one not used. Implemented in sub classes.
		return papplet.g;
	}

	public PGraphics getOuterPG() {
		return papplet.g;
	}

	/**
	 * Is called last in {@link #draw()}. May be implemented in sub-classes to handle drawing on outerPG.
	 */
	protected void postDraw() {

		// Draws all markers
		for (MarkerManager<Marker> mm : markerManagerList) {
			mm.draw();
		}

	}

	/**
	 * Draws the on the PGraphics canvas.
	 */
	public void draw() {
		PGraphics pg = getInnerPG();
		pg.beginDraw();

		// Clears canvas (set to transparency, to not smudge previous map on panning)
		pg.clear();
		if (bgColor != null) {
			// Set background color if given (can also be transparent)
			pg.background(bgColor);
		}

		// translate and scale, from the middle
		pg.pushMatrix();
		pg.translate((float) innerOffsetX, (float) innerOffsetY);
		if (pg.is3D()) {
			pg.applyMatrix(innerMatrix);
		} else {
			pg.applyMatrix(innerMatrix.m00, innerMatrix.m01, innerMatrix.m03, innerMatrix.m10, innerMatrix.m11,
					innerMatrix.m13);
		}

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

					// REVISIT For transparency, do not paint image (why no transparent imgs?)
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
		pg.endDraw();

		postDraw();

		cleanupImageBuffer();
	}

	/**
	 * Sets background color of map.
	 * 
	 * @param color
	 *            Color for the background canvas. Can be semi-transparent. If null it is not used.
	 */
	public void setBackgroundColor(Integer color) {
		this.bgColor = color;
	}

	// Based on code by ModestMaps, Tom Carden
	protected Vector getVisibleKeys(PGraphics pg) {

		// Stores IDs of tiles already displayed
		Vector visibleKeys = new Vector();

		// Grid to load tiles for.
		int minCol, maxCol, minRow, maxRow;

		int zoomLevel = UnfoldingMap.getZoomLevelFromScale(innerScale);

		// Synchronize on this to not interfere with tile loading (see getVisibleKeys)
		// NB External threads can change innerMatrix, innerScale, and other properties while this method is
		// running, which resulted in incorrect values, and in incorrect tile loading.
		synchronized (this) {

			// Gets outer object corner positions in inner object coordinate system
			// to check which tiles to load: Always uses bounding box.
			float[] innerTL = getInnerObjectFromObjectPosition(0, 0);
			float[] innerTR = getInnerObjectFromObjectPosition(getWidth(), 0);
			float[] innerBR = getInnerObjectFromObjectPosition(getWidth(), getHeight());
			float[] innerBL = getInnerObjectFromObjectPosition(0, getHeight());

			Coordinate coordTL = getCoordinateFromInnerPosition(innerTL[0], innerTL[1]).zoomTo(zoomLevel);
			Coordinate coordTR = getCoordinateFromInnerPosition(innerTR[0], innerTR[1]).zoomTo(zoomLevel);
			Coordinate coordBR = getCoordinateFromInnerPosition(innerBR[0], innerBR[1]).zoomTo(zoomLevel);
			Coordinate coordBL = getCoordinateFromInnerPosition(innerBL[0], innerBL[1]).zoomTo(zoomLevel);

			minCol = (int) PApplet.min(new float[] { coordTL.column, coordTR.column, coordBR.column, coordBL.column });
			maxCol = (int) PApplet.max(new float[] { coordTL.column, coordTR.column, coordBR.column, coordBL.column });
			minRow = (int) PApplet.min(new float[] { coordTL.row, coordTR.row, coordBR.row, coordBL.row });
			maxRow = (int) PApplet.max(new float[] { coordTL.row, coordTR.row, coordBR.row, coordBL.row });
		}

		// Add tile padding (to pre-load, and because we might be zooming out between zoom levels)
		minCol -= grid_padding;
		minRow -= grid_padding;
		maxCol += grid_padding;
		maxRow += grid_padding;

		// log.debug("getVisibleKeys: " + minCol + "," + maxCol + "; " + minRow + "," + maxRow);
		// PApplet.println("getVisibleKeys: " + minCol + "," + maxCol + "; " + minRow + "," + maxRow);

		// we don't wrap around the world yet, so:
		int numberTiles = (int) UnfoldingMap.getScaleFromZoom(zoomLevel);
		minCol = PApplet.constrain(minCol, 0, numberTiles);
		maxCol = PApplet.constrain(maxCol, 0, numberTiles);
		minRow = PApplet.constrain(minRow, 0, numberTiles);
		maxRow = PApplet.constrain(maxRow, 0, numberTiles);

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
						Coordinate[] kids = { zoomed, zoomed.right(), zoomed.down(), zoomed.right().down() };
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
		queueSorter.setCenter(new Coordinate((minRow + maxRow) / 2.0f, (minCol + maxCol) / 2.0f, zoomLevel));
		Collections.sort(queue, queueSorter);

		// load up to 4 more things:
		processQueue();

		return visibleKeys;
	}

	// TILE LOADING ---------------------------------------

	protected TileLoader createTileLoader(Coordinate coord) {
		return new TileLoader(papplet, provider, this, coord);

		// TODO Make showDebug etc public.
		// TileLoader tl = new TileLoader(papplet, provider, this, coord);
		// tl.showDebugBorder();
		// tl.showTileCoordinates();
		// return tl;
	}

}
