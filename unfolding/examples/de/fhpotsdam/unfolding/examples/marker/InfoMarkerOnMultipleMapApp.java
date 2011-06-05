package de.fhpotsdam.unfolding.examples.marker;

import java.util.List;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import processing.core.PFont;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Multiple markers on multiple maps.
 * 
 * Check various interactions to see, how the labels behave. The left map is rotated, but the labels
 * are not. The right map is innerRotated, thus the labels are too.
 */
public class InfoMarkerOnMultipleMapApp extends PApplet {

	public static Logger log = Logger.getLogger(InfoMarkerOnMultipleMapApp.class);

	Map map1;
	Map map2;
	EventDispatcher eventDispatcher;

	MarkerManager markerManager1, markerManager2;

	PFont font;

	public void setup() {
		size(1300, 600, GLConstants.GLGRAPHICS);
		smooth();

		font = loadFont("Miso-Light-12.vlw");
		textFont(font);

		map1 = new Map(this, "map", 10, 10, 780, 580, true, false,
				new OpenStreetMap.CloudmadeProvider(MapDisplayFactory.OSM_API_KEY, 23058));
		map1.setTweening(false);
		map1.rotate(0.3f);

		map2 = new Map(this, "map", 800, 10, 490, 580, true, false,
				new OpenStreetMap.CloudmadeProvider(MapDisplayFactory.OSM_API_KEY, 23058));
		map2.setTweening(false);
		map2.outerRotate(-0.2f);
		eventDispatcher = MapUtils.createDefaultEventDispatcher(this, map1, map2);

		// Same markers but new instances to allow independent interaction on both maps
		List<Marker> markers1 = InfoMarkerApp.loadGeoRSSMarkers(this, "bbc-georss-test.xml", font);
		List<Marker> markers2 = InfoMarkerApp.loadGeoRSSMarkers(this, "bbc-georss-test.xml", font);

		markerManager1 = new MarkerManager(map1, markers1);
		markerManager2 = new MarkerManager(map2, markers2);

		map1.mapDisplay.setMarkerManager(markerManager1);
		map2.mapDisplay.setMarkerManager(markerManager2);
	}

	public void draw() {
		background(0);

		map1.draw();
		map2.draw();
	}

	public void mouseMoved() {
		checkInsideMarker(map1);
		checkInsideMarker(map2);
	}

	public void checkInsideMarker(Map map) {
		if (map.isHit(mouseX, mouseY)) {
			MarkerManager mm = map.mapDisplay.getMarkerManager();

			// Deselect all marker
			for (LabeledMarker lm : (List<LabeledMarker>) mm.getMarkers()) {
				lm.setSelected(false);
			}

			// Select hit marker
			LabeledMarker marker = (LabeledMarker) mm.getFirstHitMarker(mouseX, mouseY);
			if (marker != null) {
				marker.setSelected(true);
			}
		}
	}

}
