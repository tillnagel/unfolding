package live;

// Java utilities libraries
import java.util.ArrayList;
import java.util.List;

//Processing library
import processing.core.PApplet;
import processing.core.PFont;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/* EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 */

public class EarthquakeCityMap extends PApplet {

	// Prevents error in Eclipse
	private static final long serialVersionUID = 1L;

	public static final float THRESHOLD_MODERATE = 5;
	public static final float THRESHOLD_LIGHT = 4;

	int red = color(255, 0, 0);
	int orange = color(255, 165, 0);
	int yellow = color(255, 255, 0);
	int green = color(0, 255, 0);
	int turquoise = color(48, 213, 200);
	int blue = color(0, 0, 255);

	private UnfoldingMap map;

	// USGS feed with magnitude 2.5+ Earthquakes
	private final String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	public void setup() {
		size(950, 600, OPENGL);

		map = new UnfoldingMap(this, 250, 50, 650, 500, new Microsoft.HybridProvider());

		// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
		// earthquakesURL = "2.5_week.atom"

		map.zoomToLevel(2);
		map.setTweening(true);
		MapUtils.createDefaultEventDispatcher(this, map);

		final List<Marker> markers = new ArrayList<Marker>();

		// Collect properties for each earthquake
		final List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);

		for (final PointFeature pt : earthquakes) {
			final SimplePointMarker spm = createMarker(pt);
			markers.add(spm);
		}

		// Add the markers to the map so that they are displayed
		map.addMarkers(markers);
	}

	/*
	 * createMarker: A suggested helper method that takes in an earthquake
	 * feature and returns a SimplePointMarker for that earthquake
	 * 
	 */
	private SimplePointMarker createMarker(final PointFeature feature) {

		// Create a new SimplePointMarker at the location given by the PointFeature
		final SimplePointMarker marker = new SimplePointMarker(feature.getLocation());

		final Object magObj = feature.getProperty("magnitude");
		final float mag = Float.parseFloat(magObj.toString());

		if (mag <= 2.5) {
			marker.setColor(blue);
			marker.setRadius(7);
		} else if (mag >= 2.5 && mag <= 5.4) {
			marker.setColor(turquoise);
			marker.setRadius(10);
		} else if (mag >= 5.5 && mag <= 6.0) {
			marker.setColor(green);
			marker.setRadius(13);
		} else if (mag >= 6.1 && mag <= 6.9) {
			marker.setColor(yellow);
			marker.setRadius(16);
		} else if (mag >= 7.0 && mag <= 7.9) {
			marker.setColor(orange);
			marker.setRadius(19);
		} else if (mag >= 8.0) {
			marker.setColor(red);
			marker.setRadius(22);
		}

		return marker;
	}

	public void draw() {
		background(10);
		map.draw();
		addKey();
	}

	// Helper method to draw the map key/legend in GUI
	private void addKey() {
		fill(235, 158, 52);
		rect(50, 50, 150, 500);

		fill(0);
		textAlign(LEFT, CENTER);
		final PFont tenorite = createFont("tenorite.ttf", 19);
		textFont(tenorite);
		text("MMS Legend", 70, 75);

		textSize(14);
		fill(color(blue));
		ellipse(80, 125, 7, 7);
		fill(color(turquoise));
		ellipse(80, 175, 10, 10);
		fill(color(green));
		ellipse(80, 225, 13, 13);
		fill(color(yellow));
		ellipse(80, 275, 16, 16);
		fill(color(orange));
		ellipse(80, 325, 19, 19);
		fill(color(red));
		ellipse(80, 375, 22, 22);

		fill(0, 0, 0);
		text("< 2.5", 125, 125);
		text("2.5-5.4", 125, 175);
		text("5.5-6.0", 125, 225);
		text("6.1-6.9", 125, 275);
		text("7.0-7.9", 125, 325);
		text("8.0+", 125, 375);

	}
}
