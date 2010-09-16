package de.fhpotsdam.unfolding.events;

import org.apache.log4j.Logger;

import processing.core.PVector;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;

public class PanMapEvent extends MapEvent {

	public static Logger log = Logger.getLogger(PanMapEvent.class);

	public static final String PAN = "pan";

	public static final String PAN_BY = "panBy";
	public static final String PAN_TO = "panTo";
	public static final String PAN_LEFT = "panLeft";
	public static final String PAN_RIGHT = "panRight";
	public static final String PAN_UP = "panUp";
	public static final String PAN_DOWN = "panDown";

	Location location;
	PVector delta;

	public PanMapEvent(Object source, String mapId) {
		super(source, PAN, mapId);
		setSubType(PAN_TO);
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public PVector getDelta() {
		return delta;
	}

	public void setDelta(PVector delta) {
		this.delta = delta;
	}

	@Override
	public void executeManipulationFor(Map map) {
		if (PAN_BY.equals(getSubType())) {
			log.debug("Panning mapDisplay " + map.getId() + " by " + getLocation());
			map.panOriginDelta(getDelta().x, getDelta().y, isTweening());
		} else if (PAN_TO.equals(getSubType())) {
			log.debug("Panning mapDisplay " + map.getId() + " to " + getLocation());
			map.panCenterTo(getLocation(), isTweening());
		} else if (PAN_LEFT.equals(getSubType())) {
			map.panLeft();
		} else if (PAN_RIGHT.equals(getSubType())) {
			map.panRight();
		} else if (PAN_UP.equals(getSubType())) {
			map.panUp();
		} else if (PAN_DOWN.equals(getSubType())) {
			map.panDown();
		}
	}

}
