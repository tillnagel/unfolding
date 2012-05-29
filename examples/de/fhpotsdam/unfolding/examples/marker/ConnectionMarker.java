package de.fhpotsdam.unfolding.examples.marker;

import processing.core.PGraphics;
import processing.core.PVector;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

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

		ScreenPosition pos1 = map.mapDisplay.getScreenPosition(fromMarker.getLocation());
		ScreenPosition pos2 = map.mapDisplay.getScreenPosition(toMarker.getLocation());
		pg.line(pos1.x, pos1.y, pos2.x, pos2.y);
	}

	@Override
	public Location getLocation() {
		return fromMarker.getLocation();
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
