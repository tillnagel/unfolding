package de.fhpotsdam.unfolding.examples.overviewdetail;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.interactions.MouseHandler;

/**
 * This Overview + Detail example shows how to setup connected map views.
 * 
 * FIXME: If zooming to far out, the mapOverview crashes, sort of.
 * 
 */
public class OverviewAndDetail2MapApp extends PApplet {

	Map mapDetail;
	Map mapOverview;
	Map mapStaticOverview;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		mapDetail = new Map(this, "detail", 10, 10, 585, 580);
		mapOverview = new Map(this, "overview", 605, 10, 185, 185);
		mapOverview.setActive(false);

		mapStaticOverview = new Map(this, "overview", 605, 205, 185, 185);

		EventDispatcher eventDispatcher = new EventDispatcher();

		// TODO Add keyboard handler, but listens to too many
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
		mapStaticOverview.draw();

		// Selection box for overview map
		float[] overviewSB_TL = mapOverview.mapDisplay.getScreenPositionFromLocation(mapDetail
				.getTopLeftBorder());
		float[] overviewSB_BR = mapOverview.mapDisplay.getScreenPositionFromLocation(mapDetail
				.getBottomRightBorder());
		drawDetailSelectionBox(overviewSB_TL, overviewSB_BR);

		// Selection box for static overview map
		float[] staticSB_TL = mapStaticOverview.mapDisplay.getScreenPositionFromLocation(mapDetail
				.getTopLeftBorder());
		float[] staticSB_BR = mapStaticOverview.mapDisplay.getScreenPositionFromLocation(mapDetail
				.getBottomRightBorder());
		drawDetailSelectionBox(staticSB_TL, staticSB_BR);
	}

	public void drawDetailSelectionBox(float[] screenTL, float[] screenBR) {
		noFill();
		stroke(30, 30, 255, 140);
		float w = screenBR[0] - screenTL[0];
		float h = screenBR[1] - screenTL[1];
		rect(screenTL[0], screenTL[1], w, h);
	}

}
