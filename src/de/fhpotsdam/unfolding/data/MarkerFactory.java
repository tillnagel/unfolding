package de.fhpotsdam.unfolding.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.data.Feature.FeatureType;
import de.fhpotsdam.unfolding.marker.AbstractMultiMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimpleMarker;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;

public class MarkerFactory {
	
	HashMap<FeatureType, Class> featureMarkerMap;
	
	public MarkerFactory(){
		featureMarkerMap = new HashMap<Feature.FeatureType, Class>();
		featureMarkerMap.put(FeatureType.POINT, SimpleMarker.class);
		featureMarkerMap.put(FeatureType.LINES, SimpleLinesMarker.class);
		featureMarkerMap.put(FeatureType.POLYGON, SimplePolygonMarker.class);
	}
	
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
			
			try{
				marker = (Marker) featureMarkerMap.get(feature.getType()).newInstance();
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}

			switch (feature.getType()) {
			case POINT:
				PointFeature pointF = (PointFeature)feature;
				marker.setLocation(pointF.getLocation());
				break;
			case LINES:
			case POLYGON:
				MultiFeature multiF = (MultiFeature) feature;
				((AbstractMultiMarker)marker).setLocations(multiF.getLocations());
				break;
			}

			markers.add(marker);
		}

		return markers;
	}

	protected Marker createPolygonMarker(MultiFeature feature) {
		return new SimplePolygonMarker(feature.getLocations());
	}

	protected Marker createPointMarker(PointFeature feature) {
		return new SimpleMarker(feature.getLocation());
	}

	protected Marker createLinesMarker(MultiFeature feature) {
		return new SimpleLinesMarker(feature.getLocations());
	}

}
