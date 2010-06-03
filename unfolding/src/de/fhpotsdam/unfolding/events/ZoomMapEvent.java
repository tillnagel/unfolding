package de.fhpotsdam.unfolding.events;

import org.apache.log4j.Logger;

import de.fhpotsdam.unfolding.Map;

public class ZoomMapEvent extends MapEvent {

	public static Logger log = Logger.getLogger(ZoomMapEvent.class);

	public static final String ZOOM = "zoom";
	public static final String ZOOM_BY = "zoomBy";
	public static final String ZOOM_TO = "zoomTo";

	public int zoomLevel;

	public int zoomLevelDelta;

	public ZoomMapEvent(Object source, String mapId) {
		super(source, ZOOM, mapId);
	}

	public ZoomMapEvent(Object source, String mapId, String type) {
		this(source, mapId, type, 1);
	}

	public ZoomMapEvent(Object source, String mapId, String type, int zoom) {
		super(source, ZOOM, mapId);
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

	@Override
	public void executeManipulationFor(Map map) {
		if (ZOOM_BY.equals(getSubType())) {
			log.debug("Zooming mapDisplay " + map.getId() + " by " + getZoomLevelDelta());
			map.zoom(getZoomLevelDelta());
		} else {
			log.debug("Zooming mapDisplay " + map.getId() + " to " + getZoomLevel());
			map.zoomToLevel(getZoomLevel());
		}
	}

}
