package de.fhpotsdam.unfolding.events;

import org.apache.log4j.Logger;

import processing.core.PVector;
import de.fhpotsdam.unfolding.Map;

public class ZoomMapEvent extends MapEvent {

	protected static Logger log = Logger.getLogger(ZoomMapEvent.class);

	public static final String TYPE_ZOOM = "zoom";

	public static final String ZOOM_BY = "zoomBy";
	public static final String ZOOM_TO = "zoomTo";

	public int zoomLevel;

	public int zoomLevelDelta;

	public PVector transformationCenter;

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

	public PVector getTransformationCenter() {
		return transformationCenter;
	}

	public void setTransformationCenter(PVector transformationCenter) {
		this.transformationCenter = transformationCenter;
	}

	@Override
	public void executeManipulationFor(Map map) {
		if (transformationCenter != null) {
			map.mapDisplay.setInnerTransformationCenter(transformationCenter);
		}

		if (ZOOM_BY.equals(getSubType())) {
			log.debug("Zooming mapDisplay " + map.getId() + " by " + getZoomLevelDelta());
			map.zoom(getZoomLevelDelta());
		} else {
			log.debug("Zooming mapDisplay " + map.getId() + " to " + getZoomLevel());
			map.zoomToLevel(getZoomLevel());
		}
	}

}
