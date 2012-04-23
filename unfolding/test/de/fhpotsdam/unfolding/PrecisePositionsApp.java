package de.fhpotsdam.unfolding;

import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class PrecisePositionsApp extends PApplet {

	Map map;

	public static final Location SINGAPORE_LOCATION = new Location(1.283f, 103.833f);
	Location location1 = new Location(1.283000f, 103.833000f);
	Location location2 = new Location(1.283001f, 103.833001f);
	Location location3 = new Location(1.283018f, 103.833015f);

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new Map(this);
		map.zoomAndPanTo(SINGAPORE_LOCATION, 14);
		map.setZoomRange(14, 22);

		MapUtils.createDefaultEventDispatcher(this, map);

		map.updateMap();

		float x = 103.833000f;
		float y = 103.833002f;
		println(x + ", " + y);
		System.out.printf("x = %.9f y = %.9f \n", x, y);
		// println(PApplet.nf(x, 1, 9) + ", " + PApplet.nf(y, 1, 9));

	}

	public void draw() {
		background(0);
		map.draw();

		float[] pos1 = map.getScreenPositionFromLocation(location1);
		float[] pos2 = map.getScreenPositionFromLocation(location2);
		float[] pos3 = map.getScreenPositionFromLocation(location3);
		// println("1 " + pos1[0] + "," + pos1[1]);
		// println("2 " + pos2[0] + "," + pos2[1]);
		// println("3 " + pos3[0] + "," + pos3[1]);

		Location locA = map.getLocationFromScreenPosition(mouseX, mouseY);
		Location locB = map.getLocationFromScreenPosition(mouseX + 5, mouseY + 5);
		ellipse(mouseX, mouseY, 10, 10);
		ellipse(mouseX + 5, mouseY + 5, 10, 10);
		println("locA " + locA);
		println("locB " + locB);

		fill(255, 0, 0);
		ellipse(pos1[0], pos1[1], 6, 6);
		fill(0, 255, 0);
		ellipse(pos2[0], pos2[1], 6, 6);
		fill(0, 0, 255);
		ellipse(pos3[0], pos3[1], 6, 6);
	}

}
