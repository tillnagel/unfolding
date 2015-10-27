package de.fhpotsdam.unfolding.examples.overviewdetail;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Shows an navigable detail map, and a static overview map with a viewport. The area in the detail map is shown as
 * finder rectangle (aka viewport) in the overview map. The finder can be dragged around.
 * 
 * This Overview + Detail example shows how to use custom interaction without relying on Unfolding's internal event
 * mechanism.
 */
public class OverviewAndDetailWithViewportApp extends PApplet {

	// Big map showing a detailed area
	UnfoldingMap mapDetail;

	// Small map showing the overview, i.e. the world
	UnfoldingMap mapOverviewStatic;

	// Interactive finder box atop the overview map.
	ViewportRect viewportRect;

	public void settings() {
		size(800, 600, P2D);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { OverviewAndDetailWithViewportApp.class.getName() });
	}

	public void setup() {
		// Detail map with default mouse and keyboard interactions
		mapDetail = new UnfoldingMap(this, "detail", 10, 10, 585, 580);
		// mapDetail.setTweening(true);
		mapDetail.zoomToLevel(4);
		mapDetail.setZoomRange(4, 10);
		MapUtils.createDefaultEventDispatcher(this, mapDetail);

		// Static overview map
		mapOverviewStatic = new UnfoldingMap(this, "overviewStatic", 605, 10, 185, 185);

		viewportRect = new ViewportRect();
	}

	public void draw() {
		background(0);

		mapDetail.draw();
		mapOverviewStatic.draw();

		// Viewport is updated by the actual area of the detail map
		ScreenPosition tl = mapOverviewStatic.getScreenPosition(mapDetail.getTopLeftBorder());
		ScreenPosition br = mapOverviewStatic.getScreenPosition(mapDetail.getBottomRightBorder());
		viewportRect.setDimension(tl, br);
		viewportRect.draw();
	}

	public void panViewportOnDetailMap() {
		float x = viewportRect.x + viewportRect.w / 2;
		float y = viewportRect.y + viewportRect.h / 2;
		Location newLocation = mapOverviewStatic.mapDisplay.getLocation(x, y);
		mapDetail.panTo(newLocation);
	}

	class ViewportRect {
		float x;
		float y;
		float w;
		float h;
		boolean dragged = false;

		public boolean isOver(float checkX, float checkY) {
			return checkX > x && checkY > y && checkX < x + w && checkY < y + h;
		}

		public void setDimension(ScreenPosition tl, ScreenPosition br) {
			this.x = tl.x;
			this.y = tl.y;
			this.w = br.x - tl.x;
			this.h = br.y - tl.y;
		}

		public void draw() {
			noFill();
			stroke(251, 114, 0, 240);
			rect(x, y, w, h);
		}

	}

	float oldX;
	float oldY;

	public void mousePressed() {
		if (viewportRect.isOver(mouseX, mouseY)) {
			viewportRect.dragged = true;
			oldX = mouseX - viewportRect.x;
			oldY = mouseY - viewportRect.y;
		}
	}

	public void mouseReleased() {
		viewportRect.dragged = false;
	}

	public void mouseDragged() {
		if (viewportRect.dragged) {
			viewportRect.x = mouseX - oldX;
			viewportRect.y = mouseY - oldY;

			panViewportOnDetailMap();
		}
	}

}
