package de.fhpotsdam.unfolding.data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.data.Feature.FeatureType;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;

public class MarkerFactory {

	HashMap<FeatureType, Class<? extends Marker>> featureMarkerMap;

	public MarkerFactory() {
		featureMarkerMap = new HashMap<Feature.FeatureType, Class<? extends Marker>>();
		featureMarkerMap.put(FeatureType.POINT, SimplePointMarker.class);
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

		try {
			for (Feature feature : features) {
				Marker marker = createMarker(feature);
				markers.add(marker);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return markers;
	}

	private Marker createMarker(Feature feature) throws Exception {
		Marker marker = null;

		switch (feature.getType()) {
		case POINT:
			marker = createPointMarker((PointFeature) feature);
			break;
		case LINES:
			marker = createLinesMarker((ShapeFeature) feature);
			break;
		case POLYGON:
			marker = createPolygonMarker((ShapeFeature) feature);
			break;
		case MULTI:
			marker = createMultiMarker((MultiFeature) feature);
			break;
		}

		// Set id
		marker.setId(feature.getId());

		// Copy properties
		marker.setProperties(feature.getProperties());

		return marker;
	}

	public void setPointClass(Class<? extends Marker> pointMarkerClass) {
		featureMarkerMap.remove(FeatureType.POINT);
		featureMarkerMap.put(FeatureType.POINT, pointMarkerClass);
	}

	public void setLineClass(Class<? extends Marker> lineMarkerClass) {
		featureMarkerMap.remove(FeatureType.LINES);
		featureMarkerMap.put(FeatureType.LINES, lineMarkerClass);
	}

	public void setPolygonClass(Class<? extends Marker> polygonMarkerClass) {
		featureMarkerMap.remove(FeatureType.POLYGON);
		featureMarkerMap.put(FeatureType.POLYGON, polygonMarkerClass);
	}

	protected Marker createPointMarker(PointFeature feature) throws Exception {
		Class<? extends Marker> markerClass = featureMarkerMap.get(feature.getType());
		Marker marker = null;
		try {
			Constructor<? extends Marker> markerConstructor = markerClass.getDeclaredConstructor(Location.class, HashMap.class);
			marker = (Marker) markerConstructor.newInstance(feature.getLocation(), feature.getProperties());
		} catch (NoSuchMethodException e) {
			Constructor<? extends Marker> markerConstructor = markerClass.getDeclaredConstructor(Location.class);
			marker = (Marker) markerConstructor.newInstance(feature.getLocation());
			marker.setProperties(feature.getProperties());
		}
		return marker;
	}

	protected Marker createLinesMarker(ShapeFeature feature) throws Exception {
		Class<? extends Marker> markerClass = featureMarkerMap.get(feature.getType());
		Marker marker = null;
		try {
			Constructor<? extends Marker> markerConstructor = markerClass.getDeclaredConstructor(List.class, HashMap.class);
			marker = (Marker) markerConstructor.newInstance(feature.getLocations(), feature.getProperties());
		} catch (NoSuchMethodException e) {
			Constructor<? extends Marker> markerConstructor = markerClass.getDeclaredConstructor(List.class);
			marker = (Marker) markerConstructor.newInstance(feature.getLocations());
			marker.setProperties(feature.getProperties());
		}
		return marker;
	}

	protected Marker createPolygonMarker(ShapeFeature feature) throws Exception {
		Class<? extends Marker> markerClass = featureMarkerMap.get(feature.getType());
		Marker marker = null;
		try {
			Constructor<? extends Marker> markerConstructor = markerClass.getDeclaredConstructor(List.class, HashMap.class);
			marker = (Marker) markerConstructor.newInstance(feature.getLocations(), feature.getProperties());
		} catch (NoSuchMethodException e) {
			Constructor<? extends Marker> markerConstructor = markerClass.getDeclaredConstructor(List.class);
			marker = (Marker) markerConstructor.newInstance(feature.getLocations());
			marker.setProperties(feature.getProperties());
		}
		return marker;
	}

	private Marker createMultiMarker(MultiFeature multiFeature) throws Exception {
		MultiMarker multiMarker = new MultiMarker();
		multiMarker.setProperties(multiFeature.getProperties());

		for (Feature feature : multiFeature.getFeatures()) {
			Marker marker = createMarker(feature);
			multiMarker.addMarkers(marker);
		}

		return multiMarker;
	}
}
