package de.fhpotsdam.unfolding.utils;

public class GeoJsonParser {
	@SuppressWarnings("static-access")
	public void parseFromJSON(){
		
		// variables
		public PApplet p;
		public String filePath;
		public ArrayList <Feature> features = new ArrayList <Feature> ();
 

		if(filePath == null){
			p.println("Please set a file path to your geo.json file using the function FeatureManager.setFilePath(String filePath)");
		}

		try {		
			JSONObject geoJson = new JSONObject(p.join(p.loadStrings(filePath), ""));
			JSONArray allFeatures = geoJson.getJSONArray("features");

	
			for(int i = 0; i < allFeatures.length(); i++){
					
				JSONObject currJSONObjGeometry = allFeatures.getJSONObject(i).getJSONObject("geometry");
				JSONObject currJSONObjProperties = allFeatures.getJSONObject(i).getJSONObject("properties");

				if(currJSONObjGeometry != null){
					if(currJSONObjGeometry.getString("type").equals("GeometryCollection")){
						JSONArray currJSONObjGeometries = currJSONObjGeometry.getJSONArray("geometries");
						for (int j = 0; j < currJSONObjGeometries.length(); j++) {
							getLocationByType(currJSONObjGeometries.getJSONObject(j), currJSONObjProperties);
						}
					}
					else{
						getLocationByType(currJSONObjGeometry, currJSONObjProperties);
					}
				}		
			}
		}
		catch (JSONException e) {
			// if there is something wrong with the JSON file, we'll get a message
			
			p.println(e.toString());
		}
	}	

	@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
	
	// die properties m�ssen hier empfangen werden
	public void getLocationByType(JSONObject geometry, JSONObject properties){
		// println(geometry.getString("type"));

		Feature feature = null;
		String featureType = geometry.getString("type"); 
		
		
		if(featureType.equals("Point")){

			JSONArray coords = geometry.getJSONArray("coordinates");
			
			feature = new FeaturePoint(p);
			FeaturePoint currFeaturePoint = (FeaturePoint) feature;

			try{

				double y = coords.getDouble(0);
				double x = coords.getDouble(1);

				Location currPos = new Location((float)x, (float)y);

				currFeaturePoint.setLocation(currPos);
				// println(currPos.toString());

				features.add(currFeaturePoint);

			}
			catch(JSONException e){
				p.println(e.toString());
			}
		}

		if(featureType.equals("MultiPoint")){

			p.println("Donot know what to do with type: " + geometry.getString("type"));
		}

		if(featureType.equals("LineString")){

			JSONArray coordsArray = geometry.getJSONArray("coordinates");
			

			feature = new FeatureLineString(p);
			FeatureLineString currFeatureLineString = (FeatureLineString) feature;
			
			try{
				ArrayList <Location> locations = new ArrayList <Location> ();
				
				for(int i = 0; i < coordsArray.length(); i++){
					JSONArray coords = coordsArray.getJSONArray(i);
					
					
					double y = coords.getDouble(0);
					double x = coords.getDouble(1);

					Location currLoc = new Location((float)x, (float)y);
					
					locations.add(currLoc);
				}
				currFeatureLineString.setLocations(locations);
				features.add(currFeatureLineString);

			}
			catch(JSONException e){
				p.println(e.toString());
			}
		}

		if(featureType.equals("MultiLineString")){

			p.println("Donot know what to do with type: " + geometry.getString("type"));
		}

		if(featureType.equals("Polygon")){
			JSONArray coords = geometry.getJSONArray("coordinates");
			
			feature = new FeaturePolygon(p);
			FeaturePolygon currFeaturePolygon = (FeaturePolygon) feature;

			try{

				ArrayList <ArrayList >shapes = new ArrayList <ArrayList>();

				for(int i = 0; i < coords.length(); i++){
					ArrayList <Location> currShape = new ArrayList <Location>();

					for(int l = 0; l < coords.getJSONArray(i).length(); l++){

						double y = coords.getJSONArray(i).getJSONArray(l).getDouble(0);
						double x = coords.getJSONArray(i).getJSONArray(l).getDouble(1);

						Location currLoc = new Location((float)x, (float)y);

						currShape.add(currLoc);
					}
					shapes.add(currShape);
				}

				currFeaturePolygon.setShapes(shapes);
				currFeaturePolygon.setType(geometry.getString("type"));
				features.add(currFeaturePolygon);
			}
			catch(JSONException e){
				p.println(e.toString());
			}
		}

		if(featureType.equals("MultiPolygon")){
			JSONArray coords = geometry.getJSONArray("coordinates");
			
			feature = new FeatureMultiPolygon(p);
			FeatureMultiPolygon currFeatureMultiPolygon = (FeatureMultiPolygon) feature;
					
			try{

				ArrayList shapes = new ArrayList();


				for(int i = 0; i < coords.length(); i++){


					for(int l = 0; l < coords.getJSONArray(i).length(); l++){

						Polygon2D currShape = new Polygon2D();

						for(int k = 0; k < coords.getJSONArray(i).getJSONArray(l).length(); k++){


							double y = coords.getJSONArray(i).getJSONArray(l).getJSONArray(k).getDouble(0);
							double x = coords.getJSONArray(i).getJSONArray(l).getJSONArray(k).getDouble(1);

							Vec2D thisCoord = new Vec2D((float)x, (float)y);


							currShape.add(thisCoord);
						}
						shapes.add(currShape);

					}
	
				}
				currFeatureMultiPolygon.setShapes(shapes);
				currFeatureMultiPolygon.setType(geometry.getString("type"));
				features.add(currFeatureMultiPolygon);

			}
			catch(JSONException e){
				p.println(e.toString());
			}
		}
		
		
		// Property setting cool funktion um die Props zu parsen
		// hier einf�gen.
		
		parseProps(feature, properties);
		
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void parseProps(Feature feature, JSONObject properties){
		
		JSONArray keys = properties.names();
		HashMap props = new HashMap();
		
		for(int i = 0; i < keys.length(); i++){
			try {
				props.put((String)keys.get(i), properties.get((String)keys.get(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		feature.setProps(props);	
	}
}
