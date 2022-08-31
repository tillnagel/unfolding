package module6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;
import processing.core.PFont;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {
	
	// We will use member variables, instead of local variables, to store the data
	// that the setup and draw methods will need to access (as well as other methods)
	// You will use many of these variables, but the only one you should need to add
	// code to modify is countryQuakes, where you will store the number of earthquakes
	// per country.
	
	// You can ignore this.  It's to get rid of eclipse warnings
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFILINE, change the value of this variable to true
	private static final boolean offline = false;
	
	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	// The files containing city names and info and country names and info
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";
	
	// Color variables
	public int red = color(255, 0, 0);
	public int orange = color(255, 165, 0);
	public int yellow = color(255, 255, 0);
	public int green = color(0, 255, 0);
	public int turquoise = color(48, 213, 200);
	public int blue = color(0, 0, 255);
	
	
	// The map
	private UnfoldingMap map;
	
	// Markers for each city
	private List<Marker> cityMarkers;
	// Markers for each earthquake
	private List<Marker> quakeMarkers;

	// A List of country markers
	private List<Marker> countryMarkers;
	
	// NEW IN MODULE 5
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	
	
	public void setup() {		
		// (1) Initializing canvas and map tiles
		size(900, 700, OPENGL);
		if (offline) {
		    map = new UnfoldingMap(this, 0, 0, 900, 700, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom";  // The same feed, but saved August 7, 2015
		}
		else {
			map = new UnfoldingMap(this, 0, 0, 900, 700, new OpenStreetMap.OpenStreetMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
		    //earthquakesURL = "2.5_week.atom";
			earthquakesURL = "quiz2.atom";
		}
		MapUtils.createDefaultEventDispatcher(this, map);
		map.zoomToLevel(2);
		map.setZoomRange(2, 19);
				
		// (2) Reading in earthquake data and geometric properties
	    //     STEP 1: load country features and markers
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		//     STEP 2: read in city data
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for(Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}
	    
		//     STEP 3: read in earthquake RSS feed
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    quakeMarkers = new ArrayList<Marker>();
	    
	    for(PointFeature feature : earthquakes) {
		  //check if LandQuake
		  if(isLand(feature)) {
		    quakeMarkers.add(new LandQuakeMarker(feature));
		  }
		  // OceanQuakes
		  else {
		    quakeMarkers.add(new OceanQuakeMarker(feature));
		  }
	    }

	    // could be used for debugging
	    // printQuakes();
	    sortAndPrint(20);
	 		
	    // (3) Add markers to map
	    //     NOTE: Country markers are not added to the map.  They are used
	    //           for their geometric properties
	    map.addMarkers(quakeMarkers);
	    map.addMarkers(cityMarkers);
	    
	}  // End setup
	
	
	public void draw() {
		background(0);
		map.draw();
		addKey();
		
	}
	
	public void keyPressed() {
		if (key == 'r')
			map.rotate(0.1f);
		if (key == 'l')
			map.rotate(-0.1f);
	}
	
	// TODO: Add the method:
	//   private void sortAndPrint(int numToPrint)
	// and then call that method from setUp

	/** Event handler that gets called automatically when the 
	 * mouse moves.
	 */
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		selectMarkerIfHover(quakeMarkers);
		selectMarkerIfHover(cityMarkers);
	}
	
	// If there is a marker under the cursor, and lastSelected is null 
	// set the lastSelected to be the first marker found under the cursor
	// Make sure you do not select two markers.
	// 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		for (Marker m : markers) {
			if (lastSelected == null) {
				if (m.isInside(map, mouseX, mouseY)) {
					m.setSelected(true);
					lastSelected = (CommonMarker) m;
					break;
				}
			}
		}
		// TODO: Implement this method
	}
	
	/** The event handler for mouse clicks
	 * It will display an earthquake and its threat circle of cities
	 * Or if a city is clicked, it will display all the earthquakes 
	 * where the city is in the threat circle
	 */
	@Override
	public void mouseClicked() {
		// Implement this method
		// Hint: You probably want a helper method or two to keep this code
		// from getting too long/disorganized
		selectMarkerIfClicked(quakeMarkers, cityMarkers);
	}

	@SafeVarargs
	private final void selectMarkerIfClicked(List<Marker>... markers) {
		for (List<Marker> markerList : markers)
			for (Marker marker : markerList)
				if (marker.isInside(map, mouseX, mouseY)) {
					if (lastClicked == marker) {
						showMarkers();
						lastClicked = null;
					} else {
						clickedMarkerHelper((CommonMarker) marker);
						lastClicked = (CommonMarker) marker;
					}
					return;
				}

		// No marker clicked
		showMarkers();
	}

	private void showMarkers() {
		for (Marker m : quakeMarkers)
			m.setHidden(false);
		for (Marker m : cityMarkers)
			m.setHidden(false);
	}

	private void clickedMarkerHelper(CommonMarker markerSelected) {
		Location location = markerSelected.getLocation();
		if (markerSelected instanceof EarthquakeMarker) {
			for (Marker marker : quakeMarkers)
				marker.setHidden(true);

			double threatDistance = ((EarthquakeMarker) markerSelected).threatCircle();
			for (Marker marker : cityMarkers)
				if (marker.getDistanceTo(location) > threatDistance)
					marker.setHidden(true);
		} else {
			for (Marker marker : cityMarkers)
				marker.setHidden(true);

			for (Marker marker : quakeMarkers)
				if (marker.getDistanceTo(location) > ((EarthquakeMarker) marker).threatCircle())
					marker.setHidden(true);
		}
		markerSelected.setHidden(false);
	}
	
	
	// loop over and unhide all markers
	private void unhideMarkers() {
		for(Marker marker : quakeMarkers) {
			marker.setHidden(false);
		}
			
		for(Marker marker : cityMarkers) {
			marker.setHidden(false);
		}
	}
	
	// helper method to draw key in GUI
	private void addKey() {	
		// Remember you can use Processing's graphics methods here
		fill(235, 158, 52);
		rect(5, 5, 150, 290, 10);

		fill(0);

		final PFont tenorite = createFont("tenorite.ttf", 16);
		textFont(tenorite);
		text("Earthquake Legend", 14, 25);
		line(15, 35, 145, 35);

		textSize(15);

		fill(234, 0, 255);
		int x = 35;
		int y = 65;
		
		// triangle(x, y - 5, x - 5, y + 5, x + 5, y + 5);

		fill(color(255, 255, 255));
		ellipse(35, 95, 20, 20);
		rect(25, 116, 20, 20);

		fill(yellow);
		ellipse(35, 155, 20, 20);

		fill(blue);
		ellipse(35, 185, 20, 20);

		fill(red);
		ellipse(35, 215, 20, 20);

		fill(255, 255, 255);
		ellipse(35, 245, 20, 20);
		
		fill(0, 0, 0);
		text("Megacity", 60, 70);
		text("Land", 60, 100);
		text("Ocean", 60, 130);

		text("Shallow", 60, 160);
		text("Intermediate", 60, 190);
		text("Deep", 60, 220);
		text("Past Day", 60, 250);

		// TODO: Update with symbol for proportional
		text("Size ~ Magnitude", 16, 280);
		
		strokeWeight(2);
		line(25, 235, 45, 255);
		line(45, 235, 25, 255);
		
		pushStyle();
		fill(234, 0, 255);
		strokeWeight(8);
		stroke(234, 0, 255, 100);
		noFill();
		ellipse(x, y, 6, 6);
		popStyle();
	}

	
	
	// Checks whether this quake occurred on land.  If it did, it sets the 
	// "country" property of its PointFeature to the country where it occurred
	// and returns true.  Notice that the helper method isInCountry will
	// set this "country" property already.  Otherwise it returns false.	
	private boolean isLand(PointFeature earthquake) {
		
		// IMPLEMENT THIS: loop over all countries to check if location is in any of them
		// If it is, add 1 to the entry in countryQuakes corresponding to this country.
		for (Marker country : countryMarkers) {
			if (isInCountry(earthquake, country)) {
				return true;
			}
		}
		
		// not inside any country
		return false;
	}
	
	// prints countries with number of earthquakes
	private void printQuakes() {
		int totalWaterQuakes = quakeMarkers.size();
		for (Marker country : countryMarkers) {
			String countryName = country.getStringProperty("name");
			int numQuakes = 0;
			for (Marker marker : quakeMarkers)
			{
				EarthquakeMarker eqMarker = (EarthquakeMarker)marker;
				if (eqMarker.isOnLand()) {
					if (countryName.equals(eqMarker.getStringProperty("country"))) {
						numQuakes++;
					}
				}
			}
			if (numQuakes > 0) {
				totalWaterQuakes -= numQuakes;
				System.out.println(countryName + ": " + numQuakes);
			}
		}
		System.out.println("OCEAN QUAKES: " + totalWaterQuakes);
	}
	
	private void sortAndPrint(int numToPrint) {
		System.out.println("\nTop " + numToPrint + " Highest Magnitudes");
		System.out.println("--------------------------------------------");
		Object[] earthquakeMarkers = new EarthquakeMarker[quakeMarkers.size()];
		earthquakeMarkers = quakeMarkers.toArray();
		Arrays.sort(earthquakeMarkers, Collections.reverseOrder());
		if (numToPrint > earthquakeMarkers.length) {
			for (int i = 0; i < earthquakeMarkers.length; i++) {
				System.out.println(earthquakeMarkers[i]);
			}
		} else if (numToPrint < earthquakeMarkers.length) {
			for (int i = 0; i < numToPrint; i++) {
				System.out.println(earthquakeMarkers[i]);
			}
		}
	}
	
	// helper method to test whether a given earthquake is in a given country
	// This will also add the country property to the properties of the earthquake feature if 
	// it's in one of the countries.
	// You should not have to modify this code
	private boolean isInCountry(PointFeature earthquake, Marker country) {
		// getting location of feature
		Location checkLoc = earthquake.getLocation();

		// some countries represented it as MultiMarker
		// looping over SimplePolygonMarkers which make them up to use isInsideByLoc
		if(country.getClass() == MultiMarker.class) {
				
			// looping over markers making up MultiMarker
			for(Marker marker : ((MultiMarker)country).getMarkers()) {
					
				// checking if inside
				if(((AbstractShapeMarker)marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));
						
					// return if is inside one
					return true;
				}
			}
		}
			
		// check if inside country represented by SimplePolygonMarker
		else if(((AbstractShapeMarker)country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));
			
			return true;
		}
		return false;
	}

}
