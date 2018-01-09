package de.fhpotsdam.unfolding.providers;

import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.MercatorProjection;
import de.fhpotsdam.unfolding.geo.Transformation;

/**
 * Provider based on tiles from the Dutch government. Only describes the Netherlands. 
 * https://nlmaps.nl/
 * https://github.com/kadaster/nlmaps/
 * 
 * Available map styles:
 * standaard: the default BRT-Achtergrondkaart in color
 * pastel: in pastel tints
 * grijs: in very low saturation
 * luchtfoto: aerial imagery  
 */
public class NLmaps {
	
	public static abstract class GenericNLmapsProvider extends AbstractMapTileUrlProvider
	{
		public GenericNLmapsProvider() {
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
	
	public static class Standaard extends GenericNLmapsProvider {
		public Standaard() {
		}

		public String[] getTileUrls(Coordinate coordinate) {
			// https://geodata.nationaalgeoregister.nl/tiles/service/wmts/brtachtergrondkaart/EPSG:3857/{z}/{x}/{y}.png
			String url = "http://geodata.nationaalgeoregister.nl/tiles/service/wmts/brtachtergrondkaart/EPSG:3857/" +
					getZoomString(coordinate) + ".png";
			return new String[] { url };
		}
	}
	
	public static class Pastel extends GenericNLmapsProvider {
		public Pastel() {
		}

		public String[] getTileUrls(Coordinate coordinate) {
			// https://geodata.nationaalgeoregister.nl/tiles/service/wmts/brtachtergrondkaartpastel/EPSG:3857/{z}/{x}/{y}.png
			String url = "http://geodata.nationaalgeoregister.nl/tiles/service/wmts/brtachtergrondkaartpastel/EPSG:3857/" +
					getZoomString(coordinate) + ".png";
			return new String[] { url };
		}
	}
	
	public static class Grijs extends GenericNLmapsProvider {
		public Grijs() {
		}

		public String[] getTileUrls(Coordinate coordinate) {
			// https://geodata.nationaalgeoregister.nl/tiles/service/wmts/brtachtergrondkaartpastel/EPSG:3857/{z}/{x}/{y}.png
			String url = "http://geodata.nationaalgeoregister.nl/tiles/service/wmts/brtachtergrondkaartgrijs/EPSG:3857/" +
					getZoomString(coordinate) + ".png";
			return new String[] { url };
		}
	}
	
	
	public static class Luchtfoto extends GenericNLmapsProvider {
		public Luchtfoto() {
		}

		public String[] getTileUrls(Coordinate coordinate) {
			// https://geodata.Io4.nl/luchtfoto/rgb/wmts/1.0.0/2016_ortho25/EPSG:3857/{z}/{x}/{y}.png
			String url = "http://geodata.nationaalgeoregister.nl/luchtfoto/rgb/wmts/1.0.0/2016_ortho25/EPSG:3857/" +
					getZoomString(coordinate) + ".png";
			return new String[] { url };
		}
	}



}
