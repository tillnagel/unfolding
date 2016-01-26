package de.fhpotsdam;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class AbstractShapeMarkerLocationTest {

	private UnfoldingMap map;
	private SimplePolygonMarker squareMarker;
	private float squareWidth = 10.f;
	private PApplet p;

	@Before
	public void before() {
		p = new PApplet();
		map = new UnfoldingMap(p);
	}

	private void addCountryMarker() {
		List<Feature> countries = GeoJSONReader.loadData(p, p.sketchPath("data/data/countries.geo.json"));
		List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
	}

	private void addSquareMarker() {
		ArrayList<Location> list = new ArrayList<Location>();
		list.add(new Location(0, 0));
		list.add(new Location(0, squareWidth));
		list.add(new Location(squareWidth, squareWidth));
		list.add(new Location(squareWidth, 0));
		squareMarker = new SimplePolygonMarker(list);
	}

	@Test
	public void testSquareMarker() {
		addSquareMarker();

		assertTrue(squareMarker.isInsideByLocation(squareWidth / 2.0f, squareWidth / 2.0f));
		assertFalse(squareMarker.isInsideByLocation(-1, -1));
		assertFalse(squareMarker.isInsideByLocation(squareWidth + 1, squareWidth + 1));
	}

	@Test
	public void testCountryMarker() {
		addCountryMarker();

		Location berlinLocation = new Location(52.5f, 13.4f);
		Location pragueLocation = new Location(50.08f, 14.42f);

		for (Marker marker : map.getMarkers()) {
			if ("DEU".equals(marker.getId())) {

				AbstractShapeMarker shapeMarker = (AbstractShapeMarker) marker;

				assertTrue(shapeMarker.isInsideByLocation(berlinLocation));
				assertFalse(shapeMarker.isInsideByLocation(pragueLocation));
			}
		}

	}

}
