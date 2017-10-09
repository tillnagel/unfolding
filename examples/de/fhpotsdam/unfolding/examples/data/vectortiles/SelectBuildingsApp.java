package de.fhpotsdam.unfolding.examples.data.vectortiles;

import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.examples.interaction.snapshot.CircularMapSnapshot;
import de.fhpotsdam.unfolding.examples.interaction.snapshot.MapSnapshot;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Experiment to select building, then zoom+pan to fit and show in thumbnail.
 */
public class SelectBuildingsApp extends PApplet {

	UnfoldingMap map;

	VectorTilesUtils vectorTilesUtils;
	String featureLayer = "buildings";

	Location[] boundingBox;
	MapSnapshot mapSnapshot = null;

	public void settings() {
		size(800, 600, P2D);
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { SelectBuildingsApp.class.getName() });
	}

	public void setup() {
		map = new UnfoldingMap(this, 0, 0, 600, 600);

		map.zoomAndPanTo(16, new Location(52.501, 13.395));
		MapUtils.createDefaultEventDispatcher(this, map);
		map.setZoomRange(10, 19);

		vectorTilesUtils = new VectorTilesUtils(this, map, VectorTilesApp.MAPZEN_API_KEY);
		List<Marker> markers = vectorTilesUtils.loadMarkersForScreenPos(featureLayer, width / 2, height / 2);
		map.addMarkers(markers);
	}

	public void draw() {
		background(0);
		map.draw();

		if (mapSnapshot != null)
			mapSnapshot.draw(620, 20, 200, 200);

		if (boundingBox != null) {
			ScreenPosition nwPos = map.getScreenPosition(boundingBox[0]);
			ScreenPosition sePos = map.getScreenPosition(boundingBox[1]);
			stroke(0, 255, 0, 200);
			noFill();
			rect(nwPos.x, nwPos.y, sePos.x - nwPos.x, sePos.y - nwPos.y);
		}
	}

	public void mouseClicked() {
		List<Marker> markers = map.getMarkers();
		for (Marker marker : markers)
			marker.setHidden(true);

		Marker hitMarker = map.getFirstHitMarker(mouseX, mouseY);
		if (hitMarker != null) {

			map.zoomAndPanToFit(GeoUtils.getLocations(hitMarker));
			map.draw();

			boundingBox = GeoUtils.getBoundingBox(GeoUtils.getLocations(hitMarker));

			mapSnapshot = new CircularMapSnapshot(this, map, 300, 5);
			mapSnapshot.snapshot(map, 0, 0, 600, 600);

			mapSnapshot.draw(0, 0, 600, 600);

			hitMarker.setHidden(false);
			String buildingName = hitMarker.getStringProperty("name");
			println(buildingName != null ? buildingName : "n/a");

		}
	}

	public void keyPressed() {
		if (key == 't') {
			List<Marker> markers = map.getMarkers();
			for (Marker marker : markers)
				marker.setHidden(false);
		}

		if (key == 's') {
			mapSnapshot = new CircularMapSnapshot(this);
			mapSnapshot.snapshot(map);
		}
	}

}
