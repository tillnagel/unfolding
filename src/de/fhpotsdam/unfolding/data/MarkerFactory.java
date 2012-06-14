package de.fhpotsdam.unfolding.data;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.marker.Marker;

public abstract class MarkerFactory {

	/**
	 * Creates a marker for each feature. Marker depends on feature type.
	 * 
	 * @param features
	 *            The list of features.
	 * @return A list of markers.
	 */
	public List<Marker> createMarkers(List<Feature> features) {
		List<Marker> markers = new ArrayList<Marker>();

		for (Feature feature : features) {
			Marker marker = null;

			switch (feature.getType()) {
			case POINT:
				marker = createPointMarker((PointFeature) feature);
				break;
			case LINES:
				marker = createLinesMarker((MultiFeature) feature);
				break;
			case POLYGON:
				marker = createPolygonMarker((MultiFeature) feature);
				break;
			}

			markers.add(marker);
		}

		return markers;
	}

	protected abstract Marker createPolygonMarker(MultiFeature feature);

	protected abstract Marker createPointMarker(PointFeature feature);

	protected abstract Marker createLinesMarker(MultiFeature feature);

}
