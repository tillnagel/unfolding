package de.fhpotsdam.unfolding.providers;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.MercatorProjection;
import de.fhpotsdam.unfolding.geo.Transformation;

public class GeoMapApp {

	public static abstract class GeoMapAppProvider extends AbstractMapTileUrlProvider {

		public GeoMapAppProvider() {
			super(new MercatorProjection(26, new Transformation(1.068070779e7, 0.0,
					3.355443185e7, 0.0, -1.068070890e7, 3.355443057e7)));
		}

		public String getZoomString(Coordinate coordinate) {
			return toMicrosoft((int) coordinate.column, (int) coordinate.row, (int) coordinate.zoom);
		}

		public int tileWidth() {
			return 336;
			// return 320;
		}

		public int tileHeight() {
			return 336;
			// return 320;
		}

		public abstract String[] getTileUrls(Coordinate coordinate);

	}

	public static class TopologicalGeoMapProvider extends GeoMapAppProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String path = getPath(coordinate);
			// String url = "http://dev.geomapapp.org/MapApp/merc_320_1024/" +
			// path;
			String url = "http://r" + (int) random(0, 4) + ".ortho.tiles.virtualearth.net/tiles/r"
					+ getZoomString(sourceCoordinate(coordinate)) + ".png?g=90&shading=hill";
			return new String[] { url };
		}
	}

	public static String getPath(Coordinate _coordinate) {
		String path;
		switch ((int) _coordinate.zoom) {
		case 1:
			path = "i_512";
			float column_middle = _coordinate.column / 2;
			float row_middle = _coordinate.row / 2;

			path += "/E" + (int) _coordinate.column;
			int col_subdivs = (int) _coordinate.column % 4;
			System.out.println(col_subdivs + " " + _coordinate.column + " (" + _coordinate.zoom
					+ ")");
			break;
		case 2:
			path = "i_256";
			break;
		case 3:
			path = "i_128";
			break;
		case 4:
			path = "i_64";
			break;
		case 5:
			path = "i_32";
			break;
		case 6:
			path = "i_16";
			break;
		case 7:
			path = "i_8";
			break;
		case 8:
			path = "i_4";
			break;
		case 9:
			path = "i_2";
			break;
		case 10:
			path = "i_1";
			break;
		default:
			path = "i_i";
		}
		return path;
	}

	public static Coordinate fromMicrosoft(String s) {
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

	public static String toMicrosoft(int col, int row, int zoom) {
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
		return fromMicrosoft(s);
	}

	public static String toMicrosoftRoad(int col, int row, int zoom) {
		// Return x, y, z for Microsoft Road tile column, row, zoom.
		return toMicrosoft(col, row, zoom);
	}

	public static Coordinate fromMicrosoftAerial(String s) {
		// Return column, row, zoom for Microsoft Aerial tile string.
		return fromMicrosoft(s);
	}

	public static String toMicrosoftAerial(int col, int row, int zoom) {
		// Return x, y, z for Microsoft Aerial tile column, row, zoom.
		return toMicrosoft(col, row, zoom);
	}

}
