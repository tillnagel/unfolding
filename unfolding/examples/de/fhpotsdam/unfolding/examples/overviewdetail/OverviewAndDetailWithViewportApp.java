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
 * This Overview + Detail example shows how to use custom interaction without relying on Unfolding's
 * internal event mechanism.
 */
public class OverviewAndDetailWithViewportApp extends PApplet {
	
	// Big map showing a detailed area
	Map mapDetail;

	// Small map showing the overview, i.e. the world
	Map mapOverviewStatic;

	// Interactive finder box atop the overview map.
	ViewportRect viewportRect;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		// Detail map with default mouse and keyboard interactions
		mapDetail = new Map(this, "detail", 10, 10, 585, 580);
		mapDetail.zoomToLevel(4);
		mapDetail.setZoomRange(4, 10);
		EventDispatcher eventDispatcher = MapUtils.createDefaultEventDispatcher(this, mapDetail);

		// Static overview map
		mapOverviewStatic = new Map(this, "overviewStatic", 605, 10, 185, 185);

		viewportRect = new ViewportRect();
	}

	public void draw() {
		background(0);

		mapDetail.draw();
		mapOverviewStatic.draw();

		// Viewport is updated by the actual area of the detail map
		float[] tl = mapOverviewStatic.mapDisplay.getScreenPositionFromLocation(mapDetail.getTopLeftBorder());
		float[] br = mapOverviewStatic.mapDisplay.getScreenPositionFromLocation(mapDetail.getBottomRightBorder());
		viewportRect.setDimension(tl, br);
		viewportRect.draw();
	}

	public void panDetailMap() {
		float x = viewportRect.x + viewportRect.w / 2;
		float y = viewportRect.y + viewportRect.h / 2;
		Location newLocation = mapOverviewStatic.mapDisplay.getLocationFromScreenPosition(x, y);
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

		public void setDimension(float[] tl, float[] br) {
			this.x = tl[0];
			this.y = tl[1];
			this.w = br[0] - tl[0];
			this.h = br[1] - tl[1];
		}

		public void draw() {
			noFill();
			stroke(30, 30, 255, 140);
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

			panDetailMap();
		}
	}

}
