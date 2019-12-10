package de.fhpotsdam.unfolding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import de.fhpotsdam.unfolding.events.MapEvent;
import de.fhpotsdam.unfolding.events.MapEventListener;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.mapdisplay.AbstractMapDisplay;
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import de.fhpotsdam.utils.Integrator;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * An interactive map. Uses the MapDisplay, and handles hit test, active status, as well as all interactions such as
 * panning, zooming, and rotating (with or without tweening).
 * 
 * Acts as facade for the map interactions, e.g. using innerScale for zooming, and outerRotate for rotating.
 * 
 * (c) 2015 Till Nagel, and others. See http://unfoldingmaps.org/contact.html for full credits. Licensed under MIT
 * License.
 */
public class UnfoldingMap implements MapEventListener {

	public static final String GREETING_MESSAGE = "Unfolding Map v0.9.93";

	public static final float SCALE_DELTA_IN = 1.05f;
	public static final float SCALE_DELTA_OUT = 1 / 1.05f;

	/** The default tile width of 256px. */
	public static final int TILE_WIDTH = 256;
	/** The default tile height of 256px. */
	public static final int TILE_HEIGHT = 256;
	private static final float PAN_DEFAULT_DELTA = TILE_WIDTH / 2;

	/** UnfoldingMap does not use tweened animation by default. Use {@link #setTweening(boolean)} to switch it on. */
	public static final boolean DEFAULT_TWEENING = false;

	public static final Location PRIME_MERIDIAN_EQUATOR_LOCATION = new Location(0, 0);
	/** The default zoom level of an UnfoldingMap shows the whole world. */
	public static final int DEFAULT_ZOOM_LEVEL = 2;
	private static final float DEFAULT_MIN_SCALE = 1;
	private static final float DEFAULT_MAX_SCALE = 262144; // 2^18

	private static final String MAPCHANGED_METHOD_NAME = "mapChanged";
	private Method mapChangedMethod = null;

	/** The minimum scale. If set the map cannot be further zoomed in. Use {@link #setZoomRange(float, float)}. */
	public float minScale = DEFAULT_MIN_SCALE;
	/** The maximum scale. If set the map cannot be further zoomed out. Use {@link #setZoomRange(float, float)}. */
	public float maxScale = DEFAULT_MAX_SCALE;

	/** The center location of the restricted pan area. */
	protected Location restrictedPanLocation = null;
	/** The maximum distance to the center location of the restricted pan area. */
	protected float maxPanningDistance;

	/** Top left location for rectangular panning restriction. */
	protected Location restrictedRectangularPanningTopLeftLocation = null;
	/** Bottom right location for rectangular panning restriction. */
	protected Location restrictedRectangularPanningBottomRightLocation = null;

	public static Logger log = Logger.getLogger(UnfoldingMap.class);

	/** Whether Unfolding lib showed a greeting message, i.e. the library version. */
	private static boolean greetingMessageDisplayed = false;

	/** The Processing applet. */
	protected PApplet p;

	/** The display of the map. */
	public AbstractMapDisplay mapDisplay;

	/** The ID of this map. */
	protected String id;

	/** Indicates whether this map is currently active. May be used for non-direct interactions. */
	protected boolean active = true;

	/** Indicates whether to smoothly animate between mapDisplay states. */
	private boolean tweening = DEFAULT_TWEENING;

	/** Tweens the scale. */
	public Integrator scaleIntegrator = new Integrator(1);

	/** Tweens the position. */
	private Integrator txIntegrator = new Integrator(1);
	private Integrator tyIntegrator = new Integrator(1);

	/**
	 * Creates a new full canvas map with the given ID.
	 * 
	 * @param p
	 *            The main applet.
	 * @param id
	 *            The ID of this map.
	 */
	public UnfoldingMap(PApplet p, String id) {
		this(p, id, 0, 0, p.width, p.height, true, false, null, null);
	}

	/**
	 * Creates a new full canvas map with a generated ID.
	 * 
	 * @param p
	 *            The main applet.
	 */
	public UnfoldingMap(PApplet p) {
		this(p, generateId(), 0, 0, p.width, p.height, true, false, null, null);
	}

	/**
	 * Creates a new full canvas map with tiles from the given provider.
	 * 
	 * @param p
	 *            The main applet.
	 * @param provider
	 *            The map tiles provider to use.
	 */
	public UnfoldingMap(PApplet p, AbstractMapProvider provider) {
		this(p, generateId(), 0, 0, p.width, p.height, true, false, provider, null);
	}

	/**
	 * Creates a new full canvas map with given ID, and with tiles from the given provider.
	 * 
	 * @param p
	 *            The main applet.
	 * @param id
	 *            The ID of this map.
	 * @param provider
	 *            The map tiles provider to use.
	 */
	public UnfoldingMap(PApplet p, String id, AbstractMapProvider provider) {
		this(p, id, 0, 0, p.width, p.height, true, false, provider, null);
	}

	/**
	 * Creates a new map with specific position and dimension.
	 * 
	 * @param p
	 *            The main applet.
	 * @param x
	 *            The x position of this map.
	 * @param y
	 *            The y position of this map.
	 * @param width
	 *            The width of this map.
	 * @param height
	 *            The height of this map.
	 */
	public UnfoldingMap(PApplet p, float x, float y, float width, float height) {
		this(p, generateId(), x, y, width, height, true, false, null, null);
	}

	public UnfoldingMap(PApplet p, float x, float y, float width, float height, String renderer) {
		this(p, generateId(), x, y, width, height, true, false, null, renderer);
	}

	/**
	 * Creates a new map with specific position and dimension.
	 * 
	 * @param p
	 *            The main applet.
	 * @param id
	 *            The ID of this map.
	 * @param x
	 *            The x position of this map.
	 * @param y
	 *            The y position of this map.
	 * @param width
	 *            The width of this map.
	 * @param height
	 *            The height of this map.
	 */
	public UnfoldingMap(PApplet p, String id, float x, float y, float width, float height) {
		this(p, id, x, y, width, height, true, false, null, null);
	}

	public UnfoldingMap(PApplet p, String id, float x, float y, float width, float height, String renderer) {
		this(p, id, x, y, width, height, true, false, null, renderer);
	}

	public UnfoldingMap(PApplet p, String id, float x, float y, float width, float height, boolean useDistortion) {
		this(p, id, x, y, width, height, true, useDistortion, null, null);
	}

	public UnfoldingMap(PApplet p, String id, float x, float y, float width, float height, boolean useDistortion,
			String renderer) {
		this(p, id, x, y, width, height, true, useDistortion, null, renderer);
	}

	/**
	 * Creates a new map with specific position and dimension.
	 * 
	 * @param p
	 *            The main applet.
	 * @param x
	 *            The x position of this map.
	 * @param y
	 *            The y position of this map.
	 * @param width
	 *            The width of this map.
	 * @param height
	 *            The height of this map.
	 * @param provider
	 *            The map tiles provider to use.
	 */
	public UnfoldingMap(PApplet p, float x, float y, float width, float height, AbstractMapProvider provider) {
		this(p, generateId(), x, y, width, height, true, false, provider, null);
	}

	public UnfoldingMap(PApplet p, float x, float y, float width, float height, AbstractMapProvider provider,
			String renderer) {
		this(p, generateId(), x, y, width, height, true, false, provider, renderer);
	}

	public UnfoldingMap(PApplet p, String id, float x, float y, float width, float height, boolean useMask,
			boolean useDistortion, AbstractMapProvider provider) {
		this(p, id, x, y, width, height, true, false, provider, null);
	}

	/**
	 * Creates a new map with specific position and dimension.
	 * 
	 * @param p
	 *            The main applet.
	 * @param id
	 *            The ID of this map.
	 * @param x
	 *            The x position of this map.
	 * @param y
	 *            The y position of this map.
	 * @param width
	 *            The width of this map.
	 * @param height
	 *            The height of this map.
	 * @param useMask
	 *            Whether this map enables using masks (test)
	 * @param useDistortion
	 *            Whether this map enables using distortion (test)
	 * @param provider
	 *            The map tiles provider to use.
	 */
	public UnfoldingMap(PApplet p, String id, float x, float y, float width, float height, boolean useMask,
			boolean useDistortion, AbstractMapProvider provider, String renderer) {
		this.p = p;
		this.id = id;

		if (!greetingMessageDisplayed) {
			PApplet.println(GREETING_MESSAGE);
			greetingMessageDisplayed = true;
		}

		this.mapDisplay = MapDisplayFactory.getMapDisplay(p, id, x, y, width, height, useMask, useDistortion, provider,
				this, renderer);

		// panCenterZoomTo(PRIME_MERIDIAN_EQUATOR_LOCATION, DEFAULT_ZOOM_LEVEL);

		prepareMapChangedMethod();
	}

	/**
	 * Internal method to call listening methods in your application for map changes.
	 */
	protected void prepareMapChangedMethod() {
		// Prepare mapChanged method via reflection
		Class<? extends PApplet> appletClass = p.getClass();
		try {
			// First searches for a method with MapEvent as param
			mapChangedMethod = appletClass.getMethod(MAPCHANGED_METHOD_NAME, MapEvent.class);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
			try {
				// If that failed, searches for a method without
				mapChangedMethod = appletClass.getMethod(MAPCHANGED_METHOD_NAME);
			} catch (SecurityException e2) {
			} catch (NoSuchMethodException e2) {
			}
		}
	}

	/**
	 * Check whether all currently visible tiles have been loaded.
	 * 
	 * @return True if all tiles have been loaded, false otherwise.
	 */
	public boolean allTilesLoaded() {
		return mapDisplay.allTilesLoaded();
	}

	protected static String generateId() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Checks whether the given screen coordinates are on this Map.
	 * 
	 * @param checkX
	 *            The vertical position to check.
	 * @param checkY
	 *            The horizontal position to check.
	 * @return True if map is hit, false otherwise.
	 */
	public boolean isHit(float checkX, float checkY) {
		float[] check = mapDisplay.getObjectFromScreenPosition(checkX, checkY);
		return (check[0] > 0 && check[0] < mapDisplay.getWidth() && check[1] > 0 && check[1] < mapDisplay.getHeight());
	}

	/**
	 * Checks whether the given screen coordinates are on this Map.
	 * 
	 * @param screenPosition
	 *            The position to check.
	 * @return True if map is hit, false otherwise.
	 */
	public boolean isHit(ScreenPosition screenPosition) {
		return isHit(screenPosition.x, screenPosition.y);
	}

	/**
	 * Indicates whether this map is currently active. May be used for non-direct interactions.
	 * 
	 * @return True if this map is active, false otherwise.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the active flag of this map.
	 * 
	 * @param active
	 *            True if this map is active, false otherwise.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Returns the ID of this map.
	 * 
	 * @return A identifying string for this map.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Updates and draws the map. The main method to display this UnfoldingMap.
	 */
	public void draw() {
		updateMap();
		restrictMapToArea();
		mapDisplay.draw();
	}

	/**
	 * Internal method to listens to mapDisplay events. This will call any listening methods in your application.
	 */
	@Override
	public void onManipulation(MapEvent mapEvent) {
		mapEvent.executeManipulationFor(this);

		// Forward map event to application via reflection
		// TODO See https://github.com/tillnagel/unfolding/issues/102
		if (mapChangedMethod != null) {
			try {
				if (mapChangedMethod.getParameterTypes().length > 0) {
					mapChangedMethod.invoke(p, mapEvent);
				} else {
					mapChangedMethod.invoke(p);
				}
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
		}

	}

	/**
	 * Updates the integrators for tweening. Must be called before {@link AbstractMapDisplay#draw()} .
	 */
	public void updateMap() {
		if (tweening) {
			scaleIntegrator.update();
			mapDisplay.innerScale = scaleIntegrator.value;

			txIntegrator.update();
			mapDisplay.innerOffsetX = txIntegrator.value;
			tyIntegrator.update();
			mapDisplay.innerOffsetY = tyIntegrator.value;

			mapDisplay.calculateInnerMatrix();
		}
	}

	// ----------------------------------------------------

	/**
	 * Returns the top left corner of the visible map.
	 * 
	 * @return The geographic location with latitude and longitude.
	 */
	public Location getTopLeftBorder() {
		return mapDisplay.getLocationFromObjectPosition(0, 0);
	}

	/**
	 * Returns the bottom right corner of the visible map.
	 * 
	 * @return The geographic location with latitude and longitude.
	 */
	public Location getBottomRightBorder() {
		return mapDisplay.getLocationFromObjectPosition(mapDisplay.getWidth(), mapDisplay.getHeight());
	}

	/**
	 * Returns the center location of the visible map.
	 * 
	 * @return The geographic location with latitude and longitude.
	 */
	public Location getCenter() {
		return mapDisplay.getLocationFromObjectPosition(mapDisplay.getWidth() / 2, mapDisplay.getHeight() / 2);
	}

	// public Location getLocation(float x, float y) {
	// return mapDisplay.getLocationForScreenPosition(x, y);
	// }

	// @Override
	// public Location getCenterLocation() {
	// Location location = getLocationForObjectPosition(width / 2, height / 2);
	// return location;
	// }
	//
	@Deprecated
	public Location getLocationFromScreenPosition(float x, float y) {
		return getLocation(x, y);
	}

	/**
	 * Converts a position on the screen to a geographic location.
	 * 
	 * @param screenPosition
	 *            The position in screen coordinates.
	 * @return The geographic location with latitude and longitude.
	 */
	public Location getLocation(ScreenPosition screenPosition) {
		return mapDisplay.getLocation(screenPosition);
	}

	/**
	 * Converts a position on the screen to a geographic location.
	 * 
	 * @param x
	 *            The x position in screen coordinates.
	 * @param y
	 *            The y position in screen coordinates.
	 * @return The geographic location with latitude and longitude.
	 */
	public Location getLocation(float x, float y) {
		return mapDisplay.getLocation(new ScreenPosition(x, y));
	}

	@Deprecated
	public float[] getScreenPositionFromLocation(Location location) {
		return mapDisplay.getScreenPositionFromLocation(location);
	}

	/**
	 * Converts the geographic location to a position on the screen.
	 * 
	 * @param location
	 *            The geographic location with latitude and longitude.
	 * @return The screen position.
	 */
	public ScreenPosition getScreenPosition(Location location) {
		return mapDisplay.getScreenPosition(location);
	}

	// public PVector getScreenPositionFromLocation(Location location) {
	// float[] xy = mapDisplay.getScreenPositionFromLocation(location);
	// return new PVector(xy[0], xy[1]);
	// }

	// Transformations ----------------------------------------------------

	/**
	 * Rotates the map by given angle.
	 * 
	 * <p>
	 * Use {@link #outerRotate(float)} to rotate the map container. Check RotatableMapApp in examples for a comparison.
	 * </p>
	 * 
	 * @param angle
	 *            The angle to rotate the map by.
	 */
	public void rotate(float angle) {
		innerRotate(angle);
	}

	/**
	 * Rotates the map to the given angle, that is it is rotated to the target angle.
	 * 
	 * @param angle
	 *            The angle to rotate the map to.
	 */
	public void rotateTo(float angle) {
		setInnerRotate(angle);
	}

	/**
	 * Zooms to the given level. Map tiles will be non-scaled.
	 * 
	 * @param level
	 *            The level to zoom to.
	 */
	public void zoomToLevel(int level) {
		float scale = getScaleFromZoom(level);
		setInnerScale(scale);
	}

	public void zoomTo(float zoom) {
		float scale = getScaleFromZoom(zoom);
		setInnerScale(scale);
	}

	/**
	 * Zooms in or out one or more levels. Map tiles will be non-scaled.
	 * 
	 * @param levelDelta
	 *            The number of levels to zoom in or out.
	 */
	public void zoomLevel(int levelDelta) {
		int newLevel = getZoomLevelFromScale(mapDisplay.innerScale) + levelDelta;
		zoomToLevel(newLevel);
	}

	/**
	 * @deprecated Replaced by {@link #zoomLevel(int)}
	 * @param levelDelta
	 *            The number of levels to zoom in or out.
	 */
	@Deprecated
	public void zoom(int levelDelta) {
		zoomLevel(levelDelta);
	}

	/**
	 * Zooms in a level.
	 */
	public void zoomLevelIn() {
		zoom(1);
	}

	/**
	 * Zooms out a level.
	 */
	public void zoomLevelOut() {
		zoom(-1);
	}

	/**
	 * Zooms in or out. Map tiles may be scaled.
	 * 
	 * @param scaleDelta
	 *            The scale to zoom by.
	 */
	public void zoom(float scaleDelta) {
		innerScale(scaleDelta);
	}

	/**
	 * Zooms into the map less than a full level. Map tiles will be scaled.
	 */
	public void zoomIn() {
		innerScale(SCALE_DELTA_IN);
	}

	/**
	 * Zooms out of the map less than a full level. Map tiles will be scaled.
	 */
	public void zoomOut() {
		innerScale(SCALE_DELTA_OUT);
	}

	/**
	 * Zooms in around position, and pans to it.
	 * 
	 * After the pan the center still is at the same location. (As innerTransformationCenter is in object coordinates,
	 * thus stays at same inner position.)
	 * 
	 * @param x
	 *            X position to zoom around and pan to (in screen coordinates).
	 * @param y
	 *            Y position to zoom around and pan to (in screen coordinates).
	 * @param level
	 *            Zoom level to zoom to.
	 */
	public void zoomAndPanTo(float x, float y, int level) {
		// NB: Could not be deprecated as switching float/int parameters would be ambiguous!

		// Works only when first zoom around pos, then pan to pos
		mapDisplay.setInnerTransformationCenter(new PVector(x, y));
		zoomToLevel(level);
		panTo(x, y);
	}

	/**
	 * Zooms in around position, and pans to it.
	 * 
	 * @deprecated Use {@link #zoomAndPanTo(int, ScreenPosition)}.
	 * 
	 * @param screenPosition
	 *            ScreenPosition to zoom around and pan to.
	 * @param level
	 *            Zoom level to zoom to.
	 */
	public void zoomAndPanTo(ScreenPosition screenPosition, int level) {
		zoomAndPanTo(level, screenPosition);
	}

	/**
	 * Zooms in around position, and pans to it.
	 * 
	 * After the pan the center still is at the same location. (As innerTransformationCenter is in object coordinates,
	 * thus stays at same inner position.)
	 * 
	 * @param level
	 *            Zoom level to zoom to.
	 * @param screenPosition
	 *            ScreenPosition to zoom around and pan to.
	 */
	public void zoomAndPanTo(int level, ScreenPosition screenPosition) {
		// Works only when first zoom around pos, then pan to pos
		mapDisplay.setInnerTransformationCenter(new PVector(screenPosition.x, screenPosition.y));
		zoomToLevel(level);
		panTo(screenPosition.x, screenPosition.y);
	}

	/**
	 * Zooms in around position, and pans to it.
	 * 
	 * @deprecated Use {@link #zoomAndPanTo(int, Location)}.
	 * 
	 * @param location
	 *            The Location to zoom around and pan to.
	 * @param zoomLevel
	 *            Zoom level to zoom to.
	 */
	public void zoomAndPanTo(Location location, int zoomLevel) {
		zoomAndPanTo(zoomLevel, location);
	}

	/**
	 * Zooms in around position, and pans to it.
	 * 
	 * After the pan the center still is at the same location. (As innerTransformationCenter is in object coordinates,
	 * thus stays at same inner position.)
	 * 
	 * @param zoomLevel
	 *            Zoom level to zoom to.
	 * @param location
	 *            The Location to zoom around and pan to.
	 */
	public void zoomAndPanTo(int zoomLevel, Location location) {
		ScreenPosition pos = mapDisplay.getScreenPosition(location);
		mapDisplay.setInnerTransformationCenter(new PVector(pos.x, pos.y));
		zoomToLevel(zoomLevel);
		panTo(location);
	}

	/**
	 * Pans to the given position. The position will be centered.
	 * 
	 * @param x
	 *            X of the position to pan to, in screen coordinates.
	 * @param y
	 *            Y of the position to pan to, in screen coordinates.
	 */
	public void panTo(float x, float y) {
		float[] objectXY = mapDisplay.getObjectFromScreenPosition(x, y);
		panObjectPositionToObjectCenter(objectXY[0], objectXY[1]);
	}

	/**
	 * Pans to the given screen position. The position will be centered.
	 * 
	 * @param screenPosition
	 *            the position to pan to.
	 */
	public void panTo(ScreenPosition screenPosition) {
		float[] objectXY = mapDisplay.getObjectFromScreenPosition(screenPosition.x, screenPosition.y);
		panObjectPositionToObjectCenter(objectXY[0], objectXY[1]);
	}

	/**
	 * Pans to the given Location. The position of the location will be centered.
	 * 
	 * @param location
	 *            The Location to pan to.
	 */
	public void panTo(Location location) {
		float[] innerXY = mapDisplay.getInnerObjectFromLocation(location);
		float[] objectXY = mapDisplay.getObjectFromInnerObjectPosition(innerXY[0], innerXY[1]);
		panObjectPositionToObjectCenter(objectXY[0], objectXY[1]);
	}

	/**
	 * Pans from point1 to point 2, given in screen coordinates.
	 */
	public void pan(float x1, float y1, float x2, float y2) {
		float[] xy1 = mapDisplay.getObjectFromScreenPosition(x1, y1);
		float[] xy2 = mapDisplay.getObjectFromScreenPosition(x2, y2);

		float dx = xy2[0] - xy1[0];
		float dy = xy2[1] - xy1[1];

		addInnerOffset(dx, dy);
	}

	/**
	 * Pans between two ScreenPosition.
	 * 
	 * @param from
	 *            ScreenPosition to start from.
	 * @param to
	 *            ScreenPosition to pan to.
	 */
	public void pan(ScreenPosition from, ScreenPosition to) {
		float[] xy1 = mapDisplay.getObjectFromScreenPosition(from.x, from.y);
		float[] xy2 = mapDisplay.getObjectFromScreenPosition(to.x, to.y);

		float dx = xy2[0] - xy1[0];
		float dy = xy2[1] - xy1[1];

		addInnerOffset(dx, dy);
	}

	/**
	 * Pans from one location to another one.
	 * 
	 * @param fromLocation
	 *            Origin location to pan from.
	 * @param toLocation
	 *            Destination location to pan to.
	 */
	public void pan(Location fromLocation, Location toLocation) {
		float[] xy1 = mapDisplay.getObjectFromLocation(fromLocation);
		float[] xy2 = mapDisplay.getObjectFromLocation(toLocation);

		float dx = xy2[0] - xy1[0];
		float dy = xy2[1] - xy1[1];

		addInnerOffset(dx, dy);
	}

	/**
	 * Pans by distance in screen coordinates.
	 * 
	 * @param dx
	 *            Horizontal distance in pixel.
	 * @param dy
	 *            Vertical distance in pixel.
	 */
	public void panBy(float dx, float dy) {
		addInnerOffset(dx, dy);
	}

	/**
	 * Pans one tile to the left.
	 */
	public void panLeft() {
		addInnerOffset(PAN_DEFAULT_DELTA, 0);
	}

	/**
	 * Pans one tile to the right.
	 */
	public void panRight() {
		addInnerOffset(-PAN_DEFAULT_DELTA, 0);
	}

	/**
	 * Pans one tile up.
	 */
	public void panUp() {
		addInnerOffset(0, PAN_DEFAULT_DELTA);
	}

	/**
	 * Pans one tile down.
	 */
	public void panDown() {
		addInnerOffset(0, -PAN_DEFAULT_DELTA);
	}

	/**
	 * Moves the map to the given position.
	 * 
	 * <p>
	 * The whole map container is moved. Use one of the panning methods to pan the geographical map.
	 * </p>
	 * 
	 * @param x
	 *            X position in screen coordinates.
	 * @param y
	 *            Y position in screen coordinates.
	 */
	public void move(float x, float y) {
		setOffset(x, y);
	}

	/**
	 * Moves the map to the given ScreenPosition.
	 * 
	 * @param screenPosition
	 *            the ScreenPosition to move to.
	 */
	public void move(ScreenPosition screenPosition) {
		setOffset(screenPosition.x, screenPosition.y);
	}

	/**
	 * Moves the map by the given screen coordinates.
	 * 
	 * @param dx
	 *            The x distance to move by.
	 * @param dy
	 *            The y distance to move by.
	 * 
	 */
	public void moveBy(float dx, float dy) {
		addOffset(dx, dy);
	}

	public void zoomAndPanToFitAllMarkers() {
		zoomAndPanToFitMarkers(getMarkers());
	}

	/**
	 * Zooms and pans the map so that all markers are within the view.
	 * 
	 * @param markers
	 *            The markers to fit.
	 */
	public void zoomAndPanToFitMarkers(List<Marker> markers) {
		zoomAndPanToFit(GeoUtils.getLocationsFromMarkers(markers));
	}

	/**
	 * Zooms and pans the map so that the marker is within the view.
	 * 
	 * @param marker
	 *            The marker to fit.
	 */
	public void zoomAndPanToFit(Marker marker) {
		zoomAndPanToFit(GeoUtils.getLocations(marker));
	}

	/**
	 * Zooms and pans the map so that all locations are within the view.
	 * 
	 * @param locations
	 *            A list of locations to fit.
	 */
	public void zoomAndPanToFit(List<Location> locations) {
		Location[] boundingBox = GeoUtils.getBoundingBox(locations);
		List<Location> boundingBoxLocations = Arrays.asList(boundingBox);
		Location centerLocation = GeoUtils.getEuclideanCentroid(boundingBoxLocations);
		ScreenPosition pos = mapDisplay.getScreenPosition(centerLocation);
		mapDisplay.setInnerTransformationCenter(new PVector(pos.x, pos.y));
		zoomToFit(boundingBox);
		panTo(centerLocation);
	}

	public void zoomToFit(List<Location> locations) {
		Location[] boundingBox = GeoUtils.getBoundingBox(locations);
		zoomToFit(boundingBox);
	}

	public void zoomToFit(Location[] boundingBox) {
		ScreenPosition nwPos = mapDisplay.getScreenPositionFloat(boundingBox[0]);
		ScreenPosition sePos = mapDisplay.getScreenPositionFloat(boundingBox[1]);
		float zoomScale = 0.9f / Math.max((sePos.x - nwPos.x) / getWidth(), (sePos.y - nwPos.y) / getHeight());
		innerScale(zoomScale);
	}

	// MarkerManagement -----------------------------------------------

	/**
	 * Add a MarkerManager to the map. Replaces the default MarkerManager if the given one is the first, and the default
	 * one is empty.
	 * 
	 * <p>
	 * Use this if you want to handle multiple independent marker groups. Otherwise, simply use UnfoldingMap's marker
	 * methods directly (such as {@link #addMarker(Marker)}) which handles this internally with the default
	 * MarkerManager.
	 * </p>
	 * 
	 * @param markerManager
	 *            The MarkerManager to add.
	 */
	public void addMarkerManager(MarkerManager<Marker> markerManager) {
		markerManager.setMap(this);
		mapDisplay.addMarkerManager(markerManager);
	}

	/**
	 * Returns the lastly added MarkerManager.
	 * 
	 * @return The last MarkerManager.
	 */
	public MarkerManager<Marker> getLastMarkerManager() {
		return mapDisplay.getLastMarkerManager();
	}

	/**
	 * Returns the default MarkerManager, i.e. the first MarkerManager. This is the one used internally by all
	 * UnfoldingMap's marker methods.
	 * 
	 * @return The default MarkerManager.
	 */
	public MarkerManager<Marker> getDefaultMarkerManager() {
		return mapDisplay.getDefaultMarkerManager();
	}

	public List<MarkerManager<Marker>> getMarkerManagerList() {
		return mapDisplay.getMarkerManagerList();
	}

	public void removeMarkerManager(MarkerManager<Marker> markerManager) {
		mapDisplay.getMarkerManagerList().remove(markerManager);
	}

	public void removeMarkerManager(int i) {
		mapDisplay.getMarkerManagerList().remove(i);
	}

	/**
	 * Clears all markers from the map. (Removes markers from default and all other MarkerManagers.)
	 */
	public void clearAllMarkers() {
		mapDisplay.clearAllMarkers();
	}

	/**
	 * Removes one marker from the map.
	 * 
	 * The marker is removed only from the default MarkerManager. If using multiple MarkerManagers, you need to remove
	 * the marker on your own.
	 * 
	 * @param marker
	 *            The marker to remove.
	 * @return Whether the default MarkerManager contained the given marker.
	 */
	public boolean removeMarker(Marker marker) {
		return mapDisplay.getDefaultMarkerManager().removeMarker(marker);
	}

	/**
	 * Returns the MarkerManager at the given index position.
	 * 
	 * @param index
	 *            The index to get.
	 * @return The MarkerManager at the index, or null if not existing.
	 */
	public MarkerManager<Marker> getMarkerManager(int index) {
		return mapDisplay.getMarkerManager(index);
	}

	/**
	 * Adds one or multiple markers to the map.
	 * 
	 * <p>
	 * <em>Note</em>: Uses the default marker manager. If you have more than one marker manager, use
	 * {@link MarkerManager#addMarker(Marker)} instead.
	 * </p>
	 * 
	 * @param marker
	 *            The marker or markers to add.
	 */
	public void addMarkers(Marker... marker) {
		for (Marker m : marker) {
			mapDisplay.addMarker(m);
		}
	}

	/**
	 * Adds a marker to the map.
	 * 
	 * <p>
	 * <em>Note</em>: Uses the default marker manager. If you have more than one marker manager, use
	 * {@link MarkerManager#addMarker(Marker)} instead.
	 * </p>
	 * 
	 * @param marker
	 *            The marker to add.
	 */
	public void addMarker(Marker marker) {
		addMarkers(marker);
	}

	/**
	 * Adds multiple markers to the map.
	 * 
	 * <p>
	 * <em>Note</em>: Uses the default marker manager. If you have more than one marker manager, use
	 * {@link MarkerManager#addMarkers(List)} instead.
	 * </p>
	 * 
	 * @param markers
	 *            The markers to add.
	 */
	public void addMarkers(List<Marker> markers) {
		mapDisplay.addMarkers(markers);
	}

	/**
	 * Gets markers of the map.
	 * 
	 * <p>
	 * <em>Note</em>: Returns only the markers of the default marker manager. If you have more than one marker manager,
	 * use {@link MarkerManager#addMarkers(List)} instead.
	 * </p>
	 * 
	 * @return The markers.
	 */
	public List<Marker> getMarkers() {
		return mapDisplay.getDefaultMarkerManager().getMarkers();
	}

	/**
	 * Checks whether a marker got hit with the given screen coordinates. Can be used for interactive selection, etc.
	 * This only returns the first found marker. Use {@link #getHitMarker(float, float)} to get all hit markers.
	 * 
	 * <p>
	 * <em>Note</em>: Returns only the markers of the default marker manager. If you have more than one marker manager,
	 * use {@link MarkerManager#getFirstHitMarker(float, float)} instead.
	 * </p>
	 * 
	 * @return The hit marker, or null if none was hit.
	 */
	public Marker getFirstHitMarker(float checkX, float checkY) {
		return mapDisplay.getDefaultMarkerManager().getFirstHitMarker(checkX, checkY);
	}

	/**
	 * Checks whether multiple markers got hit with the given screen coordinates. Can be used for interactive selection,
	 * etc. This returns all found markers. Use {@link #getFirstHitMarker(float, float)} to get only one marker.
	 * 
	 * <p>
	 * <em>Note</em>: Returns only the markers of the default marker manager. If you have more than one marker manager,
	 * use {@link MarkerManager#getHitMarkers(float, float)} instead.
	 * </p>
	 * 
	 * @return All hit markers, or an empty list if none were hit.
	 */
	public List<Marker> getHitMarkers(float checkX, float checkY) {
		return mapDisplay.getDefaultMarkerManager().getHitMarkers(checkX, checkY);
	}

	/**
	 * @deprecated Use {@link #getHitMarkers(float, float)} instead.
	 */
	public List<Marker> getHitMarker(float checkX, float checkY) {
		return getHitMarkers(checkX, checkY);
	}

	// Transformations ------------------------------------

	protected void setOuterRotate(float angle) {
		mapDisplay.angle = angle;
		mapDisplay.calculateMatrix();
	}

	/**
	 * Rotates the map container.
	 */
	public void outerRotate(float angle) {
		mapDisplay.angle += angle;
		mapDisplay.calculateMatrix();
	}

	protected void setInnerRotate(float angle) {
		mapDisplay.innerAngle = angle;
		mapDisplay.calculateInnerMatrix();
	}

	/**
	 * @deprecated Use {@link #rotate(float)} instead.
	 * 
	 * @param angle
	 *            The angle to rotate by.
	 */
	public void innerRotate(float angle) {
		mapDisplay.innerAngle += angle;
		mapDisplay.calculateInnerMatrix();
	}

	protected void outerScale(float scale) {
		mapDisplay.scale *= scale;
		mapDisplay.calculateMatrix();
	}

	protected void setOuterScale(float scale) {
		mapDisplay.scale = scale;
		mapDisplay.calculateMatrix();
	}

	public void innerScale(float scale) {
		// TODO Check max,min scale in TileProvider, not here in UnfoldingMap
		scale = PApplet.constrain(mapDisplay.innerScale * scale, minScale, maxScale);
		if (tweening) {
			scaleIntegrator.target(scale);
		} else {
			mapDisplay.innerScale = scale;
			mapDisplay.calculateInnerMatrix();
		}
	}

	public void setInnerScale(float scale) {
		scale = PApplet.constrain(scale, minScale, maxScale);
		if (tweening) {
			scaleIntegrator.target(scale);
		} else {
			mapDisplay.innerScale = scale;
			mapDisplay.calculateInnerMatrix();
		}
	}

	/**
	 * Gets the current zoom level of the map. Typically ranges from 0 to 18 or higher, depending on the MapProvider.
	 * 
	 * @return The zoom level.
	 */
	public int getZoomLevel() {
		return getZoomLevelFromScale(mapDisplay.innerScale);
	}

	/**
	 * Gets the current zoom of the map. Can be used to scale the size of markers.
	 * 
	 * This is the actual scale of the map (not simply a floating zoom level value). It ranges from 0^2 to 18^2 or
	 * higher. Use {@link #getZoomFromScale(double)} to convert to floating zoom level.
	 * 
	 * @return The zoom value.
	 */
	public float getZoom() {
		return mapDisplay.innerScale;
	}

	/**
	 * Sets the range of map scale factors.
	 */
	public void setScaleRange(float minScale, float maxScale) {
		this.minScale = minScale;
		this.maxScale = maxScale;
	}

	/**
	 * Sets the range of map zoom levels. All zoom interactions will be restricted to these levels.
	 * 
	 * @param minZoomLevel
	 *            The lower zoom level boundary.
	 * @param maxZoomLevel
	 *            The upper zoom level boundary.
	 */
	public void setZoomRange(float minZoomLevel, float maxZoomLevel) {
		this.minScale = getScaleFromZoom(minZoomLevel);
		this.maxScale = getScaleFromZoom(maxZoomLevel);
	}

	/**
	 * Restricts the area this map can pan to in a rectangular fashion. When map.setTweening is set to true the map
	 * animates back to the restricted area.
	 * 
	 * @param topLeftLocation
	 *            The top left corner of the restricted rectangular area.
	 * @param bottomRightLocation
	 *            The bottom right corner of the restricted rectangular area.
	 */
	public void setRectangularPanningRestriction(Location topLeftLocation, Location bottomRightLocation) {
		this.restrictedRectangularPanningTopLeftLocation = topLeftLocation;
		this.restrictedRectangularPanningBottomRightLocation = bottomRightLocation;
	}

	/**
	 * Restricts the area this map can pan to in a radial fashion. When map.setTweening is set to true the map animates
	 * back to the restricted area.
	 * 
	 * @param location
	 *            The center location of the circular restriction area.
	 * @param maxPanningDistance
	 *            The radius of the circular restriction area in kilometers.
	 */
	public void setPanningRestriction(Location location, float maxPanningDistance) {
		this.restrictedPanLocation = location;
		this.maxPanningDistance = maxPanningDistance;
	}

	/**
	 * Frees this map from any panning restriction.
	 * 
	 * Both circular as well as rectangular panning restrictions are reset.
	 */
	public void resetPanningRestriction() {
		this.restrictedPanLocation = null;
		this.restrictedRectangularPanningTopLeftLocation = null;
	}

	/**
	 * Method to perform any current map panning restriction.
	 */
	private void restrictMapToArea() {
		if (restrictedPanLocation == null && restrictedRectangularPanningTopLeftLocation == null) {
			return;
		}

		if (restrictedPanLocation != null) {
			// circular
			Location mapCenter = getCenter();
			double dist = GeoUtils.getDistance(restrictedPanLocation, mapCenter);
			if (dist > maxPanningDistance) {
				float angle = PApplet.degrees((float) GeoUtils.getAngleBetween(restrictedPanLocation, mapCenter));
				float backDist = maxPanningDistance - (float) dist;
				// Pan back, with same angle but negative distance
				Location newLocation = GeoUtils.getDestinationLocation(mapCenter, angle, backDist);
				panTo(newLocation);
			}
		}

		else {
			// rectangular

			Location mapTopLeft = getTopLeftBorder();
			Location mapBottomRight = getBottomRightBorder();

			ScreenPosition mapTopLeftPos = getScreenPosition(mapTopLeft);
			ScreenPosition boundTopLeftPos = getScreenPosition(restrictedRectangularPanningTopLeftLocation);
			if (restrictedRectangularPanningTopLeftLocation.getLon() > mapTopLeft.getLon()) {
				panBy(mapTopLeftPos.x - boundTopLeftPos.x, 0);
			}
			if (restrictedRectangularPanningTopLeftLocation.getLat() < mapTopLeft.getLat()) {
				panBy(0, mapTopLeftPos.y - boundTopLeftPos.y);
			}

			ScreenPosition mapBottomRightPos = getScreenPosition(mapBottomRight);
			ScreenPosition boundBottomRightPos = getScreenPosition(restrictedRectangularPanningBottomRightLocation);
			if (restrictedRectangularPanningBottomRightLocation.getLon() < mapBottomRight.getLon()) {
				panBy(mapBottomRightPos.x - boundBottomRightPos.x, 0);
			}
			if (restrictedRectangularPanningBottomRightLocation.getLat() > mapBottomRight.getLat()) {
				panBy(0, mapBottomRightPos.y - boundBottomRightPos.y);
			}
		}

	}

	// Outer and inner offset ---------------------------------------

	/**
	 * Pans object center to given object position.
	 * 
	 * @param x
	 *            X in object coordinates.
	 * @param y
	 *            Y in object coordinates.
	 */
	protected void panObjectPositionToObjectCenter(float x, float y) {
		float dx = mapDisplay.getWidth() / 2 - x;
		float dy = mapDisplay.getHeight() / 2 - y;
		addInnerOffset(dx, dy);
	}

	protected void addInnerOffset(float dx, float dy) {
		if (tweening) {
			txIntegrator.target(txIntegrator.target + dx);
			tyIntegrator.target(tyIntegrator.target + dy);
		} else {
			mapDisplay.innerOffsetX += dx;
			mapDisplay.innerOffsetY += dy;
			mapDisplay.calculateInnerMatrix();
		}
	}

	protected void setInnerOffset(float x, float y) {
		if (tweening) {
			txIntegrator.target(x);
			tyIntegrator.target(y);
		} else {
			mapDisplay.innerOffsetX = x;
			mapDisplay.innerOffsetY = y;
			mapDisplay.calculateInnerMatrix();
		}
	}

	/**
	 * Sets the offset of the map. Can be used to re-position the map container (not the map content itself).
	 * 
	 * @param x
	 *            The x of the screen position.
	 * @param y
	 *            The y of the screen position.
	 */
	protected void setOffset(float x, float y) {
		mapDisplay.offsetX = x;
		mapDisplay.offsetY = y;
		mapDisplay.calculateMatrix();
	}

	/**
	 * Moves the offset of the map. Can be used to re-position the map container (not the map content itself).
	 * 
	 * @param dx
	 *            The x distance in screen coordinates.
	 * @param dy
	 *            The y distance in screen coordinates.
	 */
	protected void addOffset(float dx, float dy) {
		mapDisplay.offsetX += dx;
		mapDisplay.offsetY += dy;
		mapDisplay.calculateMatrix();
	}

	// --------------------------------------------------------------

	/**
	 * Converts zoom to scale.
	 * 
	 * @param zoom
	 *            The zoom value (between 0 and typically 20+).
	 * @return The scale value (between 0 and 2<sup>zoom</sup>).
	 */
	public static float getScaleFromZoom(float zoom) {
		return (float) Math.pow(2.0f, zoom);
	}

	/**
	 * Converts scale to zoom.
	 * 
	 * @param scale
	 *            The scale value (between 0 and 2<sup>zoom</sup>).
	 * @return The zoom value (between 0 and typically 20+).
	 */
	public static float getZoomFromScale(double scale) {
		return (float) Math.log(scale) / (float) Math.log(2);
	}

	/**
	 * Converts scale to zoom level. Same as {@link #getZoomFromScale(double)} but with integer values.
	 */
	public static int getZoomLevelFromScale(double scale) {
		return Math.round(getZoomFromScale(scale));
	}

	/**
	 * Switches the tweening flag.
	 */
	public void switchTweening() {
		// Use setter method to ensure proper setting of integrators.
		setTweening(!tweening);
	}

	/**
	 * Indicates whether the map currently animates between different states.
	 * 
	 * @return True if tweening is on, false otherwise.
	 */
	public boolean isTweening() {
		return tweening;
	}

	/**
	 * Sets the tweening flag, i.e. whether the map shall animate between different states.
	 * 
	 * @param tweening
	 *            Whether or not the map shall animate.
	 */
	public void setTweening(boolean tweening) {
		if (tweening == this.tweening)
			return;

		this.tweening = tweening;

		if (tweening) {
			// Set current (animated) and target (final) values to same, so there is no unnecessary animation starting
			// after switching to tweening.

			scaleIntegrator.set(mapDisplay.innerScale);
			scaleIntegrator.target(mapDisplay.innerScale);

			txIntegrator.set(mapDisplay.innerOffsetX);
			txIntegrator.target(mapDisplay.innerOffsetX);
			tyIntegrator.set(mapDisplay.innerOffsetY);
			tyIntegrator.target(mapDisplay.innerOffsetY);

		} else {
			// Set zoom and pan directly, even if still in transition animation

			mapDisplay.innerScale = scaleIntegrator.target;

			mapDisplay.innerOffsetX = txIntegrator.target;
			mapDisplay.innerOffsetY = tyIntegrator.target;
		}
	}

	/**
	 * Sets the background color of this map.
	 * 
	 * @param bgColor
	 *            The color in the current colorMode.
	 */
	public void setBackgroundColor(Integer bgColor) {
		this.mapDisplay.setBackgroundColor(bgColor);
	}

	/**
	 * Returns the width of this map.
	 * 
	 * @return The width in pixels.
	 */
	public float getWidth() {
		return mapDisplay.getWidth();
	}

	/**
	 * Returns the height of this map.
	 * 
	 * @return The height in pixels.
	 */
	public float getHeight() {
		return mapDisplay.getHeight();
	}

}
