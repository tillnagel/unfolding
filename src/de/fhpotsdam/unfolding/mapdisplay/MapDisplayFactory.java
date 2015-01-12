package de.fhpotsdam.unfolding.mapdisplay;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;

/**
 * A factory to create MapDisplays for the UnfoldingMap, depending on specified map features.
 * 
 */
@SuppressWarnings("rawtypes")
public class MapDisplayFactory {
	public static final String OPEN_GL_CLASSNAME = "processing.opengl.PGraphicsOpenGL";

	public static final boolean DEFAULT_USE_MASK = true;
	public static final boolean DEFAULT_USE_DISTORTION = false;

	public static final String OSM_API_KEY = "YOUR-OWN-KEY"; // original one invalid since June 2013
	public static final int OSM_STYLE_ID = 65678; // test: 69960; // original: 998

	public static AbstractMapDisplay getMapDisplay(PApplet p, String id, float x, float y, float width, float height,
			AbstractMapProvider provider, UnfoldingMap map, String renderer) {
		return getMapDisplay(p, id, x, y, width, height, DEFAULT_USE_MASK, DEFAULT_USE_DISTORTION, provider, map,
				renderer);
	}

	public static AbstractMapDisplay getMapDisplay(PApplet p, String id, float x, float y, float width, float height,
			boolean useMask, boolean useDistortion, AbstractMapProvider provider, UnfoldingMap map, String renderer) {

		AbstractMapDisplay mapDisplay = null;

		if (provider == null) {
			provider = getDefaultProvider();
		}

		try {
			Class openGLClass = Class.forName(OPEN_GL_CLASSNAME);
			if (openGLClass.isInstance(p.g)) {
				mapDisplay = new OpenGLMapDisplay(p, provider, renderer, x, y, width, height);
				PApplet.println("Using OpenGLMapDisplay with " + ((OpenGLMapDisplay) mapDisplay).getRenderer());
			} else {
				mapDisplay = new Java2DMapDisplay(p, provider, x, y, width, height);
				PApplet.println("No OpenGL renderer. Using Java2DMapDisplay.");
			}
		} catch (ClassNotFoundException e) {
			mapDisplay = new Java2DMapDisplay(p, provider, x, y, width, height);
		}

		mapDisplay.createDefaultMarkerManager(map);
		return mapDisplay;
	}

	public static AbstractMapProvider getDefaultProvider() {
		//return new OpenStreetMap.OSMGrayProvider();
		return new OpenStreetMap.PositronMapProvider();
	}
}
