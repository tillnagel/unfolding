package de.fhpotsdam.unfolding.examples.marker.connectionmarker;

import processing.core.PGraphics;
import processing.core.PVector;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractMarker;
import de.fhpotsdam.unfolding.marker.AbstractMultiMarker;
import de.fhpotsdam.unfolding.marker.Marker;

public class ConnectionMarker extends AbstractMultiMarker {

//	public Marker fromMarker;
//	public Marker toMarker;

	public ConnectionMarker(Marker fromMarker, Marker toMarker) {
		addLocation(fromMarker.getLocation());
		addLocation(toMarker.getLocation());
	}

	@Override
	public void drawOuter(Map map) {
		PGraphics pg = map.mapDisplay.getPG();
		float[] xy1 = map.mapDisplay.getScreenPositionFromLocation(getLocation(0));
		PVector v1 = new PVector(xy1[0], xy1[1]);
		float[] xy2 = map.mapDisplay.getScreenPositionFromLocation(getLocation(1));
		PVector v2 = new PVector(xy2[0], xy2[1]);
		pg.line(v1.x, v1.y, v2.x, v2.y);
	}

	@Override
	public Location getLocation() {
		return getLocation(0);
	}

	@Override
	public void draw(PGraphics pg, float x, float y) {
	}

	@Override
	public void drawOuter(PGraphics pg, float x, float y) {
	}

	@Override
	protected boolean isInside(float checkX, float checkY, float x, float y) {
		return false;
	}

}
