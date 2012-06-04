package de.fhpotsdam.unfolding.examples;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

import org.geonames.*;

import controlP5.*;

public class GeoNames extends PApplet {

	Map map;
	int theZoomLevel;
	
	// GEONAMES Variables
	String searchName = "berlin";
	ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
	boolean searchEvent = true;
	boolean typeName = false;

	// CONTROLP5 Variables
	ControlP5 cp5;
	String textValue = "";
	Textfield myTextfield;
	controlP5.Label label;

	@SuppressWarnings("deprecation")
	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		// INIT UNFOLDING
		map = new Map(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		MapUtils.createDefaultEventDispatcher(this, map);

		// INIT GEONAMES
		WebService.setUserName("maerzhase"); // add your username here
		searchCriteria.setMaxRows(1);

		// INIT CONTROLP5
		cp5 = new ControlP5(this);

		println(cp5);
		myTextfield = cp5.addTextfield("textinput").setPosition(20, 550)
				.setSize(200, 20).setFocus(true);

		// use setAutoClear(true/false) to clear a textfield or keep text
		// displayed in
		// a textfield after pressing return.
		myTextfield.setAutoClear(true).keepFocus(true);
		label = myTextfield.captionLabel();
		label.setColor(color(0));

	}

	public void draw() {
		background(0);
		map.updateMap();
		map.draw();
		Location loc = new Location(52.5f, 13.4f);

		// GEONAMES EVENT
		if (searchEvent == true) {
			searchCriteria.setQ(myTextfield.getStringValue());

			try {
				ToponymSearchResult searchResult = WebService
						.search(searchCriteria);

				for (Toponym toponym : searchResult.getToponyms()) {
					println(toponym.getName() + " " + toponym.getCountryName()
							+ " " + toponym.getLongitude() + " "
							+ toponym.getLatitude() + " "
							+ toponym.getFeatureClass());

					loc.x = (float) toponym.getLatitude();
					loc.y = (float) toponym.getLongitude();
					map.zoomTo(zoomLevelChecker(toponym.getFeatureClass()));
					map.panTo(loc);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			searchEvent = false;

		}
		float xy[] = map.getScreenPositionFromLocation(loc);
		ellipse(xy[0], xy[1], 20, 20);
		//println(map.getZoomLevel());

	}

	public void input(String theText) {
		// automatically receives results from controller input
		println("a textfield event for controller 'input' : " + theText);
	}

	public void controlEvent(ControlEvent theEvent) {
		if (theEvent.isAssignableFrom(Textfield.class)) {
			println("controlEvent: accessing a string from controller '"
					+ theEvent.getName() + "': " + theEvent.getStringValue());
			searchEvent = true;
		}
	}

	/*
	 * public void keyPressed() { switch (key) { case 'a': searchName =
	 * "berlin"; searchEvent = true; break;
	 * 
	 * case 's': searchName = "monaco"; searchEvent = true; break;
	 * 
	 * } }
	 */
	
	public int zoomLevelChecker(FeatureClass featureClass) {
		switch (featureClass) {
		// country, state, region...
		case A:
			theZoomLevel = 5;
			break;
			
		// parks, area...
		case L:
			theZoomLevel = 3;
			break;

		// stream, lake
		case H:
			theZoomLevel = 12;
			break;

		// city, village...
		case P:
			theZoomLevel = 10;
			break;

		// road, railroad...
		case R:
			theZoomLevel = 15;
			break;
			
		// spot, building, farm...
		case S:
			theZoomLevel = 15;
			break;
			
		// mountain, hill, rock...
		case T:
			theZoomLevel = 10;
			break;
			
		// forest, heath...
		case V:
			theZoomLevel = 10;
			break;
			
		// undersea...
		case U:
			theZoomLevel = 10;
			break;

		}
		return theZoomLevel;
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.GeoNames" });
	}
}