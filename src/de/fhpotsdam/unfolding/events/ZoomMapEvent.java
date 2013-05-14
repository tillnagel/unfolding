package de.fhpotsdam.unfolding.events;

import org.apache.log4j.Logger;

import processing.core.PVector;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * A map event for all zoom events, such as (relative) zoomBy and (absolute) zoomTo.
 */
public class ZoomMapEvent extends MapEvent {

	protected static Logger log = Logger.getLogger(ZoomMapEvent.class);

	/** The internal type of this ZoomMapEvent. */
	public static final String TYPE_ZOOM = "zoom";

	public static final String ZOOM_BY_LEVEL = "zoomByLevel";
	public static final String ZOOM_TO_LEVEL = "zoomToLevel";
	public static final String ZOOM_BY = "zoomBy";
	public static final String ZOOM_TO = "zoomTo";

	protected int zoomLevel;
	protected int zoomLevelDelta;
	protected float zoom;
	protected float zoomDelta;

	/** Center to zoom around. Will be used as innerTransCenter (innerZoom). */
	protected Location transformationCenterLocation;

	public ZoomMapEvent(Object source, String mapId) {
		super(source, TYPE_ZOOM, mapId);
	}

	public ZoomMapEvent(Object source, String mapId, String subType) {
		this(source, mapId, subType, 1);
	}

	public ZoomMapEvent(Object source, String mapId, String subType, int zoom) {
		super(source, TYPE_ZOOM, mapId);
		setSubType(subType);
		if (ZOOM_BY_LEVEL.equals(subType)) {
			setZoomLevelDelta(zoom);
		} else if (ZOOM_TO_LEVEL.equals(subType)) {
			setZoomLevel(zoom);
		} else if (ZOOM_BY.equals(subType)) {
			setZoomDelta(zoom);
		} else if (ZOOM_TO.equals(subType)) {
			setZoom(zoom);
		}
	}

	public int getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	public int getZoomLevelDelta() {
		return zoomLevelDelta;
	}

	public void setZoomLevelDelta(int zoomLevelDelta) {
		this.zoomLevelDelta = zoomLevelDelta;
	}

	public float getZoom() {
		return zoom;
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
	}

	public float getZoomDelta() {
		return zoomDelta;
	}

	public void setZoomDelta(float zoomDelta) {
		this.zoomDelta = zoomDelta;
	}

	public Location getTransformationCenterLocation() {
		return transformationCenterLocation;
	}

	/**
	 * Returns the center of the zoom event, i.e. around which geo-location the map was zoomed in or out.
	 * 
	 * @return The center location.
	 */
	public Location getCenter() {
		return getTransformationCenterLocation();
	}

	public void setTransformationCenterLocation(Location transformationCenterLocation) {
		this.transformationCenterLocation = transformationCenterLocation;
	}

	@Override
	public void executeManipulationFor(UnfoldingMap map) {
		if (transformationCenterLocation != null) {
			ScreenPosition pos = map.mapDisplay.getScreenPosition(transformationCenterLocation);
			PVector transCenter = new PVector(pos.x, pos.y);
			map.mapDisplay.setInnerTransformationCenter(transCenter);
		}

		if (ZOOM_BY_LEVEL.equals(getSubType())) {
			map.zoomLevel(zoomLevelDelta);
		} else if (ZOOM_TO_LEVEL.equals(getSubType())) {
			map.zoomToLevel(zoomLevel);
		} else if (ZOOM_BY.equals(getSubType())) {
			map.zoom(zoomDelta);
		} else if (ZOOM_TO.equals(getSubType())) {
			map.zoomTo(zoom);
		}
	}

}
