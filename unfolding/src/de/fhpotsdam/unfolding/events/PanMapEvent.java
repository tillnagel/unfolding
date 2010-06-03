package de.fhpotsdam.unfolding.events;

import org.apache.log4j.Logger;

import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;

public class PanMapEvent extends MapEvent {

	public static Logger log = Logger.getLogger(PanMapEvent.class);
	
	public static final String PAN = "pan";
	
	public static final String PAN_BY = "panBy";
	public static final String PAN_TO = "panTo";

	Location location;

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

	@Override
	public void executeManipulationFor(Map map) {
		if (PAN_BY.equals(getSubType())) {
			log.debug("Panning mapDisplay " + map.getId() + " by " + getLocation());
			log.warn("Not implemented, yet");
		} else {
			log.debug("Panning mapDisplay " + map.getId() + " to " + getLocation());
			map.panCenterTo(getLocation(), isTweening());
		}
	}

}
