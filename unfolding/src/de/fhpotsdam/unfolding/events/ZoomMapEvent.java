package de.fhpotsdam.unfolding.events;

import org.apache.log4j.Logger;

import processing.core.PVector;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;

public class ZoomMapEvent extends MapEvent {

	protected static Logger log = Logger.getLogger(ZoomMapEvent.class);

	public static final String TYPE_ZOOM = "zoom";

	public static final String ZOOM_BY = "zoomBy";
	public static final String ZOOM_TO = "zoomTo";

	protected int zoomLevel;

	protected int zoomLevelDelta;
	
	/** Center to zoom around. Will be used as innerTransCenter (innerZoom). */
	protected Location transformationCenterLocation;

	public ZoomMapEvent(Object source, String mapId) {
		super(source, TYPE_ZOOM, mapId);
	}

	public ZoomMapEvent(Object source, String mapId, String type) {
		this(source, mapId, type, 1);
	}

	public ZoomMapEvent(Object source, String mapId, String type, int zoom) {
		super(source, TYPE_ZOOM, mapId);
		setSubType(type);
		if (ZOOM_BY.equals(type)) {
			setZoomLevelDelta(zoom);
		} else {
			setZoomLevel(zoom);
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

	protected Location getTransformationCenterLocation() {
		return transformationCenterLocation;
	}

	public void setTransformationCenterLocation(Location transformationCenterLocation) {
		this.transformationCenterLocation = transformationCenterLocation;
	}

	@Override
	public void executeManipulationFor(Map map) {
		if (transformationCenterLocation != null) {
			float[] xy = map.mapDisplay.getScreenPositionFromLocation(transformationCenterLocation);
			PVector transCenter = new PVector(xy[0], xy[1]);
			map.mapDisplay.setInnerTransformationCenter(transCenter);
		}

		if (ZOOM_BY.equals(getSubType())) {
			log.debug("Zooming mapDisplay " + map.getId() + " by " + zoomLevelDelta);
			map.zoom(zoomLevelDelta);
		} else {
			log.debug("Zooming mapDisplay " + map.getId() + " to " + zoomLevel);
			map.zoomToLevel(zoomLevel);
		}
	}

}
