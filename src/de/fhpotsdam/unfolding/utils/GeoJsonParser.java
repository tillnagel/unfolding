package de.fhpotsdam.unfolding.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimpleMarker;
import de.fhpotsdam.unfolding.marker.SimplePolyMarker;

public class GeoJsonParser {

	public PApplet p;
	public String filePath;
	public ArrayList<Marker> markers = new ArrayList<Marker>();

	public void parseFromJSON() {

		// variables
		if (filePath == null) {
			PApplet.println("Please set a file path to your geo.json file using the function FeatureManager.setFilePath(String filePath)");
		}

		try {
			JSONObject geoJson = new JSONObject(PApplet.join(
					p.loadStrings(filePath),
					""));
			JSONArray allFeatures = geoJson.getJSONArray("features");

			for (int i = 0; i < allFeatures.length(); i++) {

				JSONObject currJSONObjGeometry = allFeatures.getJSONObject(i)
						.getJSONObject("geometry");
				JSONObject currJSONObjProperties = allFeatures.getJSONObject(i)
						.getJSONObject("properties");

				if (currJSONObjGeometry != null) {
					if (currJSONObjGeometry.getString("type").equals(
							"GeometryCollection")) {
						JSONArray currJSONObjGeometries = currJSONObjGeometry
								.getJSONArray("geometries");
						for (int j = 0; j < currJSONObjGeometries.length(); j++) {
							getLocationByType(
									currJSONObjGeometries.getJSONObject(j),
									currJSONObjProperties);
						}
					} else {
						getLocationByType(currJSONObjGeometry,
								currJSONObjProperties);
					}
				}
			}
		} catch (JSONException e) {
			// if there is something wrong with the JSON file, we'll get a
			// message

			PApplet.println(e.toString());
		}
	}

	// die properties m�ssen hier empfangen werden
	public void getLocationByType(JSONObject geometry, JSONObject properties) {
		// println(geometry.getString("type"));

		Marker marker = null;
		String featureType = geometry.getString("type");

		if (featureType.equals("Point")) {

			JSONArray coords = geometry.getJSONArray("coordinates");

			marker = new SimpleMarker();
			SimpleMarker pointMarker = (SimpleMarker) marker;

			try {
				double y = coords.getDouble(0);
				double x = coords.getDouble(1);

				Location currPos = new Location((float) x, (float) y);

				pointMarker.setLocation(currPos);
				// println(currPos.toString());

				markers.add(pointMarker);

			} catch (JSONException e) {
				PApplet.println(e.toString());
			}
		}

		if (featureType.equals("MultiPoint")) {

			PApplet.println("Donot knfeaturesow what to do with type: "
					+ geometry.getString("type"));
		}

		if (featureType.equals("LineString")) {

			JSONArray coordsArray = geometry.getJSONArray("coordinates");

			marker = new SimpleLinesMarker();
			SimpleLinesMarker lineMarker = (SimpleLinesMarker) marker;

			try {
				for (int i = 0; i < coordsArray.length(); i++) {
					JSONArray coords = coordsArray.getJSONArray(i);

					double y = coords.getDouble(0);
					double x = coords.getDouble(1);

					Location currLoc = new Location((float) x, (float) y);

					lineMarker.addLocations(currLoc);
				}
				markers.add(lineMarker);

			} catch (JSONException e) {
				PApplet.println(e.toString());
			}
		}

		if (featureType.equals("MultiLineString")) {

			PApplet.println("Donot know what to do with type: "
					+ geometry.getString("type"));
		}

		if (featureType.equals("Polygon")) {
			JSONArray coords = geometry.getJSONArray("coordinates");

			marker = new SimplePolyMarker();
			SimplePolyMarker polyMarker = (SimplePolyMarker) marker;

			try {

				// ArrayList <ArrayList >shapes = new ArrayList <ArrayList>();
				//
				// for(int i = 0; i < coords.length(); i++){
				// ArrayList <Location> currShape = new ArrayList <Location>();

				for (int l = 0; l < coords.getJSONArray(0).length(); l++) {

					double y = coords.getJSONArray(0).getJSONArray(l)
							.getDouble(0);
					double x = coords.getJSONArray(0).getJSONArray(l)
							.getDouble(1);

					Location currLoc = new Location((float) x, (float) y);

					polyMarker.addLocations(currLoc);
				}
				// shapes.add(currShape);
				// }

				// polyMarker.setShapes(shapes);
				// polyMarker.setType(featureType);
				markers.add(polyMarker);
			} catch (JSONException e) {
				PApplet.println(e.toString());
			}
		}

		if (featureType.equals("MultiPolygon")) {

			PApplet.println("Not supported yet -  type: " + featureType);

			// JSONArray coords = geometry.getJSONArray("coordinates");
			//
			// marker = new FeatureMultiPolygon(p);
			// geometry.getString("type")FeatureMultiPolygon
			// currFeatureMultiPolygon = (FeatureMultiPolygon) marker;
			//
			// try{
			//
			// ArrayList shapes = new ArrayList();
			//
			//
			// for(int i = 0; i < coords.length(); i++){
			//
			//
			// for(int l = 0; l < coords.getJSONArray(i).length(); l++){
			//
			// Polygon2D currShape = new Polygon2D();
			//
			// for(int k = 0; k <
			// coords.getJSONArray(i).getJSONArray(l).length(); k++){
			//
			//
			// double y =
			// coords.getJSONArray(i).getJSONArray(l).getJSONArray(k).getDouble(0);
			// double x =
			// coords.getJSONArray(i).getJSONArray(l).getJSONArray(k).getDouble(1);
			//
			// Vec2D thisCoord = new Vec2D((float)x, (float)y);
			//
			//
			// currShape.add(thisCoord);
			// }
			// shapes.add(currShape);
			//
			// }
			//
			// }
			// currFeatureMultiPolygon.setShapes(shapes);
			// currFeatureMultiPolygon.setType(geometry.getString("type"));
			// markers.add(currFeatureMultiPolygon);
			//
			// }
			// catch(JSONException e){
			// p.println(e.toString());
			// }
		}

		// Property setting cool funktion um die Props zu parsen
		// hier einf�gen.

		parseProps(marker, properties);
	}

	public void parseProps(Marker marker, JSONObject properties) {

		JSONArray keys = properties.names();
		HashMap<String,String> props = new HashMap<String,String>();

		for (int i = 0; i < keys.length(); i++) {
			try {
				props.put((String) keys.get(i),
						(String)properties.get((String) keys.get(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		marker.setProps(props);
	}
}
