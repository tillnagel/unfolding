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

/**
 * A factory to create markers from features. The factory creates appropriate markers the each feature type, e.g. a
 * polygon marker for a polygon feature, and handle multi-marker from multi-feature, as well.</p>
 * <p>
 * See the following example on how to use this factory to create your own custom markers. For this, set the marker
 * class for each feature type with the {@link #setPointClass(Class)} etc methods.
 * </p>
 * 
 * <pre>
 * MarkerFactory markerFactory = new MarkerFactory();
 * markerFactory.setPolygonClass(MyPolygonMarker.class);
 * List&lt;Marker&gt; markers = markerFactory.createMarkers(features);
 * </pre>
 * <p>
 * By default, this factory creates the simple markers provided by Unfolding, such as {@link SimplePointMarker}.
 * </p>
 */
@SuppressWarnings("rawtypes")
public class MarkerFactory {

	HashMap<FeatureType, Class> featureMarkerMap;

	/**
	 * Creates a new MarkerFactory.
	 */
	public MarkerFactory() {
		featureMarkerMap = new HashMap<Feature.FeatureType, Class>();
		featureMarkerMap.put(FeatureType.POINT, SimplePointMarker.class);
		featureMarkerMap.put(FeatureType.LINES, SimpleLinesMarker.class);
		featureMarkerMap.put(FeatureType.POLYGON, SimplePolygonMarker.class);
	}

	/**
	 * Creates markers for each feature. Marker depends on feature type.
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

	/**
	 * Creates a marker for the feature. Marker depends on feature type.
	 * 
	 * @param feature
	 *            The feature.
	 * @return A marker of the appropriate type with ID and properties.
	 */
	public Marker createMarker(Feature feature) throws Exception {
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

	/**
	 * Sets the marker class for markers to be created for point features.
	 * 
	 * @param pointMarkerClass
	 *            A marker class.
	 */
	public void setPointClass(Class pointMarkerClass) {
		featureMarkerMap.remove(FeatureType.POINT);
		featureMarkerMap.put(FeatureType.POINT, pointMarkerClass);
	}

	/**
	 * Sets the marker class for markers to be created for lines features.
	 * 
	 * @param lineMarkerClass
	 *            A marker class.
	 */
	public void setLineClass(Class lineMarkerClass) {
		featureMarkerMap.remove(FeatureType.LINES);
		featureMarkerMap.put(FeatureType.LINES, lineMarkerClass);
	}

	/**
	 * Sets the marker class for markers to be created for polygon features.
	 * 
	 * @param polygonMarkerClass
	 *            A marker class.
	 */
	public void setPolygonClass(Class polygonMarkerClass) {
		featureMarkerMap.remove(FeatureType.POLYGON);
		featureMarkerMap.put(FeatureType.POLYGON, polygonMarkerClass);
	}

	protected Marker createPointMarker(PointFeature feature) throws Exception {
		Class markerClass = featureMarkerMap.get(feature.getType());
		Marker marker = null;
		try {
			Constructor markerConstructor = markerClass.getDeclaredConstructor(Location.class, HashMap.class);
			marker = (Marker) markerConstructor.newInstance(feature.getLocation(), feature.getProperties());
		} catch (NoSuchMethodException e) {
			Constructor markerConstructor = markerClass.getDeclaredConstructor(Location.class);
			marker = (Marker) markerConstructor.newInstance(feature.getLocation());
			marker.setProperties(feature.getProperties());
		}
		return marker;
	}

	protected Marker createLinesMarker(ShapeFeature feature) throws Exception {
		Class markerClass = featureMarkerMap.get(feature.getType());
		Marker marker = null;
		try {
			Constructor markerConstructor = markerClass.getDeclaredConstructor(List.class, HashMap.class);
			marker = (Marker) markerConstructor.newInstance(feature.getLocations(), feature.getProperties());
		} catch (NoSuchMethodException e) {
			Constructor markerConstructor = markerClass.getDeclaredConstructor(List.class);
			marker = (Marker) markerConstructor.newInstance(feature.getLocations());
			marker.setProperties(feature.getProperties());
		}
		return marker;
	}

	protected Marker createPolygonMarker(ShapeFeature feature) throws Exception {
		Class markerClass = featureMarkerMap.get(feature.getType());
		Marker marker = null;
		try {
			Constructor markerConstructor = markerClass.getDeclaredConstructor(List.class, HashMap.class);
			marker = (Marker) markerConstructor.newInstance(feature.getLocations(), feature.getProperties());
		} catch (NoSuchMethodException e) {
			Constructor markerConstructor = markerClass.getDeclaredConstructor(List.class);
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
