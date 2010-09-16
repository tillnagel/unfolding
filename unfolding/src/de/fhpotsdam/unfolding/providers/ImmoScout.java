package de.fhpotsdam.unfolding.providers;

import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.providers.Microsoft.MicrosoftProvider;

public class ImmoScout {

	public static abstract class ImmoScoutProvider extends MicrosoftProvider {

		public ImmoScoutProvider() {
			super();
		}

		public int tileWidth() {
			// REVISIT ImmoScout's tiles are 250x250. But when this is changed accordingly
			// Processing's image loading throws an exception. But it seems this method never is used.
			return 256;
		}

		public int tileHeight() {
			return 256;
		}
	}

	public static class HeatMapProvider extends ImmoScoutProvider {
		public String[] getTileUrls(Coordinate coordinate) {

			String url = "http://heatmaps.immobilienscout24.de/geoserver/wmsproxy/KGS22_2009_whg_miete_gg/"
					+ getZoomString(sourceCoordinate(coordinate)) + ".png";
			return new String[] { url };
		}
	}

}
