package de.fhpotsdam.unfolding.mapdisplay;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import processing.core.PVector;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;

/**
 * Handles tiles
 * 
 */
public abstract class AbstractMapDisplay {

	public static final int TILE_WIDTH = 256;
	public static final int TILE_HEIGHT = 256;

	// If less than this memory threshold is reached, oldest tile images will be deleted.
	private static final long MEMORY_THRESHOLD_BYTES = 300000;
	// Number of tile images to delete.
	private static final int MEMORY_THRESHOLD_IMAGES = 25;

	public static Logger log = Logger.getLogger(AbstractMapDisplay.class);

	// Dimension of this map display
	public float width;
	public float height;

	// Map values: Inner position and scale (used for tiles)
	public double tx = -TILE_WIDTH / 2; // half the world width, at zoom 0
	public double ty = -TILE_HEIGHT / 2; // half the world height, at zoom 0
	public double sc = 1;

	// MapDisplay values: Outer position, rotation, and scale
	/** Vertical offset of this MapDisplay (in world coordinates). */
	public float offsetX;
	/** Horizontal offset of this MapDisplay (in world coordinates). */
	public float offsetY;
	/** Rotation of this MapDisplay. */
	public float angle;
	/** Scale of this MapDisplay. */
	public float scale = 1.0f;
	public PVector transformationCenter;

	// Tiles
	public int max_pending = 4;
	public int max_images_to_keep = 256;
	public int grid_padding = 0; // was: 1

	protected AbstractMapProvider provider;
	protected Hashtable<Coordinate, Runnable> pending = new Hashtable<Coordinate, Runnable>();
	protected Hashtable<Coordinate, Object> images = new Hashtable<Coordinate, Object>();
	protected Vector<Coordinate> queue = new Vector<Coordinate>();
	protected Vector<Object> recent_images = new Vector<Object>();

	protected ZoomComparator zoomComparator = new ZoomComparator();
	protected QueueSorter queueSorter = new QueueSorter();

	protected AbstractMapDisplay(AbstractMapProvider _provider, float _width, float _height) {
		provider = _provider;
		width = _width;
		height = _height;
		transformationCenter = new PVector(width / 2, height / 2);
		sc = (float) Math.ceil(Math.min(height / (float) TILE_WIDTH, width / (float) TILE_HEIGHT));
	}

	public AbstractMapProvider getMapProvider() {
		return this.provider;
	}

	public void setMapProvider(AbstractMapProvider provider) {
		if (this.provider.getClass() != provider.getClass()) {
			this.provider = provider;
			images.clear();
			queue.clear();
			pending.clear();
		}
	}

	public abstract void draw();

	// TRANSFORMATION --------------------------------------------------

	/**
	 * Converts world location to screen coordinates.
	 * 
	 * e.g. used to place geo-coded marker on the map.
	 */
	public abstract PVector getPointForLocation(Location location);

	/**
	 * Converts screen coordinates to map location.
	 * 
	 * This includes both transformation as well as Cartesian to world coordinates conversion.
	 * 
	 * e.g. used to pan to mouse position.
	 */
	public abstract Location getLocationForPoint(float x, float y);

	/**
	 * Returns location of the internal map center.
	 * 
	 * @return The center location.
	 */
	public abstract Location getCenterLocation();

	/**
	 * Updates the matrix to transform the map with with the current transformation center.
	 */
	public abstract void calculateMatrix();

	/**
	 * Calculates offset and rotation for screen canvas position, to be used with the internal
	 * transformation matrix.
	 * 
	 * @param x
	 *            Cartesian x coordinate.
	 * @param y
	 *            Cartesian y coordinate.
	 * @param inverse
	 *            Indicates back and forward matrix calculation. Inverse is used for point2location,
	 *            non-inverse for location2point.
	 * @return An 1d-2elements-array with x and y.
	 */
	public abstract float[] getTransformedPosition(float x, float y, boolean inverse);

	// TILES --------------------------------------------------------

	public void processQueue() {
		while (pending.size() < max_pending && queue.size() > 0) {
			Coordinate coord = (Coordinate) queue.remove(0);
			TileLoader tileLoader = createTileLoader(coord);
			pending.put(coord, tileLoader);
			new Thread(tileLoader).start();
		}
	}

	protected abstract TileLoader createTileLoader(Coordinate coord);

	public void grabTile(Coordinate coord) {
		if (!pending.containsKey(coord) && !queue.contains(coord) && !images.containsKey(coord))
			queue.add(coord);
	}

	// TODO: images & pending thread safe?
	public void tileDone(Coordinate _coord, Object _image) {
		if (pending.containsKey(_coord) && _image != null) {
			images.put(_coord, _image);
			pending.remove(_coord);
		} else {
			queue.add(_coord);
			pending.remove(_coord);
		}
	}

	// LOAD SORTING
	public class QueueSorter implements Comparator<Coordinate> {
		Coordinate center;

		public void setCenter(Coordinate center) {
			this.center = center;
		}

		public int compare(Coordinate c1, Coordinate c2) {
			if (c1.zoom == center.zoom) {
				if (c2.zoom == center.zoom) {
					// only compare squared distances… saves cpu
					float d1 = (float) Math.pow(c1.column - center.column, 2)
							+ (float) Math.pow(c1.row - center.row, 2);
					float d2 = (float) Math.pow(c2.column - center.column, 2)
							+ (float) Math.pow(c2.row - center.row, 2);
					return d1 < d2 ? -1 : d1 > d2 ? 1 : 0;
				} else {
					return -1;
				}
			} else if (c2.zoom == center.zoom) {
				return 1;
			} else {
				float d1 = Math.abs(c1.zoom - center.zoom);
				float d2 = Math.abs(c2.zoom - center.zoom);
				return d1 < d2 ? -1 : d1 > d2 ? 1 : 0;
			}
		}
	}

	public class ZoomComparator implements Comparator<Coordinate> {
		public int compare(Coordinate c1, Coordinate c2) {
			return c1.zoom < c2.zoom ? -1 : c1.zoom > c2.zoom ? 1 : 0;
		}
	}

	/**
	 * Cleans oldest images if too many images exist, or if memory is too full.
	 * 
	 * Tiles are added to the recency-based list to allow removing oldest ones from images-array.
	 * 
	 * REVISIT Check java.lang.ref.SoftReference for better solution.
	 */
	protected void cleanupImageBuffer() {
		if (recent_images.size() > max_images_to_keep) {
			log.info("Cleaning image buffer due to MAX_IMAGE reached.");
			recent_images.subList(0, recent_images.size() - max_images_to_keep).clear();
			images.values().retainAll(recent_images);
		} else if (Runtime.getRuntime().freeMemory() < MEMORY_THRESHOLD_BYTES) {
			log.info("Cleaning image buffer due to MEMORY_THRESHOLD reached.");
			int imagesToDelete = recent_images.size() > MEMORY_THRESHOLD_IMAGES ? MEMORY_THRESHOLD_IMAGES
					: recent_images.size();
			recent_images.subList(0, imagesToDelete).clear();
			images.values().retainAll(recent_images);
		}
	}
}
