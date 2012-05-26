package de.fhpotsdam.unfolding.examples.marker.connectionmarker;

import java.util.List;

import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractMultiMarker;
import de.fhpotsdam.unfolding.marker.Marker;

public class ConnectionMarker extends AbstractMultiMarker {

	public ConnectionMarker(Marker fromMarker, Marker toMarker) {
		addLocation(fromMarker.getLocation());
		addLocation(toMarker.getLocation());
	}

	@Override
	public void draw(PGraphics pg, List<ObjectPosition> objectPositions){
	}
	
	@Override
	public void drawOuter(PGraphics pg, List<ObjectPosition> objectPositions){
		ObjectPosition from = objectPositions.get(0);
		ObjectPosition to = objectPositions.get(1);
		pg.line(from.x, from.y, to.x, to.y);
	}
	
	@Override
	public Location getLocation() {
		return getLocation(0);
	}


	@Override
	protected boolean isInside(float checkX, float checkY, float x, float y) {
		return false;
	}
}
