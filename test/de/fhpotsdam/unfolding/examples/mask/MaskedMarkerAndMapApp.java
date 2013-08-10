package de.fhpotsdam.unfolding.examples.mask;

import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoRSSReader;
import de.fhpotsdam.unfolding.mapdisplay.OpenGLMapDisplay;
import de.fhpotsdam.unfolding.mapdisplay.shaders.BlurredMapDisplayShader;
import de.fhpotsdam.unfolding.mapdisplay.shaders.MapDisplayShader;
import de.fhpotsdam.unfolding.mapdisplay.shaders.MaskedMapDisplayShader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Shows two different MapDisplayShader, one shading map and marker (mask), one only map (blur).
 * 
 * Switch shader by setting the useShaderWithMarker.
 * 
 */
public class MaskedMarkerAndMapApp extends PApplet {

	boolean useShaderWithMarker = false;

	String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/catalogs/eqs7day-M5.xml";

	UnfoldingMap map;

	MapDisplayShader shader;

	public void setup() {
		size(600, 600, OPENGL);
		map = new UnfoldingMap(this, 100, 100, 400, 400);
		MapUtils.createDefaultEventDispatcher(this, map);

		List<Feature> features = GeoRSSReader.loadData(this, earthquakesURL);
		List<Marker> markers = MapUtils.createSimpleMarkers(features);
		for (Marker m : markers) {
			m.setColor(color(255, 0, 0));
		}
		map.addMarkers(markers);

		if (useShaderWithMarker) {
			// Mask shader also shades markers
			PImage maskImage = loadImage("test/mask-circular.png");
			shader = new MaskedMapDisplayShader(this, 400, 400, maskImage);
		}
		else {
			// Blur shader does not shade marker
			shader = new BlurredMapDisplayShader(this);
		}

		((OpenGLMapDisplay) map.mapDisplay).setMapDisplayShader(shader);
	}

	public void draw() {
		background(0);
		map.draw();
	}

}