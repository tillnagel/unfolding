/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhpotsdam.unfolding.examples.provider;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.CartoDBProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * 
 * TODO @marcus: Currently used carto db does not work anymore.
 */
public class CartoDBLayerMapApp extends PApplet {

	UnfoldingMap map;
	AbstractMapProvider masterProvider;
	CartoDBProvider cartodb;

	@Override
	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		// the masterProvider is used to fetch a base layer for the map
		masterProvider = new Microsoft.AerialProvider();

		// CartoDB provides transparent PNGs as data layers
		// cartodb = new CartoDBProvider("gfzba", "hymet_stations");
		cartodb = new CartoDBProvider("gfzba", "rivers_ferghana");
		// This setting will blend the CartoDB Tiles on the base map
		cartodb.masterProvider = masterProvider;

		// We can customize the elements to be displayed by changing the SQL request
		// cartodb.setSql("SELECT * FROM hymet_stations");

		// We can customize the appearance of the cartodb map tiles by sending CartoCSS styles
		// String style =
		// "#hymet_stations { marker-fill:#FF3300; marker-width:20; marker-line-color:#ffffff; marker-line-width:1; marker-opacity:.5; marker-line-opacity:1; marker-placement:point; marker-type:ellipse; marker-allow-overlap:true; }";
		// cartodb.setStyle(style);

		// map1 = new Map(this, "map1", 0, 0, width, height, true, false, );
		// map1.setTweening(false);
		map = new UnfoldingMap(this, "map", 0, 0, width, height, true, false, cartodb);
		map.setTweening(false);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	@Override
	public void draw() {
		background(0);
		tint(255, 255);
		// map1.draw();
		// tint(255,100);
		map.draw();
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.multi.CartoDBLayerMapApp" });
	}
}
