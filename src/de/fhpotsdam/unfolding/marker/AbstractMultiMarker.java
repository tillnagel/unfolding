package de.fhpotsdam.unfolding.marker;

import java.util.ArrayList;
import java.util.List;

import processing.core.PGraphics;
import processing.core.PVector;

import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;

public abstract class AbstractMultiMarker extends AbstractMarker{
	
	//temp inner class
	public class ObjectPosition extends PVector{
		public ObjectPosition() {
		}
		
		public ObjectPosition(float []xy){
			x = xy[0];
			y = xy[1];
		}
	}
	
	protected List<Location> locations;

	public AbstractMultiMarker() {
		locations = new ArrayList<Location>();
	}
	
	public void setLocations(List<Location> locations){
		this.locations = locations;
	}
	
	public List<Location> getLocations(){
		return locations;
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
	
	public void draw(Map map) {
		super.draw(map);
		
		PGraphics pg = map.mapDisplay.getPG();
		
		List<ObjectPosition> objPositions = new ArrayList<ObjectPosition>();
		
		for(Location loc : getLocations()){
			float[] xy = map.mapDisplay.getInnerObjectFromLocation(loc);
			objPositions.add(new ObjectPosition(xy));
		}
		
		draw(pg, objPositions, map);
	}

	@Override
	public void drawOuter(Map map) {
		super.drawOuter(map);
		
		PGraphics pg = map.mapDisplay.getOuterPG();
		
		List<ObjectPosition> objPositions = new ArrayList<ObjectPosition>();
		
		for(Location loc : getLocations()){
			float[] xy = map.mapDisplay.getObjectFromLocation(loc);
			objPositions.add(new ObjectPosition(xy));
		}
		
		drawOuter(pg, objPositions, map);
	}
	
	/* override these methods to draw your marker dependent of map attributes */
	protected void draw(PGraphics pg, List<ObjectPosition> objPositions, Map map) {
		draw(pg,objPositions);
	}

	protected void drawOuter(PGraphics pg, List<ObjectPosition> objPositions, Map map) {
		drawOuter(pg,objPositions);
	}
	
	/**
	 * Draws these markers in inner object coordinate system.
	 * 
	 * e.g. for labels oriented to the map
	 * 
	 * @param pg
	 *            The PGraphics to draw on
	 * @param objectPositions
	 *            The positions in inner object coordinates.
 	 */

	public abstract void draw(PGraphics pg, List<ObjectPosition> objectPositions);

	/**
	 * Draws these markers in outer object coordinate system.
	 * 
	 * e.g. for labels oriented to the map
	 * 
	 * @param pg
	 *            The PGraphics to draw on
 	 * @param objectPositions
	 *            The positions in outer object coordinates.
	 */
	public abstract void drawOuter(PGraphics pg, List<ObjectPosition> objectPositions);
	

	//REVISIT default behavior for getLocation(), draw(location), drawOuter(location)?
	
	@Override
	public void draw(PGraphics pg, float x, float y) {
	}

	@Override
	public void drawOuter(PGraphics pg, float x, float y) {
	}
}
