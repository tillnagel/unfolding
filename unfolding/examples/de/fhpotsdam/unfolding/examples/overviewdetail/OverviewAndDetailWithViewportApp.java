package de.fhpotsdam.unfolding.examples.overviewdetail;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Two maps are shown: One navigable overview maps, and one navigable detail map. The area in the
 * detail map is shown as viewport (finder rectangle) in the overview maps.
 * 
 * This Overview + Detail example shows how to setup connected map views.
 */
public class OverviewAndDetailWithViewportApp extends PApplet {

	Map mapDetail;
	Map mapOverviewStatic;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		// Detail map with default mouse and keyboard interactions
		mapDetail = new Map(this, "detail", 10, 10, 585, 580);
		mapDetail.zoomToLevel(4);
		mapDetail.setZoomRange(4, 10);
		EventDispatcher eventDispatcher = MapUtils.createDefaultEventDispatcher(this, mapDetail);

		// Static overview map
		mapOverviewStatic = new Map(this, "overviewStatic", 605, 10, 185, 185);
	}

	public void draw() {
		background(0);

		mapDetail.draw();
		mapOverviewStatic.draw();

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

	public void mouseClicked() {
		Location newLocation = mapOverviewStatic.mapDisplay.getLocationFromScreenPosition(mouseX, mouseY);
		mapDetail.panTo(newLocation);
	}
	
	
	

}
