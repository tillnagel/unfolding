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

@SuppressWarnings("rawtypes")
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

		try{
			
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
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}

		return markers;
	}
	
	
	void setPointClass(Class pointMarkerClass){
		featureMarkerMap.remove(FeatureType.POINT);
		featureMarkerMap.put(FeatureType.POINT, pointMarkerClass);
	}
	
	void setLineClass(Class lineMarkerClass){
		featureMarkerMap.remove(FeatureType.LINES);
		featureMarkerMap.put(FeatureType.LINES, lineMarkerClass);
	}
	
	void setPolygonClass(Class polygonMarkerClass){
		featureMarkerMap.remove(FeatureType.POLYGON);
		featureMarkerMap.put(FeatureType.POLYGON, polygonMarkerClass);
	}
	

	protected Marker createPolygonMarker(MultiFeature feature) throws Exception{
		Marker marker = (Marker) featureMarkerMap.get(feature.getType()).newInstance();
		((AbstractMultiMarker)marker).setLocations(feature.getLocations());
		marker.setProps(feature.getProperties());
		return marker;
	}

	protected Marker createPointMarker(PointFeature feature) throws Exception{
		Marker marker = (Marker) featureMarkerMap.get(feature.getType()).newInstance();
		marker.setLocation(feature.getLocation());
		marker.setProps(feature.getProperties());
		return marker;
	}

	protected Marker createLinesMarker(MultiFeature feature) throws Exception{
		Marker marker = (Marker) featureMarkerMap.get(feature.getType()).newInstance();
		((AbstractMultiMarker)marker).setLocations(feature.getLocations());
		marker.setProps(feature.getProperties());
		return marker;
	}

}
