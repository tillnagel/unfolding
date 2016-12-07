package de.fhpotsdam.unfolding.examples.marker.multimarker;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Manual multi-marker example showing two polygons for France and Corsica to demonstrate a multi marker. By hovering
 * over one of the two areas the whole MultiMarker gets selected, thus both areas are highlighted.
 * 
 * <p>
 * <em>Note, that loading Unfolding's DataReader automatically creates and handles multi-marker.</em>
 * </p>
 * 
 * Set the boolean useMultiMarker to false to see the same areas as independent markers.
 */
public class MultiMarkerApp extends PApplet {

	boolean useMultiMarker = true;

	UnfoldingMap map;

	public void settings() {
		size(800, 600, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this, "map");
		map.zoomToLevel(4);
		map.panTo(new Location(50f, 12f));
		MapUtils.createDefaultEventDispatcher(this, map);

		SimplePolygonMarker franceMarker = new SimplePolygonMarker(getFranceShapeLocations());
		SimplePolygonMarker corsicaMarker = new SimplePolygonMarker(getCorsicaShapeLocations());

		if (useMultiMarker) {
			// Create and add as MultiMarker
			MultiMarker multiMarker = new MultiMarker();
			multiMarker.addMarkers(franceMarker, corsicaMarker);
			map.addMarkers(multiMarker);

		} else {
			// Add France and Corsica as two independent polygon markers
			map.addMarkers(franceMarker, corsicaMarker);
		}
	}

	public void draw() {
		background(240);
		map.draw();
	}

	public void mouseMoved() {
		// Not via marker.isInside(...) as this example supports both MultiMarker and two markers.
		// multiMarker.isInside(map, mouseX, mouseY);

		Marker hitMarker = map.getDefaultMarkerManager().getFirstHitMarker(mouseX, mouseY);
		if (hitMarker != null) {
			hitMarker.setSelected(true);
		} else {
			for (Marker marker : map.getDefaultMarkerManager().getMarkers()) {
				marker.setSelected(false);
			}
		}
	}

	public static List<Location> getFranceShapeLocations() {
		// Crude shape of France
		List<Location> franceLocations = new ArrayList<Location>();
		franceLocations.add(new Location(48.985985f, 8.173828f));
		franceLocations.add(new Location(51.074539f, 2.460938f));
		franceLocations.add(new Location(49.33085f, -0.043945f));
		franceLocations.add(new Location(48.522426f, -4.746094f));
		franceLocations.add(new Location(46.231533f, -1.054687f));
		franceLocations.add(new Location(43.427392f, -1.801758f));
		franceLocations.add(new Location(42.397499f, 3.208008f));
		franceLocations.add(new Location(43.682174f, 3.911133f));
		franceLocations.add(new Location(43.075308f, 6.28418f));
		franceLocations.add(new Location(43.935879f, 7.734375f));
		franceLocations.add(new Location(46.534681f, 6.064453f));
		return franceLocations;
	}

	public static List<Location> getCorsicaShapeLocations() {
		// Crude shape of Corsica
		List<Location> corsicaLocations = new ArrayList<Location>();
		corsicaLocations.add(new Location(41.380106f, 9.162598f));
		corsicaLocations.add(new Location(42.231771f, 8.547363f));
		corsicaLocations.add(new Location(42.991791f, 9.404297f));
		corsicaLocations.add(new Location(42.052556f, 9.558105f));
		return corsicaLocations;
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { MultiMarkerApp.class.getName() });
	}

}
