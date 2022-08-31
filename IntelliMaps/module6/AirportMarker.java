package module6;

import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import processing.core.PConstants;
import processing.core.PGraphics;

/** 
 * A class to represent AirportMarkers on a world map.
 *   
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMarker extends CommonMarker {
	public static List<SimpleLinesMarker> routes;
	
	public AirportMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
	    //System.out.println(city.getProperties());
		//java.util.HashMap<String, Object> properties = city.getProperties();
		//System.out.println(this.getProperties());
		//System.out.println(routes);
	}
	
	public void draw(PGraphics pg, float x, float y) {
		// For starter code just drawMaker(...)
		if (!hidden) {
			drawMarker(pg, x, y);
			if (selected) {
				showTitle(pg, x, y);
				
			}
		}
	}
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		
		float magnitude = this.getAltitude();
		if(magnitude>4000 && magnitude<=5000){
			pg.fill(255,0,0);
			pg.ellipse(x, y, 5, 5);
		}else if(magnitude>5000 && magnitude<=6000){
			pg.fill(255,255,0);
			pg.rect(x, y, 5, 5);
		}else if(magnitude>6000){
			pg.fill(0,0,255);
			pg.triangle(x, y-5, x-5, y+5, x+5, y+5);
		}
	}

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		String title = this.getCountry()+ "-" +this.getName()+":"+this.getAltitude() + "ft.";
		pg.pushStyle();
		
		pg.rectMode(PConstants.CORNER);
		
		pg.stroke(110);
		pg.fill(255,255,255);
		pg.rect(x, y + 15, pg.textWidth(title) +6, 18, 5);
		
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.fill(0);
		pg.text(title, x + 3 , y +18);
		
		
		pg.popStyle();
		
	}
	
	public static List<SimpleLinesMarker> getRoutes() {
		
		return routes;
	}

	public static void setRoutes(List<SimpleLinesMarker> routes) {
		AirportMarker.routes = routes;
	}

	/*
	 * getters for Airport properties
	 */
	
	public float getAltitude() {
		return Float.parseFloat(getProperty("altitude").toString());
	}
	
	public String getCountry() {
		return (String) getProperty("country");
	}
	
	public String getCity() {
		return (String) getProperty("city");
	}
	
	public String getName() {
		return (String) getProperty("name");
	}
	
	public String getCode() {
		return (String) getProperty("code");	
		
	}
	
	public float getRadius() {
		return Float.parseFloat(getProperty("radius").toString());
	}
	
}