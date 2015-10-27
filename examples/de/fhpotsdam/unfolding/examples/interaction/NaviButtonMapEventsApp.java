package de.fhpotsdam.unfolding.examples.interaction;

import processing.core.PApplet;
import processing.core.PFont;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.events.PanMapEvent;
import de.fhpotsdam.unfolding.events.ZoomMapEvent;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Advanced: Manual navigation example with two maps.
 * 
 * Uses own map events to execute the navigation. Opposite to {@link NaviButtonMapApp} in this two-maps example, a
 * simple mapDetail.zoomAndPanTo() would not update mapOverview.
 */
public class NaviButtonMapEventsApp extends PApplet {

	Location berlinLocation = new Location(52.51861f, 13.408056f);
	int berlinZoomLevel = 10;
	Location universityLocation = new Location(52.411613f, 13.051779f);
	int universityZoomLevel = 14;

	UnfoldingMap mapDetail;
	UnfoldingMap mapOverview;
	PFont font;

	EventDispatcher eventDispatcher;

	public void settings() {
		size(800, 600, P2D);
		smooth();
	}

	public void setup() {
		font = createFont("sans-serif", 14);

		mapDetail = new UnfoldingMap(this, "detail", 10, 10, 585, 580);
		mapDetail.setTweening(true);
		mapDetail.zoomToLevel(4);
		mapOverview = new UnfoldingMap(this, "overview", 605, 10, 185, 185);

		eventDispatcher = MapUtils.createDefaultEventDispatcher(this, mapDetail);
		eventDispatcher.register(mapOverview, "pan", mapDetail.getId(), mapOverview.getId());
		eventDispatcher.register(mapOverview, "zoom", mapDetail.getId(), mapOverview.getId());
	}

	public void draw() {
		background(0);
		mapDetail.draw();
		mapOverview.draw();

		drawButtons();
	}

	public void mouseReleased() {
		if (mouseX > 610 && mouseX < 790 && mouseY > 210 && mouseY < 290) {
			// mapDetail.zoomAndPanTo() would not update mapOverview

			PanMapEvent panMapEvent = new PanMapEvent(this, mapDetail.getId());
			panMapEvent.setToLocation(berlinLocation);
			eventDispatcher.fireMapEvent(panMapEvent);

			ZoomMapEvent zoomMapEvent = new ZoomMapEvent(this, mapDetail.getId(), ZoomMapEvent.ZOOM_TO_LEVEL);
			zoomMapEvent.setZoomLevel(berlinZoomLevel);
			zoomMapEvent.setTransformationCenterLocation(berlinLocation);
			eventDispatcher.fireMapEvent(zoomMapEvent);

			// TODO Create convenience methods to fire map events.
			// MapUtils.fireZoomEvent(eventDispatcher, mapDetail, ZoomMapEvent.ZOOM_TO_LEVEL, berlinZoomLevel);
			// MapUtils.firePanEvent(eventDispatcher, mapDetail, berlinLocation);

		} else if (mouseX > 610 && mouseX < 790 && mouseY > 310 && mouseY < 390) {
			PanMapEvent panMapEvent = new PanMapEvent(this, mapDetail.getId());
			panMapEvent.setToLocation(universityLocation);
			eventDispatcher.fireMapEvent(panMapEvent);

			ZoomMapEvent zoomMapEvent = new ZoomMapEvent(this, mapDetail.getId(), ZoomMapEvent.ZOOM_TO_LEVEL);
			zoomMapEvent.setZoomLevel(universityZoomLevel);
			zoomMapEvent.setTransformationCenterLocation(universityLocation);
			eventDispatcher.fireMapEvent(zoomMapEvent);
		}
	}

	public void drawButtons() {
		textFont(font);
		textSize(14);

		// Simple Berlin button
		fill(127);
		stroke(200);
		strokeWeight(2);
		rect(610, 210, 180, 80);
		fill(0);
		text("Berlin (zoom 10)", 620, 252);

		// FHP button
		fill(127);
		rect(610, 310, 180, 80);
		fill(0);
		text("University (zoom 14)", 620, 352);
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { NaviButtonMapEventsApp.class.getName() });
	}

}
