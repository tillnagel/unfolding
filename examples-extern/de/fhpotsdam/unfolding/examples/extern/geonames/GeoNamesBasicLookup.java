package de.fhpotsdam.unfolding.examples.extern.geonames;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

import org.geonames.*;

public class GeoNamesBasicLookup extends PApplet {

	UnfoldingMap map;
	int theZoomLevel;

	// GEONAMES Variables
	String searchName = "berlin";
	ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
	boolean searchEvent = true;
	boolean typeName = false;

	public void setup() {

		size(650, 440, GLConstants.GLGRAPHICS);

		// INIT UNFOLDING
		map = new UnfoldingMap(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		MapUtils.createDefaultEventDispatcher(this, map);

		// INIT GEONAMES
		WebService.setUserName("username"); // add your username here
		searchCriteria.setMaxRows(1);

	}

	public void draw() {
		background(0);
		map.updateMap();
		map.draw();
		Location loc = new Location(52.5f, 13.4f);

		// GEONAMES EVENT

		if (searchEvent == true) {
			searchCriteria.setQ(searchName);

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
		// println(map.getZoomLevel());

	}

	public void keyPressed() {

		switch (key) {
		case 'a':
			searchName = "berlin";
			searchEvent = true;
			break;

		case 's':
			searchName = "monaco";
			searchEvent = true;
			break;

		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.geonames.GeoNamesBasicLookup" });
	}
}