package de.fhpotsdam.unfolding.providers;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.MercatorProjection;
import de.fhpotsdam.unfolding.geo.Transformation;

/**
 * Various map tiles from Microsoft.
 */
public class Microsoft {

	public static abstract class MicrosoftProvider extends AbstractMapTileUrlProvider {

		public MicrosoftProvider() {
			super(new MercatorProjection(26, new Transformation(1.068070779e7, 0.0, 3.355443185e7, 0.0, -1.068070890e7,
					3.355443057e7)));
		}

		public String getZoomString(Coordinate coordinate) {
			return toQuadKey(coordinate);
		}

		public int tileWidth() {
			return 256;
		}

		public int tileHeight() {
			return 256;
		}

		public abstract String[] getTileUrls(Coordinate coordinate);

	}

	public static class RoadProvider extends MicrosoftProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://r" + (int) random(0, 4) + ".ortho.tiles.virtualearth.net/tiles/r"
					+ getZoomString(sourceCoordinate(coordinate)) + ".png?g=90&shading=hill";
			return new String[] { url };
		}
	}

	public static class AerialProvider extends MicrosoftProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://a" + (int) random(0, 4) + ".ortho.tiles.virtualearth.net/tiles/a"
					+ getZoomString(sourceCoordinate(coordinate)) + ".jpeg?g=90";
			return new String[] { url };
		}
	}

	public static class HybridProvider extends MicrosoftProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://h" + (int) random(0, 4) + ".ortho.tiles.virtualearth.net/tiles/h"
					+ getZoomString(sourceCoordinate(coordinate)) + ".jpeg?g=90";
			return new String[] { url };
		}
	}
	
	/**
	 * Converts QuadKey string to Coordinates.
	 */
	public static Coordinate fromQuadKey(String s) {
		// Return column, row, zoom for Microsoft tile string.
		String rowS = "";
		String colS = "";
		for (int i = 0; i < s.length(); i++) {
			int v = Integer.parseInt("" + s.charAt(i));
			String bv = PApplet.binary(v, 2);
			rowS += bv.charAt(0);
			colS += bv.charAt(1);
		}
		return new Coordinate(PApplet.unbinary(colS), PApplet.unbinary(rowS), s.length());
	}

	/**
	 * Converts coordinates to QuadKey string.
	 */
	public static String toQuadKey(Coordinate coord) {
		return toQuadKey((int) coord.column, (int) coord.row, (int) coord.zoom);
	}

	public static String toQuadKey(int col, int row, int zoom) {
		// Return string for Microsoft tile column, row, zoom
		String y = PApplet.binary(row, zoom);
		String x = PApplet.binary(col, zoom);
		String out = "";
		for (int i = 0; i < zoom; i++) {
			out += PApplet.unbinary("" + y.charAt(i) + x.charAt(i));
		}
		return out;
	}

	public static Coordinate fromMicrosoftRoad(String s) {
		// Return column, row, zoom for Microsoft Road tile string.
		return fromQuadKey(s);
	}

	public static String toMicrosoftRoad(int col, int row, int zoom) {
		// Return x, y, z for Microsoft Road tile column, row, zoom.
		return toQuadKey(col, row, zoom);
	}

	public static Coordinate fromMicrosoftAerial(String s) {
		// Return column, row, zoom for Microsoft Aerial tile string.
		return fromQuadKey(s);
	}

	public static String toMicrosoftAerial(int col, int row, int zoom) {
		// Return x, y, z for Microsoft Aerial tile column, row, zoom.
		return toQuadKey(col, row, zoom);
	}

}
