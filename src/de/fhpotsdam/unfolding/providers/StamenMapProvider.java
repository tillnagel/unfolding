package de.fhpotsdam.unfolding.providers;

import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.providers.OpenStreetMap.GenericOpenStreetMapProvider;
import de.fhpotsdam.unfolding.providers.OpenStreetMap.OpenStreetMapProvider;

public class StamenMapProvider extends OpenStreetMapProvider {

	public static class Toner extends GenericOpenStreetMapProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://spaceclaw.stamen.com/toner/" + getZoomString(coordinate) + ".png";
			return new String[] { url };
		}
	}

	public static class TonerBackground extends GenericOpenStreetMapProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://tile.stamen.com/toner-background/" + getZoomString(coordinate) + ".png";
			return new String[] { url };
		}
	}

	public static class TonerLite extends GenericOpenStreetMapProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://tile.stamen.com/toner-lite/" + getZoomString(coordinate) + ".png";
			return new String[] { url };
		}
	}

	public static class WaterColor extends GenericOpenStreetMapProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://b.tile.stamen.com/watercolor/" + getZoomString(coordinate) + ".png";
			return new String[] { url };
		}
	}

}
