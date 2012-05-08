package de.fhpotsdam.unfolding.events;

import org.apache.log4j.Logger;

import processing.core.PVector;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class ZoomMapEvent extends MapEvent {

	protected static Logger log = Logger.getLogger(ZoomMapEvent.class);

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

	protected int getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	protected int getZoomLevelDelta() {
		return zoomLevelDelta;
	}

	public void setZoomLevelDelta(int zoomLevelDelta) {
		this.zoomLevelDelta = zoomLevelDelta;
	}

	protected float getZoom() {
		return zoom;
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
	}

	protected float getZoomDelta() {
		return zoomDelta;
	}

	public void setZoomDelta(float zoomDelta) {
		this.zoomDelta = zoomDelta;
	}

	protected Location getTransformationCenterLocation() {
		return transformationCenterLocation;
	}

	public void setTransformationCenterLocation(Location transformationCenterLocation) {
		this.transformationCenterLocation = transformationCenterLocation;
	}

	@Override
	public void executeManipulationFor(Map map) {
		if (transformationCenterLocation != null) {
			ScreenPosition pos = map.mapDisplay.getScreenPosition(transformationCenterLocation);
			PVector transCenter = new PVector(pos.x, pos.y);
			map.mapDisplay.setInnerTransformationCenter(transCenter);
		}

		if (ZOOM_BY_LEVEL.equals(getSubType())) {
			map.zoom(zoomLevelDelta);
		} else if (ZOOM_TO_LEVEL.equals(getSubType())) {
			map.zoomToLevel(zoomLevel);
		} else if (ZOOM_BY.equals(getSubType())) {
			map.zoom(zoomDelta);
		} else if (ZOOM_TO.equals(getSubType())) {
			map.zoomTo(zoom);
		}
	}

}
