package de.fhpotsdam.unfolding.examples.overviewdetail;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.interactions.MouseHandler;

/**
 * This Overview + Detail example shows how to setup simple connected map views.
 * 
 */
public class OverviewAndDetailMapApp extends PApplet {

	Map mapDetail;
	Map mapOverview;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		mapDetail = new Map(this, "detail", 10, 10, 585, 580);
		mapOverview = new Map(this, "overview", 605, 10, 185, 185);
		
		EventDispatcher eventDispatcher = new EventDispatcher();

		MouseHandler mouseHandler = new MouseHandler(this, mapDetail, mapOverview);
		eventDispatcher.addBroadcaster(mouseHandler);
		
		// Both maps listen to each other
		eventDispatcher.register(mapDetail, "pan", mapDetail.getId(), mapOverview.getId());
		eventDispatcher.register(mapDetail, "zoom", mapDetail.getId(), mapOverview.getId());
		eventDispatcher.register(mapOverview, "pan", mapDetail.getId(), mapOverview.getId());
		eventDispatcher.register(mapOverview, "zoom", mapDetail.getId(), mapOverview.getId());
	}

	public void draw() {
		background(0);

		mapDetail.draw();
		mapOverview.draw();
	}

}
