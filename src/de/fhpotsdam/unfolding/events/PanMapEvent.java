package de.fhpotsdam.unfolding.events;

import org.apache.log4j.Logger;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * A map event for all pan events, such as (relative) panBy, (absolute) panTo, or (relative) panLeft, etc.
 */
public class PanMapEvent extends MapEvent {

	protected static Logger log = Logger.getLogger(PanMapEvent.class);

	public static final String TYPE_PAN = "pan";

	public static final String PAN_BY = "panBy";
	public static final String PAN_TO = "panTo";
	public static final String PAN_LEFT = "panLeft";
	public static final String PAN_RIGHT = "panRight";
	public static final String PAN_UP = "panUp";
	public static final String PAN_DOWN = "panDown";

	protected Location fromLocation;
	protected Location toLocation;

	public PanMapEvent(Object source, String mapId) {
		this(source, mapId, PAN_TO);
	}

	public PanMapEvent(Object source, String mapId, String subType) {
		super(source, TYPE_PAN, mapId);
		setSubType(subType);
	}

	public Location getFromLocation() {
		return fromLocation;
	}

	public void setFromLocation(Location fromLocation) {
		this.fromLocation = fromLocation;
	}

	public Location getToLocation() {
		return toLocation;
	}

	public void setToLocation(Location toLocation) {
		this.toLocation = toLocation;
	}

	@Override
	public void executeManipulationFor(UnfoldingMap map) {
		if (PAN_BY.equals(getSubType())) {
			// log.debug("Panning mapDisplay " + map.getId() + " from " + fromLocation + " to "
			// + toLocation);
			map.pan(fromLocation, toLocation);
		}

		if (PAN_TO.equals(getSubType())) {
			// log.debug("Panning mapDisplay " + map.getId() + " to " + toLocation);
			map.panTo(toLocation);
		}

		// FIXME Use toLocation instead of panLeft, etc to allow listening maps to pan correctly.
		if (PAN_LEFT.equals(getSubType())) {
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
