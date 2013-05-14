package de.fhpotsdam;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;

public class AbstractShapeMarkerLocationTest {

	private UnfoldingMap map;
	private MarkerManager<Marker> markerManager = new MarkerManager<Marker>();
	private SimplePolygonMarker marker;
	private float squareWidth = 10.f;

	@Before
	public void before() {
		map = new UnfoldingMap(new PApplet());
		map.addMarkerManager(markerManager);
		addSquareMarker();
	}

	private void addSquareMarker() {
		ArrayList<Location> list = new ArrayList<Location>();
		list.add(new Location(0, 0));
		list.add(new Location(0, squareWidth));
		list.add(new Location(squareWidth, squareWidth));
		list.add(new Location(squareWidth, 0));
		marker = new SimplePolygonMarker(list);
		markerManager.addMarker(marker);
	}

	@Test
	public void test() {
		assertTrue(marker.isInsideByLocation(squareWidth / 2.0f,
				squareWidth / 2.0f));
		assertFalse(marker.isInsideByLocation(-1, -1));
		assertFalse(marker.isInsideByLocation(squareWidth + 1, squareWidth + 1));
	}

}
