package de.fhpotsdam.unfolding.providers;

import de.fhpotsdam.unfolding.providers.AbstractMapTileUrlProvider;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.MercatorProjection;
import de.fhpotsdam.unfolding.geo.Transformation;

/**
 * Provider based on Leaflet-providers: http://leaflet-extras.github.io/leaflet-providers/preview/index.html
 * Various map tiles from GIScience Research Group @ University of Heidelberg: http://giscience.uni-hd.de
 */
public class OpenMapSurferProvider {
	public static abstract class GenericOpenMapSurferProvider extends AbstractMapTileUrlProvider {

		public GenericOpenMapSurferProvider() {
			super(new MercatorProjection(26, new Transformation(1.068070779e7, 0.0, 3.355443185e7, 0.0,
					-1.068070890e7, 3.355443057e7)));
		}

		public String getZoomString(Coordinate coordinate) {
			return "x=" + (int) coordinate.column + "&y=" + (int) coordinate.row + "&z=" + (int) coordinate.zoom;
		}

		public int tileWidth() {
			return 256;
		}

		public int tileHeight() {
			return 256;
		}

		public abstract String[] getTileUrls(Coordinate coordinate);
	}

	public static class Roads extends GenericOpenMapSurferProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://openmapsurfer.uni-hd.de/tiles/roads/" + getZoomString(coordinate) ;
			return new String[] { url };
		}
	}

	public static class Grayscale extends GenericOpenMapSurferProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://openmapsurfer.uni-hd.de/tiles/roadsg/" + getZoomString(coordinate) ;
			return new String[] { url };
		}
	}

	
}

