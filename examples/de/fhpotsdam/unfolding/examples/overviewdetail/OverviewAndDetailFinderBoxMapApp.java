package de.fhpotsdam.unfolding.examples.overviewdetail;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Three maps are shown: Two overview maps, and one navigable detail map. The currently selected
 * area in the detail map is shown as finder rectangle in the overview maps.
 * 
 * This Overview + Detail example shows how to setup connected map views.
 */
public class OverviewAndDetailFinderBoxMapApp extends PApplet {

	Map mapDetail;
	Map mapOverview;
	Map mapOverviewStatic;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		// Detail map with default mouse and keyboard interactions
		mapDetail = new Map(this, "detail", 10, 10, 585, 580);
		mapDetail.zoomToLevel(4);
		mapDetail.setZoomRange(4, 10);
		mapDetail.setTweening(true);
		EventDispatcher eventDispatcher = MapUtils.createDefaultEventDispatcher(this, mapDetail);

		// Overview map listens to interaction events from the detail map
		mapOverview = new Map(this, "overview", 605, 10, 185, 185);
		mapOverview.zoomToLevel(1);
		mapOverview.setZoomRange(1, 7);
		mapOverview.setTweening(true);
		eventDispatcher.register(mapOverview, "pan", mapDetail.getId());
		eventDispatcher.register(mapOverview, "zoom", mapDetail.getId());

		// Static overview map
		mapOverviewStatic = new Map(this, "overviewStatic", 605, 205, 185, 185);
	}

	public void draw() {
		background(0);

		mapDetail.draw();
		mapOverview.draw();
		mapOverviewStatic.draw();

		// Finder box for overview map
		float[] tl1 = mapOverview.mapDisplay.getScreenPositionFromLocation(mapDetail.getTopLeftBorder());
		float[] br1 = mapOverview.mapDisplay.getScreenPositionFromLocation(mapDetail.getBottomRightBorder());
		drawDetailSelectionBox(tl1, br1);

		// Finder box for static overview map
		float[] tl2 = mapOverviewStatic.mapDisplay.getScreenPositionFromLocation(mapDetail.getTopLeftBorder());
		float[] br2 = mapOverviewStatic.mapDisplay.getScreenPositionFromLocation(mapDetail.getBottomRightBorder());
		drawDetailSelectionBox(tl2, br2);
	}

	public void drawDetailSelectionBox(float[] tl, float[] br) {
		noFill();
		stroke(30, 30, 255, 140);
		float w = br[0] - tl[0];
		float h = br[1] - tl[1];
		rect(tl[0], tl[1], w, h);
	}

}
