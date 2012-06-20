package de.fhpotsdam.unfolding.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.data.Feature.FeatureType;
import de.fhpotsdam.unfolding.geo.Location;

public class GeoJSONReader {

	public static List<Feature> loadData(PApplet p, String fileName) {
		List<Feature> features = new ArrayList<Feature>();

		try {
			JSONObject geoJson = new JSONObject(PApplet.join(p.loadStrings(fileName), ""));
			JSONArray allFeatures = geoJson.getJSONArray("features");

			for (int i = 0; i < allFeatures.length(); i++) {

				JSONObject currJSONObjGeometry = allFeatures.getJSONObject(i).getJSONObject("geometry");
				JSONObject currJSONObjProperties = allFeatures.getJSONObject(i).getJSONObject("properties");

				if (currJSONObjGeometry != null) {
					Feature feature = null;

					if (currJSONObjGeometry.getString("type").equals("GeometryCollection")) {
						// Collection of multiple geometries
						// Creates independent features, and copies properties of collection to each one.
						// TODO till: Create MultiFeature with GeometryCollections?
						JSONArray currJSONObjGeometries = currJSONObjGeometry.getJSONArray("geometries");
						for (int j = 0; j < currJSONObjGeometries.length(); j++) {
							feature = getFeatureByType(currJSONObjGeometries.getJSONObject(j), currJSONObjProperties);
							if (feature != null)
								features.add(feature);
						}
					} else {
						// Single geometry
						feature = getFeatureByType(currJSONObjGeometry, currJSONObjProperties);
						if (feature != null) {
							features.add(feature);
						}
					}

				}
			}
		} catch (JSONException e) {
			PApplet.println(e.toString());
		}

		return features;
	}

	private static Feature getFeatureByType(JSONObject geometry, JSONObject properties) throws JSONException {
		Feature feature = null;

		String featureType = geometry.getString("type");
		if (featureType.equals("Point")) {
			feature = new PointFeature();
			PointFeature pointFeature = (PointFeature) feature;

			JSONArray coords = geometry.getJSONArray("coordinates");
			double lat = coords.getDouble(1);
			double lon = coords.getDouble(0);
			pointFeature.setLocation(new Location((float) lat, (float) lon));
		}

		if (featureType.equals("MultiPoint")) {
			PApplet.println("MultiPoint not supported, yet.");
		}

		if (featureType.equals("LineString")) {
			feature = new ShapeFeature(FeatureType.LINES);
			ShapeFeature lineFeature = (ShapeFeature) feature;

			JSONArray coordsArray = geometry.getJSONArray("coordinates");
			for (int i = 0; i < coordsArray.length(); i++) {
				JSONArray coords = coordsArray.getJSONArray(i);
				double lat = coords.getDouble(1);
				double lon = coords.getDouble(0);
				lineFeature.addLocation(new Location((float) lat, (float) lon));
			}
		}

		if (featureType.equals("MultiLineString")) {
			PApplet.println("MultiLineString not supported, yet.");
		}

		if (featureType.equals("Polygon")) {
			feature = new ShapeFeature(FeatureType.POLYGON);
			ShapeFeature polygonFeature = (ShapeFeature) feature;
			
			// Creates a single polygon feature
			JSONArray coordinates = geometry.getJSONArray("coordinates").getJSONArray(0);
			populatePolygonFeature(polygonFeature, coordinates);
		}

		if (featureType.equals("MultiPolygon")) {
			feature = new MultiFeature();
			MultiFeature multiFeature = (MultiFeature) feature;
			
			// Creates multiple polygon features
			JSONArray polygons = geometry.getJSONArray("coordinates");
			for (int i = 0; i < polygons.length(); i++) {
				JSONArray coordinates = polygons.getJSONArray(i).getJSONArray(0);
				
				ShapeFeature polygonFeature = new ShapeFeature(FeatureType.POLYGON);
				populatePolygonFeature(polygonFeature, coordinates);
				multiFeature.addFeature(polygonFeature);
			}
		}

		if (feature != null) {
			setProperties(feature, properties);
		}

		return feature;
	}

	private static void populatePolygonFeature(ShapeFeature polygonFeature, JSONArray coordinates) throws JSONException {
		for (int i = 0; i < coordinates.length(); i++) {
			double lon = coordinates.getJSONArray(i).getDouble(0);
			double lat = coordinates.getJSONArray(i).getDouble(1);
			polygonFeature.addLocation(new Location((float) lat, (float) lon));
		}
	}

	private static void setProperties(Feature feature, JSONObject jsonProperties) {
		JSONArray keys = jsonProperties.names();
		HashMap<String, Object> properties = new HashMap<String, Object>();

		for (int i = 0; i < keys.length(); i++) {
			try {
				String key = String.valueOf(keys.get(i));
				properties.put(key, jsonProperties.get(key));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		feature.setProperties(properties);
	}
}
