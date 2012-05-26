package de.fhpotsdam.unfolding.marker;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.geo.Location;

public abstract class AbstractMultiMarker extends AbstractMarker{
	
	protected List<Location> locations;

	public AbstractMultiMarker() {
		locations = new ArrayList<Location>();
	}
	
	public void setLocations(List<Location> locations){
		this.locations = locations;
	}
	
	public void addLocation(Location location){
		locations.add(location);
	}
	
	public Location getLocation(int index){
		return locations.get(index);
	}
	
	public void removeLocation(Location location){
		locations.remove(location);
	}
	
	public void removeLocation(int index){
		locations.remove(index);
	}
	
	//REVISIT default behavior for getLocation()?
}
