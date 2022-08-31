package module6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;
import processing.core.PFont;

/** An applet that shows airports (and routes)
 * on a world map.  
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMap extends PApplet {
	
	UnfoldingMap map;
	private List<Marker> airportList;
	List<Marker> routeList;
	
	public void setup() {
		// setting up PAppler
		size(800,600, OPENGL);
		
		// setting up map and default events
		map = new UnfoldingMap(this, 0, 0, 800, 600);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		map.zoomToLevel(2);
		map.setZoomRange(2, 19);
		
		// get features from airport data
		List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
		
		// list for markers, hashmap for quicker access when matching with routes
		airportList = new ArrayList<Marker>();
		HashMap<Integer, Location> airports = new HashMap<Integer, Location>();
		
		// create markers from features
		for(PointFeature feature : features) {
			AirportMarker m = new AirportMarker(feature);
	
			//m.setRadius(5);
			if(Float.parseFloat(feature.getProperty("altitude").toString())>4000){
				airportList.add(m);
				//System.out.println(feature.getProperty("altitude"));
				// put airport in hashmap with OpenFlights unique id for key
				airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
			}		
			//System.out.println(feature.getProperties());
			
		
		}
		
		
		// parse route data
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
		routeList = new ArrayList<Marker>();
		for(ShapeFeature route : routes) {
			
			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));
			
			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
			}
			
			SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
		
			//System.out.println(sl.getProperties());
			 //System.out.println(route.getLocations());
			//System.out.println(route.getProperties());
			
			//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
			// routeList.add(sl);
		}
		
		
		
		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
		
		// map.addMarkers(routeList);
		
		map.addMarkers(airportList);
		/*for(Marker s: airportList){
			System.out.println(s.getProperties());
		}*/
		
	}
	
	public void draw() {
		background(0);
		map.draw();
		addKey();
	}
	
	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
		
		textSize(12);
		text("6000+ Altitude", 50, 360);
		
		
		// Remember you can use Processing's graphics methods here
		fill(52, 235, 110);
		rect(5, 5, 150, 150, 10);

		fill(0);

		final PFont tenorite = createFont("tenorite.ttf", 15);
		textFont(tenorite);
		text("High Altitude Airports", 9, 25);
		line(15, 35, 145, 35);

		fill(color(255,0,0));
		ellipse(35, 65, 20, 20);

		fill(color(255, 255, 0));
		rect(25, 85, 20, 20);
		
		fill(color(0,0,255));
		triangle(30, 130, 35, 120, 40, 130);

		fill(0, 0, 0);
		textSize(14);
		text("4000+ ft.", 80, 70);

		text("5000+ ft.", 80, 100);

		text("6000+ ft.", 80, 130);
	
	}
	
	public void mouseMoved() {
		// Deselect all marker
		for (Marker marker : map.getMarkers()) {
			//if(marker != null && (Float.parseFloat(marker.getProperty("altitude").toString())>4000))
			marker.setSelected(false);
		}

		// Select hit marker
		// Note: Use getHitMarkers(x, y) if you want to allow multiple selection.
		Marker marker = map.getFirstHitMarker(mouseX, mouseY);
		if (marker != null) {
			marker.setSelected(true);
		}
	}

}