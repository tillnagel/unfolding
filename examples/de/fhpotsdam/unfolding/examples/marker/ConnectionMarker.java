package de.fhpotsdam.unfolding.examples.marker;

import processing.core.PGraphics;
import processing.core.PVector;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractMarker;
import de.fhpotsdam.unfolding.marker.Marker;

public class ConnectionMarker extends AbstractMarker {

	public Marker fromMarker;
	public Marker toMarker;

	public ConnectionMarker(Marker fromMarker, Marker toMarker) {
		this.fromMarker = fromMarker;
		this.toMarker = toMarker;
	}

	@Override
	public void drawOuter(Map map) {
		PGraphics pg = map.mapDisplay.getPG();
		float[] xy1 = map.mapDisplay.getScreenPositionFromLocation(fromMarker.getLocation());
		PVector v1 = new PVector(xy1[0], xy1[1]);
		float[] xy2 = map.mapDisplay.getScreenPositionFromLocation(toMarker.getLocation());
		PVector v2 = new PVector(xy2[0], xy2[1]);
		pg.line(v1.x, v1.y, v2.x, v2.y);
	}

	@Override
	public Location getLocation() {
		return fromMarker.getLocation();
	}

	@Override
	public void draw(PGraphics pg, float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawOuter(PGraphics pg, float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isInside(float checkX, float checkY, float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

}
