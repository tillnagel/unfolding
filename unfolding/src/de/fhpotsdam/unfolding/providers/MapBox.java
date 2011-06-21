package de.fhpotsdam.unfolding.providers;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.providers.OpenStreetMap.OpenStreetMapProvider;

public class MapBox {

	public static abstract class MapBoxProvider extends OpenStreetMapProvider {

		public MapBoxProvider() {
			super();
		}

		public String getZoomString(Coordinate coordinate) {
			// Rows are numbered from bottom to top (opposite to OSM)
			float gridSize = PApplet.pow(2, coordinate.zoom);
			float negativeRow = gridSize - coordinate.row - 1;

			return (int) coordinate.zoom + "/" + (int) coordinate.column + "/" + (int) negativeRow;
		}
	}

	public static class WorldLightProvider extends MapBoxProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://c.tile.mapbox.com/mapbox/1.0.0/world-light/" + getZoomString(coordinate) + ".png";
			return new String[] { url };
		}
	}

	public static class ControlRoomProvider extends MapBoxProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://c.tile.mapbox.com/mapbox/1.0.0/control-room/" + getZoomString(coordinate) + ".png";
			return new String[] { url };
		}
	}
	
	// For testing non-exported map styles from your local TileMill server.
	// TODO Mention and explain in tutorial
	public static class MuseDarkStyleProvider extends MapBoxProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://localhost:8889/1.0.0/aHR0cDovL2xvY2FsaG9zdDo4ODg5L2FwaS9Qcm9qZWN0L2NvbnRyb2xfcm9vbT8xMzA3MjEwNDEw/"
					+ getZoomString(coordinate) + ".png";
			return new String[] { url };
		}
	}
	
	
	// REMOVE 
	public static class BlankProvider extends MapBoxProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://tillnagel.com/transparent255.png";
			return new String[] { url };
		}
	}

}
