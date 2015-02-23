package de.fhpotsdam.unfolding.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.data.Feature.FeatureType;
import de.fhpotsdam.unfolding.geo.Location;

/**
 * Reads GeoJSON files and creates Features.
 */
public class GeoJSONReader extends GeoDataReader {

	/**
	 * Parses a GeoJSON String and creates features for them.
	 * 
	 * @param p
	 *            The PApplet.
	 * @param fileName
	 *            The name of the GeoJSON file.
	 * @return A list of features.
	 */
	public static List<Feature> loadData(PApplet p, String fileName) {
		return loadDataFromJSON(p, PApplet.join(p.loadStrings(fileName), ""));
	}

	/**
	 * Parses a GeoJSON String and creates features for them.
	 * 
	 * @param p
	 *            The PApplet.
	 * @param jsonString
	 *            The GeoJSON string containing geometries etc.
	 * @return A list of features.
	 */
	public static List<Feature> loadDataFromJSON(PApplet p, String jsonString) {
		List<Feature> features = new ArrayList<Feature>();

		try {
			JSONObject geoJson = new JSONObject(jsonString);
			JSONArray allFeatures = geoJson.getJSONArray("features");

			for (int i = 0; i < allFeatures.length(); i++) {

				String id = allFeatures.getJSONObject(i).optString("id", UUID.randomUUID().toString());

				JSONObject currJSONObjGeometry = allFeatures.getJSONObject(i).getJSONObject("geometry");
				JSONObject currJSONObjProperties = allFeatures.getJSONObject(i).optJSONObject("properties");

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

					feature.setId(id);
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
			ShapeFeature linesFeature = (ShapeFeature) feature;

			JSONArray coordinates = geometry.getJSONArray("coordinates");
			populateLinesFeature(linesFeature, coordinates);
		}

		if (featureType.equals("MultiLineString")) {
			feature = new MultiFeature();
			MultiFeature multiFeature = (MultiFeature) feature;

			JSONArray lines = geometry.getJSONArray("coordinates");
			for (int i = 0; i < lines.length(); i++) {
				JSONArray coordinates = lines.getJSONArray(i);
				ShapeFeature linesFeature = new ShapeFeature(FeatureType.LINES);
				populateLinesFeature(linesFeature, coordinates);
				multiFeature.addFeature(linesFeature);
			}
		}

		if (featureType.equals("Polygon")) {
			// Creates a single polygon feature
			feature = new ShapeFeature(FeatureType.POLYGON);
			ShapeFeature polygonFeature = (ShapeFeature) feature;
			JSONArray coordinatesArray = geometry.getJSONArray("coordinates");

			// Store main polygon outline (exterior ring)
			JSONArray mainCoordinates = coordinatesArray.getJSONArray(0);
			populatePolygonFeature(polygonFeature, mainCoordinates);

			// Store polygon feature (potentially with interior rings)
			if (coordinatesArray.length() > 1) {
				for (int i = 1; i < coordinatesArray.length(); i++) {
					JSONArray interiorRingCoordinates = coordinatesArray.getJSONArray(i);
					populateInteriorRingsPolygonFeature(polygonFeature, interiorRingCoordinates);
				}
			}

		}

		if (featureType.equals("MultiPolygon")) {
			feature = new MultiFeature();
			MultiFeature multiFeature = (MultiFeature) feature;

			// Creates multiple polygon features
			JSONArray polygons = geometry.getJSONArray("coordinates");
			for (int i = 0; i < polygons.length(); i++) {
				
				// FIXME Handle multi-polygons with holes, too! (see above)
				JSONArray coordinates = polygons.getJSONArray(i).getJSONArray(0);

				ShapeFeature polygonFeature = new ShapeFeature(FeatureType.POLYGON);
				populatePolygonFeature(polygonFeature, coordinates);
				multiFeature.addFeature(polygonFeature);
			}
		}

		if (feature != null && properties != null && properties.length() > 0) {
			setProperties(feature, properties);
		}

		return feature;
	}

	private static void populateLinesFeature(ShapeFeature linesFeature, JSONArray coordinates) throws JSONException {
		for (int i = 0; i < coordinates.length(); i++) {
			JSONArray coords = coordinates.getJSONArray(i);
			double lat = coords.getDouble(1);
			double lon = coords.getDouble(0);
			linesFeature.addLocation(new Location((float) lat, (float) lon));
		}

	}

	private static void populatePolygonFeature(ShapeFeature polygonFeature, JSONArray coordinates) throws JSONException {
		for (int i = 0; i < coordinates.length(); i++) {
			double lon = coordinates.getJSONArray(i).getDouble(0);
			double lat = coordinates.getJSONArray(i).getDouble(1);
			polygonFeature.addLocation(new Location((float) lat, (float) lon));
		}
	}

	private static void populateInteriorRingsPolygonFeature(ShapeFeature polygonFeature, JSONArray coordinates)
			throws JSONException {
		List<Location> interiorRingLocations = new ArrayList<Location>();
		
		for (int i = 0; i < coordinates.length(); i++) {
			double lon = coordinates.getJSONArray(i).getDouble(0);
			double lat = coordinates.getJSONArray(i).getDouble(1);
			interiorRingLocations.add(new Location((float) lat, (float) lon));
		}
		
		polygonFeature.addInteriorRing(interiorRingLocations);
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
