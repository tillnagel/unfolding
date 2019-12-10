package de.fhpotsdam.unfolding.mapdisplay;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import processing.core.PGraphics;
import processing.core.PVector;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.tiles.TileLoader;
import de.fhpotsdam.unfolding.tiles.TileLoaderListener;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Handles tiles management and display, and map location and screen position conversions.
 * 
 * Use {@link UnfoldingMap} in your application. This is the internal class, and should be used only if you know what
 * you are doing.
 * 
 */
public abstract class AbstractMapDisplay implements TileLoaderListener {

	public static final int TILE_WIDTH = 256;
	public static final int TILE_HEIGHT = 256;

	// If less than this memory threshold is reached, oldest tile images will be deleted.
	private static final long MEMORY_THRESHOLD_BYTES = 300000;
	// Number of tile images to delete.
	private static final int MEMORY_THRESHOLD_IMAGES = 25;

	public static Logger log = Logger.getLogger(AbstractMapDisplay.class);

	// Dimension of this map display
	protected float width;
	protected float height;

	// Map values: Inner position and scale (used for tiles)
	public double innerOffsetX = -TILE_WIDTH / 2; // half the world width, at zoom 0
	public double innerOffsetY = -TILE_HEIGHT / 2; // half the world height, at zoom 0
	public float innerScale = 1;
	public float innerAngle;

	// MapDisplay values: Outer position, rotation, and scale
	/** Vertical offset of this MapDisplay (in screen coordinates). */
	public float offsetX;
	/** Horizontal offset of this MapDisplay (in screen coordinates). */
	public float offsetY;
	/** Rotation of this MapDisplay. */
	public float angle;
	/** Scale of this MapDisplay. Not used yet! */
	public float scale = 1.0f;
	/** Center of outer transformations in screen pixel. */
	protected PVector transformationCenter;

	// MapDisplay values: Inner stuff
	protected PVector innerTransformationCenter;

	// List of MarkerManager with one default MarkerManager
	protected List<MarkerManager<Marker>> markerManagerList;

	// Tiles
	public int max_pending = 4;
	// public int max_images_to_keep = 256;
	public int max_images_to_keep = 1024;
	public int grid_padding = 1; // set to 0 for debugging purposes

	/** Check whether all currently visible tiles have been loaded. */
	protected boolean allTilesLoaded = false;

	protected AbstractMapProvider provider;

	/** Pending threads to load tiles for coordinate. */
	protected Hashtable<Coordinate, Runnable> pending = new Hashtable<Coordinate, Runnable>();
	/** Loaded tiles for coordinate. */
	protected Hashtable<Coordinate, Object> images = new Hashtable<Coordinate, Object>();
	/** Queue of coordinates to create threads and load tiles. */
	protected Vector<Coordinate> queue = new Vector<Coordinate>();
	protected Vector<Object> recent_images = new Vector<Object>();

	protected ZoomComparator zoomComparator = new ZoomComparator();
	protected QueueSorter queueSorter = new QueueSorter();

	private MarkerManager<Marker> defaultMarkerManager = null;

	protected AbstractMapDisplay(AbstractMapProvider provider, float width, float height) {
		this.provider = provider;

		this.width = width;
		this.height = height;

		transformationCenter = new PVector(width / 2, height / 2);
		innerTransformationCenter = new PVector(width / 2, height / 2);

		innerScale = (float) Math.ceil(Math.min(height / (float) TILE_WIDTH, width / (float) TILE_HEIGHT));

		markerManagerList = new ArrayList<MarkerManager<Marker>>();
	}

	public void resize(float width, float height) {
		this.width = width;
		this.height = height;
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

	public abstract PGraphics getInnerPG();

	public abstract PGraphics getOuterPG();

	public PGraphics getMask() {
		return null;
	}

	public abstract void draw();

	/**
	 * Sets background color of map.
	 * 
	 * @param color
	 *            Color for the background canvas. Can be semi-transparent. If null it is not used.
	 */
	public abstract void setBackgroundColor(Integer color);

	// MarkerManagement -----------------------------------------------

	/**
	 * You need to set the map of the given MarkerManager before using.
	 */
	public void addMarkerManager(MarkerManager<Marker> markerManager) {
		// Replace default MarkerManager, if only default exists and has no entries
		if (markerManagerList.size() == 1) {
			MarkerManager<?> mm = markerManagerList.get(0);
			if (mm.getMarkers().size() == 0 && mm.equals(this.defaultMarkerManager)) {
				markerManagerList.remove(0);
				this.defaultMarkerManager = null;
			}
		}

		markerManagerList.add(markerManager);
	}

	public MarkerManager<Marker> getLastMarkerManager() {
		return markerManagerList.get(markerManagerList.size() - 1);
	}

	public MarkerManager<Marker> getDefaultMarkerManager() {
		return getMarkerManager(0);
	}

	@Deprecated
	public MarkerManager<Marker> getMarkerManager() {
		return getDefaultMarkerManager();
	}

	public MarkerManager<Marker> getMarkerManager(int index) {
		if (markerManagerList.size() > index) {
			return markerManagerList.get(index);
		} else {
			return null;
		}
	}

	/**
	 * Adds a marker to the default marker manager.
	 * 
	 * If you have more than one marker manager, use {@link MarkerManager#addMarker(Marker)} instead.
	 * 
	 * @param marker
	 *            The marker to add.
	 */
	public void addMarker(Marker marker) {
		getDefaultMarkerManager().addMarker(marker);
	}

	/**
	 * Adds multiple markers to the default marker manager.
	 * 
	 * If you have more than one marker manager, use {@link MarkerManager#addMarkers(List)} instead.
	 * 
	 * @param markers
	 *            The markers to add.
	 */
	public void addMarkers(List<Marker> markers) {
		getDefaultMarkerManager().addMarkers(markers);
	}

	// TRANSFORMATION --------------------------------------------------

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	/**
	 * Updates the matrix to transform the map with with the current transformation center.
	 */
	public abstract void calculateMatrix();

	public abstract void calculateInnerMatrix();

	/**
	 * Calculates offset and rotation for screen canvas position, to be used with the internal transformation matrix.
	 * 
	 * @param x
	 *            Cartesian x coordinate.
	 * @param y
	 *            Cartesian y coordinate.
	 * @param inverse
	 *            Indicates back and forward matrix calculation. Inverse is used for point2location, non-inverse for
	 *            location2point.
	 * @return An 1d-2elements-array with x and y.
	 */
	protected abstract float[] getTransformedPosition(float x, float y, boolean inverse);

	public abstract float[] getObjectFromInnerObjectPosition(float x, float y);

	public abstract float[] getInnerObjectFromObjectPosition(float x, float y);

	public abstract float[] getScreenFromInnerObjectPosition(float x, float y);

	@Deprecated
	public abstract float[] getInnerObjectFromScreenPosition(float x, float y);

	public abstract float[] getInnerObject(ScreenPosition screenPosition);

	public abstract float[] getScreenFromObjectPosition(float x, float y);

	public abstract float[] getObjectFromScreenPosition(float x, float y);

	public abstract Location getLocationFromInnerObjectPosition(float x, float y);

	@Deprecated
	public abstract Location getLocationFromScreenPosition(float x, float y);

	public abstract Location getLocation(float x, float y);

	public abstract Location getLocation(ScreenPosition screenPosition);

	public abstract Location getLocationFromObjectPosition(float x, float y);

	public abstract float[] getInnerObjectFromLocation(Location location);

	@Deprecated
	public abstract float[] getScreenPositionFromLocation(Location location);

	public abstract ScreenPosition getScreenPosition(Location location);

	public abstract ScreenPosition getScreenPositionFloat(Location location);

	public abstract float[] getObjectFromLocation(Location location);

	public PVector getTransformationCenter() {
		return transformationCenter;
	}

	/**
	 * Gets the inner transformation center, in object coordinates.
	 * 
	 * @return The PVector of the center in object coordinates.
	 */
	public PVector getInnerTransformationCenter() {
		return innerTransformationCenter;
	}

	/**
	 * Set outer transformation center.
	 * 
	 * @param transformationCenter
	 *            Point in screen coordinates.
	 */
	public void setTransformationCenter(PVector transformationCenter) {
		// NB Offset subtraction due to special handling (i.e. not included in matrix)
		this.transformationCenter.x = transformationCenter.x - offsetX;
		this.transformationCenter.y = transformationCenter.y - offsetY;
	}

	/**
	 * Set inner transformation center.
	 * 
	 * @param innerTransformationCenter
	 *            Point in screen coordinates.
	 */
	public void setInnerTransformationCenter(PVector innerTransformationCenter) {
		float[] xy = getObjectFromScreenPosition(innerTransformationCenter.x, innerTransformationCenter.y);

		this.innerTransformationCenter.x = xy[0] - (float) innerOffsetX;
		this.innerTransformationCenter.y = xy[1] - (float) innerOffsetY;
	}

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

	// TODO images & pending thread safe?
	public void tileLoaded(Coordinate coord, Object image) {
		if (pending.containsKey(coord) && coord != null && image != null) {
			images.put(coord, image);
			pending.remove(coord);
		} else {
			// Re-adds to queue
			queue.add(coord);
			pending.remove(coord);
		}

		if (pending.size() == 0 && queue.size() == 0) {
			allTilesLoaded = true;
			tilesLoaded();
		} else {
			allTilesLoaded = false;
		}
	}

	/**
	 * Check whether all currently visible tiles have been loaded.
	 * 
	 * @return True if all tiles have been loaded, false otherwise.
	 */
	public boolean allTilesLoaded() {
		return allTilesLoaded;
	}

	/**
	 * Will be called if all tiles have been loaded. Subclasses can implement this method to notify a method in the
	 * client app.
	 */
	public abstract void tilesLoaded();

	// LOAD SORTING
	public class QueueSorter implements Comparator<Coordinate> {
		Coordinate center;

		public void setCenter(Coordinate center) {
			this.center = center;
		}

		public int compare(Coordinate c1, Coordinate c2) {
			if (c1.zoom == center.zoom) {
				if (c2.zoom == center.zoom) {
					// only compare squared distances, saves cpu
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

	protected void cleanupImageBuffer(boolean force) {
		if (force) {
			images.clear();
		} else {
			cleanupImageBuffer();
		}
	}

	/**
	 * Set the map provider, dynamically. The currently selected area, as well as all events etc will stay.
	 * 
	 * Note that the image buffer will be cleaned, i.e. all tiles need to be loaded anew.
	 * 
	 * @param provider
	 *            The provider to use.
	 */
	public void setProvider(AbstractMapProvider provider) {
		this.provider = provider;
		cleanupImageBuffer(true);
	}

	protected void createDefaultMarkerManager(UnfoldingMap map) {
		if (this.defaultMarkerManager == null) {
			this.defaultMarkerManager = new MarkerManager<Marker>();
			this.defaultMarkerManager.setMap(map);
			markerManagerList.add(defaultMarkerManager);
		}
	}

	public List<MarkerManager<Marker>> getMarkerManagerList() {
		return markerManagerList;
	}

	public void removeMarkerManager(MarkerManager<Marker> markerManager) {
		markerManagerList.remove(markerManager);
	}

	public void removeMarkerManager(int i) {
		markerManagerList.remove(i);
	}
	
	public void clearAllMarkers() {
		for (MarkerManager<Marker> mm : markerManagerList) {
			mm.clearMarkers();
		}
	}

}
