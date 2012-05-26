package de.fhpotsdam.unfolding.marker;

import java.util.List;

import de.fhpotsdam.unfolding.geo.Location;

public abstract class AbstractMultiMarker extends AbstractMarker{
	protected List<Location> locations;
	
	public void setLocations(List<Location> locations){
		this.locations = locations;
	}
	
	//REVISIT default behavior for getLocation()?
}
