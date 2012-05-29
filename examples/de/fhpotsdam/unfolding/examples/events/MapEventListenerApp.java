package de.fhpotsdam.unfolding.examples.events;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.events.MapEvent;
import de.fhpotsdam.unfolding.events.MapEventListener;
import de.fhpotsdam.unfolding.events.ZoomMapEvent;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * This PApplet can react to map interactions. By implementing MapEventListener and registering this PApplet, all
 * interactions with the map can be handled.
 */
public class MapEventListenerApp extends PApplet implements MapEventListener {

	Map map;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		map = new Map(this, "myMap");

		EventDispatcher eventDispatcher = MapUtils.createDefaultEventDispatcher(this, map);
		// Register this PApplet to listen to map events
		eventDispatcher.register(this, "pan", map.getId());
		eventDispatcher.register(this, "zoom", map.getId());
	}

	public void draw() {
		map.draw();
	}

	@Override
	public void onManipulation(MapEvent event) {
		println("Map Interaction: " + event.getType() + " with subtype " + event.getSubType() + " in scope "
				+ event.getScopeId());

		if (event.getType().equals(ZoomMapEvent.TYPE_ZOOM)) {
			ZoomMapEvent zoomMapEvent = (ZoomMapEvent) event;
			Location location = zoomMapEvent.getCenter();
			float[] xy = map.getScreenPositionFromLocation(location);
			fill(255, 0, 0);
			// FIXME GLGraphics offscreen buffer does not allow any drawing outside of draw()
			ellipse(xy[0], xy[1], 10, 10);
		}
	}

	@Override
	public String getId() {
		return "app";
	}
}
