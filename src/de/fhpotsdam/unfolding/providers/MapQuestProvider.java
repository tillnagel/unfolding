package de.fhpotsdam.unfolding.providers;

import de.fhpotsdam.unfolding.providers.AbstractMapTileUrlProvider;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.MercatorProjection;
import de.fhpotsdam.unfolding.geo.Transformation;

/**
 * Provider based on Leaflet-providers: http://leaflet-extras.github.io/leaflet-providers/preview/index.html
 * Various map tiles courtesy of MapQuest: http://www.mapquest.com
 */
public class MapQuestProvider {
	public static abstract class GenericMapQuestProvider extends AbstractMapTileUrlProvider {

		public GenericMapQuestProvider() {
			super(new MercatorProjection(26, new Transformation(1.068070779e7, 0.0, 3.355443185e7, 0.0,
					-1.068070890e7, 3.355443057e7)));
		}

		public String getZoomString(Coordinate coordinate) {
			return (int) coordinate.zoom + "/" + (int) coordinate.column + "/" + (int) coordinate.row;
		}

		public int tileWidth() {
			return 256;
		}

		public int tileHeight() {
			return 256;
		}

		public abstract String[] getTileUrls(Coordinate coordinate);
	}

	public static class OSM extends GenericMapQuestProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://otile1.mqcdn.com/tiles/1.0.0/map/" + getZoomString(coordinate) + ".jpeg";
			return new String[] { url };
		}
	}

	public static class Aerial extends GenericMapQuestProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://otile1.mqcdn.com/tiles/1.0.0/sat/" + getZoomString(coordinate) + ".jpg";
			return new String[] { url };
		}
	}

	
}

