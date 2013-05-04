package de.fhpotsdam.unfolding.examples.threed;

import java.util.List;

import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoRSSReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class SphereMarkersMap3DApp extends Map3DApp {

	String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/atom/4.5/week";
	List<Feature> features;
	
	boolean useColorCoding = false;

	public void setup() {
		size(1024, 768, GLConstants.GLGRAPHICS);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(berlinLocation, 3);

		this.init3D();

		// Data loading
		features = GeoRSSReader.loadDataGeoRSS(this, earthquakesURL);
		for (Feature feature : features) {
			println(feature.getStringProperty("title") + " : " + feature.getProperty("magnitude"));
		}
	}

	public void draw() {
		background(0);

		pushMatrix();
		rotateX(0.7f);
		translate(0, -160, -100);
		map.draw();

		mousePos = getMouse3D();

		
		// TODO Create and use GLModel to improve fps
		
		fill(255, 0, 0, 100);
		noStroke();
		float maxSize = map(map.getZoomLevel(), 0, 6, 4, 40);
		for (Feature feature : features) {
			PointFeature pf = (PointFeature) feature;
			ScreenPosition pos = map.getScreenPosition(pf.getLocation());
			pushMatrix();
			translate(pos.x, pos.y);
			float magnitude = (Float) feature.getProperty("magnitude");
			if (useColorCoding) {
				int color = mapToColor(magnitude);
				fill(color, 100);
			}
			float s = map(magnitude, 4, 8, 4, maxSize);
			sphere(s);
			popMatrix();
		}

		popMatrix();

		fill(255);
		rect(5, 5, 180, 20);
		fill(0);
		text("fps: " + nfs(frameRate, 0, 2), 10, 20);
	}

	private int mapToColor(float magnitude) {
		if (magnitude < 5) {
			return color(255, 255, 0);
		}
		if (magnitude < 6) {
			return color(255, 0, 255);
		}
		if (magnitude < 8) {
			return color(0, 255, 255);
		}
		return color(0);
	}

}
