package de.fhpotsdam.unfolding.examples.overviewdetail;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * 
 * 
 * @author tillnagel
 */
public class ConvexHullConnectedMapOnStaticMap extends PApplet {

	// Small detail map
	Map mapDetail;
	// Big overview map
	Map mapOverview;

	float mapZoomX = 100;
	float mapZoomY = 100;

	OverviewPlusDetailConnection connection;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		mapOverview = new Map(this, "overview", 10, 10, 585, 580);
		mapOverview.zoomToLevel(1);
		mapOverview.setZoomRange(1, 7);
		EventDispatcher eventDispatcher = MapUtils
				.createDefaultEventDispatcher(this, mapOverview);

		mapDetail = new Map(this, "detail", 605, 10, 150, 150);
		mapDetail.setTweening(false);
		mapDetail.zoomToLevel(4);
		mapDetail.setZoomRange(4, 10);

		connection = new ConvexHullConnection(this);
		connection.setDetailSize(150, 150);
		connection.setDetailPosition(605, 10);
		connection.setPadding(0);
		((ConvexHullConnection) connection).hullFillColor = color(100, 100);
		((ConvexHullConnection) connection).hullStrokeColor = color(100, 0);
	}

	public void draw() {
		background(0);

		mapOverview.draw();

		updateConnection();
		connection.draw();

		mapDetail.draw();
	}

	public void updateConnection() {
		// Finder box for overview map
		ScreenPosition tl = mapOverview.mapDisplay.getScreenPosition(mapDetail
				.getTopLeftBorder());
		ScreenPosition br = mapOverview.mapDisplay.getScreenPosition(mapDetail
				.getBottomRightBorder());
		float w = br.x - tl.x;
		float h = br.y - tl.y;
		connection.setOverviewSize(w, h);
		connection.setOverviewPosition(tl.x, tl.y);
	}

}
