package module4;

import java.util.ArrayList;
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
import de.fhpotsdam.unfolding.providers.Yahoo;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * 
 * @author Your name here
 *         Date: July 17, 2015
 */
public class EarthquakeCityMap extends PApplet {

	// We will use member variables, instead of local variables, to store the data
	// that the setUp and draw methods will need to access (as well as other
	// methods)
	// You will use many of these variables, but the only one you should need to add
	// code to modify is countryQuakes, where you will store the number of
	// earthquakes
	// per country.

	// You can ignore this. It's to get rid of eclipse warnings
	private static final long serialVersionUID = 1L;

	/**
	 * This is where to find the local tiles, for working without an Internet
	 * connection
	 */
	public static String mbTilesString = "blankLight-1-3.mbtiles";

	public int red = color(255, 0, 0);
	public int orange = color(255, 165, 0);
	public int yellow = color(255, 255, 0);
	public int green = color(0, 255, 0);
	public int turquoise = color(48, 213, 200);
	public int blue = color(0, 0, 255);

	// feed with magnitude 2.5+ Earthquakes
	private final String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	// The files containing city names and info and country names and info
	private final String cityFile = "city-data.json";
	private final String countryFile = "countries.geo.json";

	// The map
	private UnfoldingMap map;

	// Markers for each city
	private List<Marker> cityMarkers;
	// Markers for each earthquake
	private List<Marker> quakeMarkers;

	// A List of country markers
	private List<Marker> countryMarkers;

	public void setup() {
		// (1) Initializing canvas and map tiles
		size(900, 700, OPENGL);

		map = new UnfoldingMap(this, 250, 50, 600, 600, new Microsoft.HybridProvider());

		// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
		// earthquakesURL = "2.5_week.atom";

		MapUtils.createDefaultEventDispatcher(this, map);

		// FOR TESTING: Set earthquakesURL to be one of the testing files by
		// uncommenting
		// one of the lines below. This will work whether you are online or offline
		// earthquakesURL = "test1.atom";
		// earthquakesURL = "test2.atom";

		// WHEN TAKING THIS QUIZ: Uncomment the next line
		// earthquakesURL = "quiz1.atom";

		// (2) Reading in earthquake data and geometric properties
		// STEP 1: load country features and markers
		final List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);

		// STEP 2: read in city data
		final List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for (final Feature city : cities) {
			cityMarkers.add(new CityMarker(city));
		}

		// STEP 3: read in earthquake RSS feed
		final List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
		quakeMarkers = new ArrayList<Marker>();

		for (final PointFeature feature : earthquakes) {
			// check if LandQuake
			if (isLand(feature)) {
				quakeMarkers.add(new LandQuakeMarker(feature));
			}
			// OceanQuakes
			else {
				quakeMarkers.add(new OceanQuakeMarker(feature));
			}
		}

		// Print country and ocean data (uncomment the next line)
		// printQuakes();

		// (3) Add markers to map
		// NOTE: Country markers are not added to the map. They are used
		// for their geometric properties
		map.addMarkers(quakeMarkers);
		map.addMarkers(cityMarkers);

	} // End setup

	public void draw() {
		background(0);
		map.draw();
		addKey();

	}

	// helper method to draw key in GUI
	// TODO: Update this method as appropriate
	private void addKey() {
		fill(235, 158, 52);
		rect(50, 50, 150, 600);

		fill(0);
		textAlign(LEFT, CENTER);

		final PFont tenorite = createFont("tenorite.ttf", 16);
		textFont(tenorite);
		text("Earthquake Legend", 59, 70);
		line(60, 90, 190, 90);

		textSize(15);

		fill(234, 0, 255);
		triangle(80, 125 - 5, 80 - 5, 125 + 5, 80 + 5, 125 + 5);

		fill(color(255, 255, 255));
		ellipse(80, 175, 20, 20);
		rect(70, 215, 20, 20);

		fill(yellow);
		ellipse(80, 325, 20, 20);

		fill(blue);
		ellipse(80, 375, 20, 20);

		fill(red);
		ellipse(80, 425, 20, 20);

		fill(255, 255, 255);
		ellipse(80, 475, 20, 20);
		
		fill(0, 0, 0);
		text("Megacity", 110, 123);
		text("Land", 110, 173);
		text("Ocean", 110, 223);

		// Update with symbol for proportional
		text("Size ~ Magnitude", 62, 273);
		text("Shallow", 110, 323);
		text("Intermediate", 110, 373);
		text("Deep", 110, 423);
		text("Past Day", 110, 473);

		strokeWeight(2);
		line(70, 465, 90, 485);
		line(90, 465, 70, 485);
	}

	// Checks whether this quake occurred on land. If it did, it sets the
	// "country" property of its PointFeature to the country where it occurred
	// and returns true. Notice that the helper method isInCountry will
	// set this "country" property already. Otherwise it returns false.
	private boolean isLand(final PointFeature earthquake) {

		// Loop over all the country markers.
		// For each, check if the earthquake PointFeature is in the
		// country in m. Notice that isInCountry takes a PointFeature
		// and a Marker as input.
		// If isInCountry ever returns true, isLand should return true.
		for (final Marker m : countryMarkers) {
			if (isInCountry(earthquake, m)) {
				return true;
			}
		}

		return false;
	}

	/*
	 * prints countries with number of earthquakes as
	 * Country1: numQuakes1
	 * Country2: numQuakes2
	 * ...
	 * OCEAN QUAKES: numOceanQuakes
	 */
	private void printQuakes() {
		int oceanQuakeCounter = 0;
		for (final Marker cm : countryMarkers) {
			oceanQuakeCounter = 0;
			int landQuakeCounter = 0;
			for (final Marker m : quakeMarkers) {
				final PointFeature pf = new PointFeature(m.getLocation());
				if (isLand(pf)) {
					if (cm.getProperty("name").equals(m.getProperty("country"))) {
						landQuakeCounter++;
					}
				} else {
					oceanQuakeCounter++;
				}
			}

			if (landQuakeCounter > 0) {
				System.out.println(cm.getProperty("name") + ": " + landQuakeCounter);
			}
		}
		if (oceanQuakeCounter > 0) {
			System.out.println("\nOcean: " + oceanQuakeCounter);
		}

		// TODO: Implement this method
		// One (inefficient but correct) approach is to:
		// Loop over all of the countries, e.g. using
		// for (Marker cm : countryMarkers) { ... }
		//
		// Inside the loop, first initialize a quake counter.
		// Then loop through all of the earthquake
		// markers and check to see whether (1) that marker is on land
		// and (2) if it is on land, that its country property matches
		// the name property of the country marker. If so, increment
		// the country's counter.

		// Here is some code you will find useful:
		//
		// * To get the name of a country from a country marker in variable cm, use:
		// String name = (String)cm.getProperty("name");
		// * If you have a reference to a Marker m, but you know the underlying object
		// is an EarthquakeMarker, you can cast it:
		// EarthquakeMarker em = (EarthquakeMarker)m;
		// Then em can access the methods of the EarthquakeMarker class
		// (e.g. isOnLand)
		// * If you know your Marker, m, is a LandQuakeMarker, then it has a "country"
		// property set. You can get the country with:
		// String country = (String)m.getProperty("country");

	}

	// helper method to test whether a given earthquake is in a given country
	// This will also add the country property to the properties of the earthquake
	// feature if it's in one of the countries.
	// You should not have to modify this code
	private boolean isInCountry(final PointFeature earthquake, final Marker country) {
		// getting location of feature
		final Location checkLoc = earthquake.getLocation();

		// some countries represented it as MultiMarker
		// looping over SimplePolygonMarkers which make them up to use isInsideByLoc
		if (country.getClass() == MultiMarker.class) {

			// looping over markers making up MultiMarker
			for (final Marker marker : ((MultiMarker) country).getMarkers()) {

				// checking if inside
				if (((AbstractShapeMarker) marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));

					// return if is inside one
					return true;
				}
			}
		}

		// check if inside country represented by SimplePolygonMarker
		else if (((AbstractShapeMarker) country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));

			return true;
		}
		return false;
	}

}
