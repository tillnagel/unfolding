package de.fhpotsdam.unfolding.examples.marker.infomarker;

import java.util.List;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import processing.core.PFont;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;

//TO BE DELETED! See labelmarker.* examples for how to do this now.
public class InfoMarkerOnMultipleMapApp extends PApplet {

	public static Logger log = Logger.getLogger(InfoMarkerOnMultipleMapApp.class);

	UnfoldingMap map1;
	UnfoldingMap map2;
	EventDispatcher eventDispatcher;

	MarkerManager markerManager1, markerManager2;

	PFont font;

	public void setup() {
		size(1300, 600, GLConstants.GLGRAPHICS);
		smooth();

		font = loadFont("Miso-Light-12.vlw");
		textFont(font);

		map1 = new UnfoldingMap(this, "map", 10, 10, 780, 580, true, false,
				new OpenStreetMap.CloudmadeProvider(MapDisplayFactory.OSM_API_KEY, 23058));
		map1.setTweening(false);
		map1.rotate(0.3f);

		map2 = new UnfoldingMap(this, "map", 800, 10, 490, 580, true, false,
				new OpenStreetMap.CloudmadeProvider(MapDisplayFactory.OSM_API_KEY, 23058));
		map2.setTweening(false);
		map2.outerRotate(-0.2f);
		eventDispatcher = MapUtils.createDefaultEventDispatcher(this, map1, map2);

		// Same markers but new instances to allow independent interaction on both maps
		List<Marker> markers1 = GeoRSSLoader.loadGeoRSSMarkers(this, "bbc-georss-test.xml", font);
		List<Marker> markers2 = GeoRSSLoader.loadGeoRSSMarkers(this, "bbc-georss-test.xml", font);

		markerManager1 = new MarkerManager(markers1);
		markerManager2 = new MarkerManager(markers2);

		map1.addMarkerManager(markerManager1);
		map2.addMarkerManager(markerManager2);
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

	public void checkInsideMarker(UnfoldingMap map) {
		if (map.isHit(mouseX, mouseY)) {
			MarkerManager mm = map.mapDisplay.getLastMarkerManager();

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
