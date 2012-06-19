package de.fhpotsdam.unfolding.examples.data.speed;

import de.fhpotsdam.unfolding.data.MarkerFactory;
import de.fhpotsdam.unfolding.data.MultiFeature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.Marker;

public class ColoredLinesMarkerFactory extends MarkerFactory {

	@Override
	protected Marker createLinesMarker(MultiFeature feature) {
		return new ColoredLinesMarker(feature.getLocations(), feature.getProperties());
	}

	// TODO @tillnagel Would be good when empty implementations are not necessary.  
	
	@Override
	protected Marker createPolygonMarker(MultiFeature feature) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Marker createPointMarker(PointFeature feature) {
		// TODO Auto-generated method stub
		return null;
	}

}
