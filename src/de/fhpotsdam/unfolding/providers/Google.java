package de.fhpotsdam.unfolding.providers;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.MercatorProjection;
import de.fhpotsdam.unfolding.geo.Transformation;

/**
 * Google Maps, for informational purpose, only. See Google's Terms of Service for usage
 * conditions.
 */
public class Google {
	
	public static abstract class GoogleProvider extends AbstractMapTileUrlProvider {

		public GoogleProvider() {
			super(new MercatorProjection(26, new Transformation(1.068070779e7, 0.0, 3.355443185e7, 0.0,
					-1.068070890e7, 3.355443057e7)));
		}

		public String getZoomString(Coordinate coordinate) {
			return toGoogle((int) coordinate.column, (int) coordinate.row, (int) coordinate.zoom);
		}

		public int tileWidth() {
			return 256;
		}

		public int tileHeight() {
			return 256;
		}

		public abstract String[] getTileUrls(Coordinate coordinate);
	}
	
	/**
	 * Google Terrain Map.
	 */
	public static class GoogleTerrainProvider extends GoogleProvider {
		public GoogleTerrainProvider() {
		}

		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://mt1.google.com/vt/v=w2p.116&hl=de&x=" + (int) coordinate.column + "&y="
					+ (int) coordinate.row + "&z=" + (int) coordinate.zoom + "&s=Galileo";
			return new String[] { url };
		}
	}

	/**
	 * Standard Google Map. 
	 */
	public static class GoogleMapProvider extends GoogleProvider {
		public GoogleMapProvider() {
		}

		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://mt1.google.com/vt/lyrs=m@116&hl=de&x=" + (int) coordinate.column + "&y="
					+ (int) coordinate.row + "&z=" + (int) coordinate.zoom + "&s=Galileo";
			return new String[] { url };
		}
	}

	/**
	 * Simplified Google Map. 
	 */
	public static class GoogleSimplifiedProvider extends GoogleProvider {
		public GoogleSimplifiedProvider() {
		}

		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://mt1.googleapis.com/vt?&x=" + (int) coordinate.column + "&y=" + (int) coordinate.row
					+ "&z=" + (int) coordinate.zoom
					+ "&apistyle=s.t%3A3|p.v%3Asimplified%2Cs.t%3A2|p.v%3Aoff%2Cs.t%3A81|p.v%3Aoff";
			return new String[] { url };
		}
	}

	public static class GoogleSimplified2Provider extends GoogleProvider {
		public GoogleSimplified2Provider() {
		}

		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://mt1.googleapis.com/vt?&x=" + (int) coordinate.column
					+ "&y="
					+ (int) coordinate.row
					+ "&z="
					+ (int) coordinate.zoom
					// +
					// "&apistyle=s.t%3A2%7Cp.v%3Aoff%2Cs.t%3A3%7Cs.e%3Al%7Cp.v%3Aoff%2Cs.t%3A1%7Cs.e%3Al%7Cp.v%3Aoff%2Cs.t%3A5%7Cs.e%3Al%7Cp.v%3Aoff%2Cs.t%3A6%7Cs.e%3Al%7Cp.v%3Aoff%2Cs.t%3A6%7Cp.l%3A55%2Cs.t%3A51%7Cp.v%3Aoff%2Cs.t%3A81%7Cp.v%3Aoff%2Cs.t%3A50%7Cp.v%3Asimplified%7Cp.l%3A55%2Cs.t%3A49%7Cp.v%3Asimplified%7Cp.l%3A55%2Cs.t%3A1057%7Cp.v%3Aoff";
					+ "&apistyle=s.t%3A1%7Cs.e%3Al%7Cp.v%3Aoff%2Cs.t%3A5%7Cs.e%3Al%7Cp.v%3Aoff%2Cs.t%3A2%7Cs.e%3Al%7Cp.v%3Aoff%2Cs.t%3A3%7Cs.e%3Al%7Cp.v%3Aoff%2Cs.t%3A6%7Cs.e%3Al%7Cp.v%3Aoff%2Cs.t%3A3%7Cp.v%3Aoff%2Cs.t%3A5%7Cp.v%3Aoff%2Cs.t%3A2%7Cp.v%3Aoff%2Cs.t%3A4%7Cp.v%3Aoff%2Cs.t%3A5%7Cp.v%3Aoff%7Cp.l%3A50%2Cs.t%3A6%7Cp.l%3A50&s=Ga&style=api%7Csmartmaps";
			return new String[] { url };
		}
	}

	// http://mt1.googleapis.com/vt?lyrs=m@163000000&src=apiv3&hl=en-GB&x=6455&y=4069&z=13&apistyle=s.t%3A1%7Cs.e%3Al%7Cp.v%3Aoff%2Cs.t%3A5%7Cs.e%3Al%7Cp.v%3Aoff%2Cs.t%3A2%7Cs.e%3Al%7Cp.v%3Aoff%2Cs.t%3A3%7Cs.e%3Al%7Cp.v%3Aoff%2Cs.t%3A6%7Cs.e%3Al%7Cp.v%3Aoff%2Cs.t%3A3%7Cp.v%3Aoff%2Cs.t%3A5%7Cp.v%3Aoff%2Cs.t%3A2%7Cp.v%3Aoff%2Cs.t%3A4%7Cp.v%3Aoff%2Cs.t%3A5%7Cp.v%3Aoff%7Cp.l%3A50%2Cs.t%3A6%7Cp.l%3A50&s=Ga&style=api%7Csmartmaps
	// http://mt0.googleapis.com/vt?lyrs=m@163000000&src=apiv3&hl=en-GB&x=51676&s=&y=32536&z=16&apistyle=s.t%3A2%7Cp.v%3Aoff%2Cs.t%3A3%7Cs.e%3Al%7Cp.v%3Aoff%2Cs.t%3A1%7Cs.e%3Al%7Cp.v%3Aoff%2Cs.t%3A5%7Cs.e%3Al%7Cp.v%3Aoff%2Cs.t%3A6%7Cs.e%3Al%7Cp.v%3Aoff%2Cs.t%3A6%7Cp.l%3A55%2Cs.t%3A51%7Cp.v%3Aoff%2Cs.t%3A81%7Cp.v%3Aoff%2Cs.t%3A50%7Cp.v%3Asimplified%7Cp.l%3A55%2Cs.t%3A49%7Cp.v%3Asimplified%7Cp.l%3A55%2Cs.t%3A1057%7Cp.v%3Aoff&s=Gali&style=api%7Csmartmaps

	public static Coordinate fromGoogle(String s) {
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

	public static String toGoogle(int col, int row, int zoom) {
		// Return string for Microsoft tile column, row, zoom
		String y = PApplet.binary(row, zoom);
		String x = PApplet.binary(col, zoom);
		String out = "";
		for (int i = 0; i < zoom; i++) {
			out += PApplet.unbinary("" + y.charAt(i) + x.charAt(i));
		}
		return out;
	}
}
