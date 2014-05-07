package de.fhpotsdam.unfolding.providers;

import de.fhpotsdam.unfolding.providers.AbstractMapTileUrlProvider;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.MercatorProjection;
import de.fhpotsdam.unfolding.geo.Transformation;

/**
 * Provider based on Leaflet-providers: http://leaflet-extras.github.io/leaflet-providers/preview/index.html
 * Various map tiles from Opencycle. http://www.opencyclemap.org
 */
public class ThunderforestProvider {
	public static abstract class GenericThunderforestProvider extends AbstractMapTileUrlProvider {

		public GenericThunderforestProvider() {
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

	public static class OpenCycleMap extends GenericThunderforestProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://tile.thunderforest.com/cycle/" + getZoomString(coordinate) + ".png";
			return new String[] { url };
		}
	}

	public static class Transport extends GenericThunderforestProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://tile.thunderforest.com/transport/" + getZoomString(coordinate) + ".png";
			return new String[] { url };
		}
	}
	
	public static class Landscape extends GenericThunderforestProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://tile.thunderforest.com/landscape/" + getZoomString(coordinate) + ".png";
			return new String[] { url };
		}
	}
	
	public static class Outdoors extends GenericThunderforestProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://tile.thunderforest.com/outdoors/" + getZoomString(coordinate) + ".png";
			return new String[] { url };
		}
	}

}

