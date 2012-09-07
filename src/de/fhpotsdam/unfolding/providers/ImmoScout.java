package de.fhpotsdam.unfolding.providers;

import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.providers.Microsoft.MicrosoftProvider;

public class ImmoScout {

	public static abstract class ImmoScoutProvider extends MicrosoftProvider {

		public ImmoScoutProvider() {
			super();
		}

		public String getZoomString(Coordinate coordinate) {
			return "&z=" + (int) coordinate.zoom + "&x=" + (int) coordinate.column + "&y=" + (int) coordinate.row;
		}

		public int tileWidth() {
			return 256;
		}

		public int tileHeight() {
			return 256;
		}
	}

	public static class HeatMapProvider extends ImmoScoutProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			// old:
			// "http://heatmaps.immobilienscout24.de/geoserver/wmsproxy/KGS22_2009_whg_miete_gg/"
			String url = "http://heatmaps.immobilienscout24.de/geoserver/gmwmsproxy?layer=q22011_q22012_Wohnung_Miete_@@@@"
					+ getZoomString(sourceCoordinate(coordinate));
			return new String[] { url };
		}
	}

}
