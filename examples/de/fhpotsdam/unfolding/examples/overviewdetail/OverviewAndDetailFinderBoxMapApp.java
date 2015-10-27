package de.fhpotsdam.unfolding.examples.overviewdetail;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Three maps are shown: One large navigable detail map, and two overview maps. The currently selected area in the
 * detail map is shown as finder rectangle in the overview maps. One overview map always shows the whole world, with its
 * finder rectangle changing size on zoom. The other overview map shows the same view in another zoom level, with its
 * finder rectangle keeping its size.
 * 
 * This Overview + Detail example shows how to setup connected map views.
 */
public class OverviewAndDetailFinderBoxMapApp extends PApplet {

	UnfoldingMap mapDetail;
	UnfoldingMap mapOverview;
	UnfoldingMap mapOverviewStatic;

	public void settings() {
		size(800, 600, P2D);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { OverviewAndDetailFinderBoxMapApp.class.getName() });
	}

	public void setup() {

		// Detail map with default mouse and keyboard interactions
		mapDetail = new UnfoldingMap(this, "detail", 10, 10, 585, 580);
		mapDetail.zoomToLevel(4);
		mapDetail.setZoomRange(4, 10);
		mapDetail.setTweening(true);
		EventDispatcher eventDispatcher = MapUtils.createDefaultEventDispatcher(this, mapDetail);

		// Overview map listens to interaction events from the detail map
		mapOverview = new UnfoldingMap(this, "overview", 605, 10, 185, 185);
		mapOverview.zoomToLevel(1);
		mapOverview.setZoomRange(1, 7);
		mapOverview.setTweening(true);
		eventDispatcher.register(mapOverview, "pan", mapDetail.getId());
		eventDispatcher.register(mapOverview, "zoom", mapDetail.getId());

		// Static overview map
		mapOverviewStatic = new UnfoldingMap(this, "overviewStatic", 605, 205, 185, 185);
	}

	public void draw() {
		background(0);

		mapDetail.draw();
		mapOverview.draw();
		mapOverviewStatic.draw();

		// Finder box for overview map 3 levels zoomed in
		ScreenPosition tl1 = mapOverview.getScreenPosition(mapDetail.getTopLeftBorder());
		ScreenPosition br1 = mapOverview.getScreenPosition(mapDetail.getBottomRightBorder());
		drawDetailSelectionBox(tl1, br1);

		// Finder box for static overview map
		ScreenPosition tl2 = mapOverviewStatic.getScreenPosition(mapDetail.getTopLeftBorder());
		ScreenPosition br2 = mapOverviewStatic.getScreenPosition(mapDetail.getBottomRightBorder());
		drawDetailSelectionBox(tl2, br2);
	}

	public void drawDetailSelectionBox(ScreenPosition tl, ScreenPosition br) {
		noFill();
		stroke(251, 114, 0, 240);
		float w = br.x - tl.x;
		float h = br.y - tl.y;
		rect(tl.x, tl.y, w, h);
	}
}
