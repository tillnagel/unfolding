package de.fhpotsdam.unfolding.providers;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.MercatorProjection;
import de.fhpotsdam.unfolding.geo.Transformation;

public class Google {
	public static abstract class GoogleProvider extends AbstractMapTileUrlProvider {

		public GoogleProvider() {
			super(new MercatorProjection(26, new Transformation(1.068070779e7f, 0.0f, 3.355443185e7f, 0.0f,
					-1.068070890e7f, 3.355443057e7f)));
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

	public static class GoogleTerrainProvider extends GoogleProvider {
		public GoogleTerrainProvider() {
		}

		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://mt1.google.com/vt/v=w2p.116&hl=de&x=" + (int) coordinate.column + "&y="
					+ (int) coordinate.row + "&z=" + (int) coordinate.zoom + "&s=Galileo";
			return new String[] { url };
		}
	}

	public static class GoogleMapProvider extends GoogleProvider {
		public GoogleMapProvider() {
		}

		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://mt1.google.com/vt/lyrs=m@116&hl=de&x=" + (int) coordinate.column + "&y="
					+ (int) coordinate.row + "&z=" + (int) coordinate.zoom + "&s=Galileo";
			return new String[] { url };
		}
	}

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
