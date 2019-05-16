package de.fhpotsdam.unfolding.examples.mask;

import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoRSSReader;
import de.fhpotsdam.unfolding.mapdisplay.OpenGLMapDisplay;
import de.fhpotsdam.unfolding.mapdisplay.shaders.BlurredMapDisplayShader;
import de.fhpotsdam.unfolding.mapdisplay.shaders.MapDisplayShader;
import de.fhpotsdam.unfolding.mapdisplay.shaders.MaskedMapDisplayShader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Shows two different MapDisplayShader, one shading map and marker (mask), one only map (blur).
 * 
 * Whether the shader also affects marker depends on the implementation in the Shader class. 
 * 
 * Switch shader by setting the useShaderWithMarker.
 */
public class MaskedMarkerAndMapApp extends PApplet {

	boolean useShaderWithMarker = true;

	String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_week.atom";

	UnfoldingMap map;

	MapDisplayShader shaderWithMarker;
	MapDisplayShader shaderWithoutMarker;

	public void settings() {
		size(800, 800, P2D);
	}

	public void setup() {
		map = new UnfoldingMap(this, 100, 100, 600, 600);
		MapUtils.createDefaultEventDispatcher(this, map);

		List<Feature> features = GeoRSSReader.loadDataGeoRSS(this, earthquakesURL);
		List<Marker> markers = MapUtils.createSimpleMarkers(features);
		for (Marker m : markers) {
			m.setColor(color(255, 0, 0));
		}
		map.addMarkers(markers);

		// Mask shader also shades markers, i.e. markers will be masked, too
		PImage maskImage = loadImage("shader/mask-circular.png");
		shaderWithMarker = new MaskedMapDisplayShader(this, 400, 400, maskImage);
		
		// Blur shader does not shade marker, i.e. markers stay sharp
		shaderWithoutMarker = new BlurredMapDisplayShader(this);

		if (useShaderWithMarker) {
			((OpenGLMapDisplay) map.mapDisplay).setMapDisplayShader(shaderWithMarker);
		} else {
			((OpenGLMapDisplay) map.mapDisplay).setMapDisplayShader(shaderWithoutMarker);
		}
	}

	public void draw() {
		background(20);
		map.draw();
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { MaskedMarkerAndMapApp.class.getName() });
	}

}