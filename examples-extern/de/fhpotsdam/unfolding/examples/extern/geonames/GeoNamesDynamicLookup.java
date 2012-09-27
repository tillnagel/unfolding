package de.fhpotsdam.unfolding.examples.extern.geonames;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

import org.geonames.*;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Textfield;

public class GeoNamesDynamicLookup extends PApplet {

	// UNFOLDING Variables
	UnfoldingMap map;
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

	public void setup() {

		size(650, 440, GLConstants.GLGRAPHICS);

		// INIT UNFOLDING
		map = new UnfoldingMap(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		MapUtils.createDefaultEventDispatcher(this, map);

		// INIT GEONAMES
		WebService.setUserName("username"); // add your username here
		searchCriteria.setMaxRows(1);

		// INIT CONTROLP5
		cp5 = new ControlP5(this);

		println(cp5);
		myTextfield = cp5.addTextfield("Search Criteria").setPosition(20, 400).setSize(200, 20).setFocus(true);

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
				ToponymSearchResult searchResult = WebService.search(searchCriteria);

				for (Toponym toponym : searchResult.getToponyms()) {
					println(toponym.getName() + " " + toponym.getCountryName() + " " + toponym.getLongitude() + " "
							+ toponym.getLatitude() + " " + toponym.getFeatureClass());

					loc.x = (float) toponym.getLatitude();
					loc.y = (float) toponym.getLongitude();
					map.panTo(loc);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			searchEvent = false;

		}
		ScreenPosition pos = map.getScreenPosition(loc);
		fill(0, 150);
		noStroke();
		ellipse(pos.x, pos.y, 20, 20);

	}

	public void input(String theText) {
		// automatically receives results from controller input
		println("a textfield event for controller 'input' : " + theText);
	}

	public void controlEvent(ControlEvent theEvent) {
		if (theEvent.isAssignableFrom(Textfield.class)) {
			println("controlEvent: accessing a string from controller '" + theEvent.getName() + "': "
					+ theEvent.getStringValue());
			searchEvent = true;
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.geonames.GeoNamesDynamicLookup" });
	}
}