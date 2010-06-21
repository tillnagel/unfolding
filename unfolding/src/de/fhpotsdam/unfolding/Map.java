package de.fhpotsdam.unfolding;

import java.util.UUID;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import processing.core.PVector;
import de.fhpotsdam.unfolding.core.Coordinate;
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
 */
public class Map implements MapEventListener {

	public static final float SCALE_DELTA_IN = 1.05f;
	public static final float SCALE_DELTA_OUT = 1 / 1.05f;

	public static final int TILE_WIDTH = 256;
	public static final int TILE_HEIGHT = 256;
	private static final double PAN_DEFAULT_DELTA = TILE_WIDTH / 2;

	public static final boolean DEFAULT_TWEENING = true;

	public static final Location PRIME_MERIDIAN_EQUATOR_LOCATION = new Location(0, 0);
	public static final int DEFAULT_ZOOM_LEVEL = 2;

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
	private Integrator scaleIntegrator = new Integrator(1);

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
		this(p, UUID.randomUUID().toString(), 0, 0, p.width, p.height, true, false, null);
	}

	public Map(PApplet p, String id, float x, float y, float width, float height) {
		this(p, id, x, y, width, height, true, false, null);
	}

	public Map(PApplet p, String id, float x, float y, float width, float height,
			boolean useDistortion) {
		this(p, id, x, y, width, height, true, useDistortion, null);
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

		panCenterZoomTo(PRIME_MERIDIAN_EQUATOR_LOCATION, DEFAULT_ZOOM_LEVEL);
	}

	public PVector getCenterInScreenCoordinates() {
		// NB: getCenterLocation() does not use outer transformation, thus this is not the same back
		// and forward conversion.
		return mapDisplay.getPointForLocation(mapDisplay.getCenterLocation());
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
	public boolean isHit(int checkX, int checkY) {
		float[] check = mapDisplay.getTransformedPosition(checkX, checkY, true);
		return (check[0] > 0 && check[0] < mapDisplay.width && check[1] > 0 && check[1] < mapDisplay.height);
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
	 * Listens to mapDisplay events and
	 */
	public void onManipulation(MapEvent mapEvent) {
		mapEvent.executeManipulationFor(this);
	}

	public Location getLocation(float x, float y) {
		return mapDisplay.getLocationForPoint(x, y);
	}

	/**
	 * Updates the integrators to tween. Must be called before {@link PMap#draw()}.
	 */
	public void updateMap() {
		if (tweening) {
			scaleIntegrator.update();
			mapDisplay.sc = scaleIntegrator.value;

			txIntegrator.update();
			mapDisplay.tx = txIntegrator.value;
			tyIntegrator.update();
			mapDisplay.ty = tyIntegrator.value;
		}
	}

	/**
	 * Pans the mapDisplay origin to the given position.
	 * 
	 * @param x
	 *            The x position.
	 * @param y
	 *            The y position.
	 */
	public void panOriginTo(double x, double y) {
		float tx = (float) (x / mapDisplay.sc);
		float ty = (float) (y / mapDisplay.sc);
		setPosition(tx, ty, tweening);
	}

	public void panOriginTo(double x, double y, boolean tweening) {
		float tx = (float) (x / mapDisplay.sc);
		float ty = (float) (y / mapDisplay.sc);
		setPosition(tx, ty, tweening);
	}

	/**
	 * Pans the mapDisplay origin with delta vector.
	 * 
	 * @param dx
	 *            Delta x to pan horizontally in screen pixel.
	 * @param dy
	 *            Delta y to pan vertically in screen pixel.
	 * @param tweening
	 *            Whether to animate the panning.
	 */
	public void panOriginDelta(double dx, double dy, boolean tweening) {
		float tx = (float) (mapDisplay.tx + (dx / mapDisplay.sc));
		float ty = (float) (mapDisplay.ty + (dy / mapDisplay.sc));
		setPosition(tx, ty, tweening);
	}

	public void panOriginDelta(double dx, double dy) {
		panOriginDelta(dx, dy, tweening);
	}

	/**
	 * Internal pan method with optional tweening.
	 */
	private void setPosition(float tx, float ty, boolean tweening) {
		if (tweening) {
			txIntegrator.target(tx);
			tyIntegrator.target(ty);
		} else {
			mapDisplay.tx = tx;
			mapDisplay.ty = ty;

			// Set integrator values to support correct tweening after switch
			txIntegrator.target(tx);
			txIntegrator.set(tx);
			tyIntegrator.target(ty);
			tyIntegrator.set(ty);
		}
	}

	/**
	 * Pans the mapDisplay by the distance between the two positions. Used for direct panning, thus
	 * not using tweening, even if global setting is true.
	 */
	public void pan(double oldX, double oldY, double x, double y) {
		double dx = (double) (x - oldX);
		double dy = (double) (y - oldY);
		panOriginDelta(dx, dy, false);
	}

	/**
	 * Pans the mapDisplay, centers at the coordinate.
	 * 
	 * @param coord
	 *            The coordinate to center.
	 */
	public void panCenterTo(Coordinate coord) {
		panCenterTo(coord, tweening);
	}

	public void panCenterTo(Coordinate coord, boolean tweening) {
		// Re-set scale from rounded zoom level (in case of floating zoom levels)
		float scale = getScaleFromZoom(coord.zoom);
		zoomToScale(scale);

		double x = -TILE_WIDTH * coord.column;
		double y = -TILE_HEIGHT * coord.row;
		panOriginTo(x, y, tweening);
	}

	/**
	 * Pans to location.
	 * 
	 * FIXME Works with tweening only if pan before zoom due to scaleIntegrator and mapDisplay.sc.
	 * 
	 */
	public void panCenterZoomTo(Location location, int zoom) {
		panCenterTo(location);
		zoomToLevel(zoom);
	}

	/**
	 * Pans the mapDisplay, centers at the location.
	 * 
	 * @param location
	 *            The location to center.
	 */
	public void panCenterTo(Location location) {
		panCenterTo(location, tweening);
	}

	public void panCenterTo(Location location, boolean tweening) {
		float zoom = getZoomFromScale(mapDisplay.sc);
		Coordinate coordinate = mapDisplay.getMapProvider().locationCoordinate(location).zoomTo(
				zoom);
		panCenterTo(coordinate, tweening);
	}

	/**
	 * Pans the mapDisplay, centers at the given canvas position.
	 * 
	 * @param x
	 * @param y
	 */
	public void panCenterTo(float x, float y) {
		Location location = mapDisplay.getLocationForPoint(x, y);
		panCenterTo(location);
	}

	public void panLeft() {
		panOriginDelta(PAN_DEFAULT_DELTA, 0, tweening);
	}

	public void panRight() {
		panOriginDelta(-PAN_DEFAULT_DELTA, 0, tweening);
	}

	public void panUp() {
		panOriginDelta(0, PAN_DEFAULT_DELTA, tweening);
	}

	public void panDown() {
		panOriginDelta(0, -PAN_DEFAULT_DELTA, tweening);
	}

	/**
	 * Zooms in or out a level. Map tiles will be non-scaled.
	 * 
	 * @param levelDelta
	 *            The number of levels to zoom in or out.
	 */
	public void zoom(int levelDelta) {
		int newLevel = getZoomLevelFromScale(mapDisplay.sc) + levelDelta;
		zoomToLevel(newLevel);
	}

	/**
	 * Zooms to the given level. Map tiles will be non-scaled.
	 * 
	 * @param level
	 *            The level to zoom to.
	 */
	public void zoomToLevel(int level) {
		float scale = getScaleFromZoom(level);
		zoomToScale(scale);
	}

	public static float getScaleFromZoom(float zoom) {
		return (float) Math.pow(2.0f, zoom);
	}

	public static float getZoomFromScale(double scale) {
		return (float) Math.log(scale) / (float) Math.log(2);
	}

	public static int getZoomLevelFromScale(double scale) {
		return (int) Math.round(Math.log(scale) / Math.log(2));
	}

	/**
	 * Zooms in a level.
	 */
	public void zoomIn() {
		zoom(1);
	}

	/**
	 * Zooms out a level.
	 */
	public void zoomOut() {
		zoom(-1);
	}

	/**
	 * Zooms in or out by changing the scale. Multiplies the scaleDelta with the current scale of
	 * the mapDisplay.
	 * 
	 * @param scaleDelta
	 */
	public void zoomScale(double scaleDelta) {
		zoomToScale((float) (mapDisplay.sc * scaleDelta));
	}

	/**
	 * Internal zoom method with optional tweening.
	 * 
	 * @param scale
	 *            The scale to zoom to.
	 */
	protected void zoomToScale(float scale) {
		if (tweening) {
			scaleIntegrator.target(scale);
		} else {
			mapDisplay.sc = scale;
			// Also update Integrator to support correct tweening after switch
			scaleIntegrator.target(scale);
			scaleIntegrator.set(scale);
		}
	}

	/**
	 * Zooms into the mapDisplay less than a full level. Map tiles will be scaled.
	 */
	public void zoomScaleIn() {
		zoomScale(SCALE_DELTA_IN);
	}

	/**
	 * Zooms out of the mapDisplay less than a full level. Map tiles will be scaled.
	 */
	public void zoomScaleOut() {
		zoomScale(SCALE_DELTA_OUT);
	}

	public void rotate(float diffAngle, PVector center) {
		mapDisplay.transformationCenter = center;
		mapDisplay.angle += diffAngle;
		mapDisplay.calculateMatrix();
	}

	public float getAngle() {
		return mapDisplay.angle;
	}

	public void rotateTo(float angle, PVector center) {
		mapDisplay.transformationCenter = center;
		mapDisplay.angle = angle;
		mapDisplay.calculateMatrix();
	}

	// public void rotate(float diffAngle) {
	// rotate(diffAngle, new PVector(width / 2 + x, height / 2 + y));
	// }
	//
	// public void rotateTo(float angle) {
	// rotateTo(angle, new PVector(width / 2 + x, height / 2 + y));
	// }

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
