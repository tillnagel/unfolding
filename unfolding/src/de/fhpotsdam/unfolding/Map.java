package de.fhpotsdam.unfolding;

import java.util.UUID;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import processing.core.PVector;
import de.fhpotsdam.unfolding.events.MapEvent;
import de.fhpotsdam.unfolding.events.MapEventListener;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.mapdisplay.AbstractMapDisplay;
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.utils.Integrator;

/**
 * An interactive map. Uses the MapDisplay, and handles hit test, active status, as well as all
 * interactions such as panning, zooming, and rotating (with or without tweening).
 * 
 * Acts as facade for the map interactions, e.g. using innerScale for zooming, and outerRotate for
 * rotating.
 */
public class Map implements MapEventListener {

	public static final float SCALE_DELTA_IN = 1.05f;
	public static final float SCALE_DELTA_OUT = 1 / 1.05f;

	public static final int TILE_WIDTH = 256;
	public static final int TILE_HEIGHT = 256;
	private static final float PAN_DEFAULT_DELTA = TILE_WIDTH / 2;

	public static final boolean DEFAULT_TWEENING = false;

	public static final Location PRIME_MERIDIAN_EQUATOR_LOCATION = new Location(0, 0);
	public static final int DEFAULT_ZOOM_LEVEL = 2;
	private static final float DEFAULT_MIN_SCALE = 1;
	private static final float DEFAULT_MAX_SCALE = 262144; // 2^18

	public float minScale = DEFAULT_MIN_SCALE;
	public float maxScale = DEFAULT_MAX_SCALE;

	public static Logger log = Logger.getLogger(Map.class);

	/** The Processing applet. */
	protected PApplet p;

	/** The display of the map. */
	public AbstractMapDisplay mapDisplay;

	/** The ID of this map. */
	protected String id;

	protected boolean active = true;

	/** Indicates whether to smoothly animate between mapDisplay states. */
	private boolean tweening = DEFAULT_TWEENING;

	/** Tweens the scale. */
	public Integrator scaleIntegrator = new Integrator(1);

	/** Tweens the position. */
	private Integrator txIntegrator = new Integrator(1);
	private Integrator tyIntegrator = new Integrator(1);

	/**
	 * Creates a new full-canvas Map.
	 */
	public Map(PApplet p, String id) {
		this(p, id, 0, 0, p.width, p.height, true, false, null);
	}

	/**
	 * Creates a new full-canvas Map with a generated ID.
	 */
	public Map(PApplet p) {
		this(p, generateId(), 0, 0, p.width, p.height, true, false, null);
	}

	public Map(PApplet p, AbstractMapProvider provider) {
		this(p, generateId(), 0, 0, p.width, p.height, true, false, provider);
	}
	
	public Map(PApplet p, float x, float y, float width, float height) {
		this(p, generateId(), x, y, width, height, true, false, null);
	}

	public Map(PApplet p, String id, float x, float y, float width, float height) {
		this(p, id, x, y, width, height, true, false, null);
	}

	public Map(PApplet p, String id, float x, float y, float width, float height,
			boolean useDistortion) {
		this(p, id, x, y, width, height, true, useDistortion, null);
	}
	
	public Map(PApplet p, float x, float y, float width, float height, AbstractMapProvider provider) {
		this(p, generateId(), x, y, width, height, true, false, provider);
	}

	/**
	 * Creates a Map at given position with given dimension.
	 */
	public Map(PApplet p, String id, float x, float y, float width, float height, boolean useMask,
			boolean useDistortion, AbstractMapProvider provider) {
		this.p = p;
		this.id = id;
		
		this.mapDisplay = MapDisplayFactory.getMapDisplay(p, id, x, y, width, height, useMask,
				useDistortion, provider);

		// panCenterZoomTo(PRIME_MERIDIAN_EQUATOR_LOCATION, DEFAULT_ZOOM_LEVEL);
	}

	private static String generateId() {
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
		return (check[0] > 0 && check[0] < mapDisplay.getWidth() && check[1] > 0 && check[1] < mapDisplay
				.getHeight());
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getId() {
		return id;
	}

	public void draw() {
		updateMap();
		mapDisplay.draw();
	}

	/**
	 * Listens to mapDisplay events.
	 */
	public void onManipulation(MapEvent mapEvent) {
		mapEvent.executeManipulationFor(this);
	}

	/**
	 * Updates the integrators for tweening. Must be called before {@link AbstractMapDisplay#draw()}
	 * .
	 */
	private void updateMap() {
		if (tweening) {
			scaleIntegrator.update();
			mapDisplay.innerScale = scaleIntegrator.value;
			/*
			txIntegrator.update();
			mapDisplay.innerOffsetX = txIntegrator.value;
			tyIntegrator.update();
			mapDisplay.innerOffsetY = tyIntegrator.value;
			*/
			
			mapDisplay.calculateInnerMatrix();
		}
	}

	// ----------------------------------------------------

	public Location getTopLeftBorder() {
		return mapDisplay.getLocationFromObjectPosition(0, 0);
	}

	public Location getBottomRightBorder() {
		return mapDisplay.getLocationFromObjectPosition(mapDisplay.getWidth(), mapDisplay
				.getHeight());
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
	
	public Location getLocationFromScreenPosition(float x, float y) {
		return mapDisplay.getLocationFromScreenPosition(x, y);
	}

	public float[] getScreenPositionFromLocation(Location location) {
		return mapDisplay.getScreenPositionFromLocation(location);
	}

//	public PVector getScreenPositionFromLocation(Location location) {
//		float[] xy = mapDisplay.getScreenPositionFromLocation(location);
//		return new PVector(xy[0], xy[1]);
//	}

	// Transformations ----------------------------------------------------

	/**
	 * Rotates the map by given angle.
	 * 
	 * Whole map will be rotated, not only the inner display.
	 * 
	 * @param angle
	 *            The angle to rotate the map by.
	 */
	public void rotate(float angle) {
		innerRotate(angle);
	}

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

	/**
	 * Zooms in or out one or more levels. Map tiles will be non-scaled.
	 * 
	 * @param levelDelta
	 *            The number of levels to zoom in or out.
	 */
	public void zoom(int levelDelta) {
		int newLevel = getZoomLevelFromScale(mapDisplay.innerScale) + levelDelta;
		zoomToLevel(newLevel);
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
	 * After the pan the center still is at the same location. (As innerTransformationCenter is in
	 * object coordinates, thus stays at same inner position.)
	 * 
	 * @param x
	 *            X position to zoom around and pan to (in screen coordinates).
	 * @param y
	 *            Y position to zoom around and pan to (in screen coordinates).
	 * @param level
	 *            Zoom level to zoom to.
	 */
	public void zoomAndPanTo(float x, float y, int level) {
		// Works only when first zoom around pos, then pan to pos

		mapDisplay.setInnerTransformationCenter(new PVector(x, y));
		zoomToLevel(level);
		panTo(x, y);
	}

	public void zoomAndPanTo(Location location, int level) {
		float[] xy = mapDisplay.getScreenPositionFromLocation(location);
		mapDisplay.setInnerTransformationCenter(new PVector(xy[0], xy[1]));
		zoomToLevel(level);
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
	 * Pans from one location to another one.
	 * 
	 * @param fromLocation
	 * @param toLocation
	 */
	public void pan(Location fromLocation, Location toLocation) {
		float[] xy1 = mapDisplay.getObjectFromLocation(fromLocation);
		float[] xy2 = mapDisplay.getObjectFromLocation(toLocation);

		float dx = xy2[0] - xy1[0];
		float dy = xy2[1] - xy1[1];

		addInnerOffset(dx, dy);
	}

	public void panLeft() {
		addInnerOffset(PAN_DEFAULT_DELTA, 0);
	}

	public void panRight() {
		addInnerOffset(-PAN_DEFAULT_DELTA, 0);
	}

	public void panUp() {
		addInnerOffset(0, PAN_DEFAULT_DELTA);
	}

	public void panDown() {
		addInnerOffset(0, -PAN_DEFAULT_DELTA);
	}

	/**
	 * Moves the map to the given position.
	 * 
	 * @param x
	 *            X position in screen coordinates.
	 * @param y
	 *            Y position in screen coordinates.
	 */
	public void move(float x, float y) {
		setOffset(x, y);
	}

	// --------------------------------------------------------------

	// Transformations ------------------------------------

	protected void setOuterRotate(float angle) {
		mapDisplay.angle = angle;
		mapDisplay.calculateMatrix();
	}

	public void outerRotate(float angle) {
		mapDisplay.angle += angle;
		mapDisplay.calculateMatrix();
	}

	protected void setInnerRotate(float angle) {
		mapDisplay.innerAngle = angle;
		mapDisplay.calculateInnerMatrix();
	}

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

	protected void innerScale(float scale) {
		mapDisplay.innerScale *= scale;
		// TODO Check max,min scale in TileProvider, not here in Map
		mapDisplay.innerScale = PApplet.constrain(mapDisplay.innerScale, minScale, maxScale);
		mapDisplay.calculateInnerMatrix();
	}

	protected void setInnerScale(float scale) {
		mapDisplay.innerScale = scale;
		mapDisplay.innerScale = PApplet.constrain(mapDisplay.innerScale, minScale, maxScale);
		// TEST tweening
		scaleIntegrator.target(scale);
		mapDisplay.calculateInnerMatrix();
	}
	
	public float getZoomLevel() {
		return getZoomLevelFromScale(mapDisplay.innerScale);
	}

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
	 * Sets the range of map zoom levels.
	 */
	public void setZoomRange(float minZoom, float maxZoom) {
		this.minScale = getScaleFromZoom(minZoom);
		this.maxScale = getScaleFromZoom(maxZoom);
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

	protected void addOffset(float dx, float dy) {
		mapDisplay.offsetX += dx;
		mapDisplay.offsetY += dy;
		mapDisplay.calculateMatrix();
	}

	protected void addInnerOffset(float dx, float dy) {
		mapDisplay.innerOffsetX += dx;
		mapDisplay.innerOffsetY += dy;
		mapDisplay.calculateInnerMatrix();
	}

	protected void setInnerOffset(float x, float y) {
		mapDisplay.innerOffsetX = x;
		mapDisplay.innerOffsetY = y;
		mapDisplay.calculateInnerMatrix();
	}

	protected void setOffset(float x, float y) {
		mapDisplay.offsetX = x;
		mapDisplay.offsetY = y;
		mapDisplay.calculateMatrix();
	}

	// --------------------------------------------------------------

	public static float getScaleFromZoom(float zoom) {
		return (float) Math.pow(2.0f, zoom);
	}

	public static float getZoomFromScale(double scale) {
		return (float) Math.log(scale) / (float) Math.log(2);
	}

	public static int getZoomLevelFromScale(double scale) {
		return (int) Math.round(getZoomFromScale(scale));
	}

	/**
	 * Switches the tweening.
	 */
	public void switchTweening() {
		this.tweening = !this.tweening;
	}

	public void setTweening(boolean tweening) {
		this.tweening = tweening;
	}

}
