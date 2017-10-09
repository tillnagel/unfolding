package de.fhpotsdam.unfolding.examples.data.vectortiles;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;
import processing.core.PMatrix3D;

/**
 * Helper class to load and parse vector tiles. Loads features and markers for a single vector tile.
 */
public class VectorTilesUtils {

	private static final String VECTOR_TILE_API = "http://tile.mapzen.com/mapzen/vector/v1/";

	protected PApplet p;
	private UnfoldingMap map;
	protected String mapzenAPIKey = "YOUR_KEY_HERE";

	/**
	 * Creates this VectorTilesUtils for the given applet and the given map. Uses Mapzen's Vector Tiles server.
	 * 
	 * @param p
	 *            The Processing Applet.
	 * @param map
	 *            The UnfoldingMap.
	 */
	public VectorTilesUtils(PApplet p, UnfoldingMap map, String apiKey) {
		super();
		this.p = p;
		this.map = map;
		this.mapzenAPIKey = apiKey;
	}

	/**
	 * Loads all markers from a vector tile for the given screen position.
	 * 
	 * @param layers
	 *            The name of layers to load, e.g. "buildings", or "roads".
	 * @param x
	 *            The x screen coordinate for the vector tile.
	 * @param y
	 *            The y screen coordinate for the vector tile.
	 * @return All markers of the vector tile.
	 */
	public List<Marker> loadMarkersForScreenPos(String layers, int x, int y) {
		int[] coord = getCoordinate(x, y);
		List<Feature> features = loadVectorTiles(layers, map.getZoomLevel(), coord[0], coord[1]);
		List<Marker> markers = MapUtils.createSimpleMarkers(features);

		for (Marker m : markers) {
			m.setColor(p.color(255, 0, 0, 100));
		}

		return markers;
	}

	/**
	 * Loads all markers from all vector tiles for the current map view.
	 * 
	 * @param layers
	 *            The name of layers to load, e.g. "buildings", or "roads".
	 */
	public List<Marker> loadMarkersForCurrentMapView(String layers) {
		List<Marker> allMarkers = new ArrayList<Marker>();
		for (int x = 0; x < map.getWidth(); x += UnfoldingMap.TILE_WIDTH) {
			for (int y = 0; y < map.getHeight(); y += UnfoldingMap.TILE_HEIGHT) {
				List<Marker> tileMarkers = loadMarkersForScreenPos(layers, x, y);
				allMarkers.addAll(tileMarkers);
			}
		}
		return allMarkers;
	}

	public List<Feature> loadFeaturesForScreenPos(String layers, int x, int y) {
		int[] coord = getCoordinate(x, y);
		return loadVectorTiles(layers, map.getZoomLevel(), coord[0], coord[1]);
	}

	public List<Feature> loadFeaturesForCurrentMapView(String layers) {
		List<Feature> allFeatures = new ArrayList<Feature>();
		for (int x = 0; x < map.getWidth(); x += UnfoldingMap.TILE_WIDTH) {
			for (int y = 0; y < map.getHeight(); y += UnfoldingMap.TILE_HEIGHT) {
				List<Feature> tileFeatures = loadFeaturesForScreenPos(layers, x, y);
				allFeatures.addAll(tileFeatures);
			}
		}
		return allFeatures;
	}

	/**
	 * Loads features of a vector tile.
	 * 
	 * @param layers
	 *            The name of layers to load, e.g. "buildings", or "roads".
	 * @param zoomLevel
	 *            The zoom level of the vector tile.
	 * @param x
	 *            The x coordinate for the vector tile.
	 * @param y
	 *            The y coordinate for the vector tile.
	 * @return All features of the vector tile.
	 */
	public List<Feature> loadVectorTiles(String layers, int zoomLevel, int x, int y) {
		String vectorTileUrl = VECTOR_TILE_API + layers + "/" + zoomLevel + "/" + x + "/" + y + ".json";
		vectorTileUrl += "?api_key=" + mapzenAPIKey;
		PApplet.println("Loading vector tile from " + vectorTileUrl);
		return GeoJSONReader.loadData(p, vectorTileUrl);
	}

	protected int[] getCoordinate(int x, int y) {
		float[] pos = map.mapDisplay.getInnerObjectFromObjectPosition(x, y);
		Coordinate coord = getCoordinateFromInnerPosition(pos[0], pos[1]).zoomTo(map.getZoomLevel());
		return new int[] { (int) coord.column, (int) coord.row };
	}

	// Copied from map.mapDisplay.getCoordinateFromInnerPosition
	protected Coordinate getCoordinateFromInnerPosition(float x, float y) {
		PMatrix3D m = new PMatrix3D();
		float tl[] = new float[3];
		m.mult(new float[] { 0, 0, 0 }, tl);
		float br[] = new float[3];
		m.mult(new float[] { UnfoldingMap.TILE_WIDTH, UnfoldingMap.TILE_HEIGHT, 0 }, br);

		float col = (x - tl[0]) / (br[0] - tl[0]);
		float row = (y - tl[1]) / (br[1] - tl[1]);
		Coordinate coord = new Coordinate(row, col, 0);
		return coord;
	}

}
