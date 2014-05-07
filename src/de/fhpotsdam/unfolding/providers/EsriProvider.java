package de.fhpotsdam.unfolding.providers;

import de.fhpotsdam.unfolding.providers.AbstractMapTileUrlProvider;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.MercatorProjection;
import de.fhpotsdam.unfolding.geo.Transformation;

/**
 * Provider based on Leaflet-providers: http://leaflet-extras.github.io/leaflet-providers/preview/index.html
 * Tiles (c)Esri - Source: Esri, DeLorme, NAVTEQ, USGS, Intermap, iPC, NRCAN, Esri Japan, METI, Esri China (Hong Kong), Esri (Thailand), TomTom, 2012
 */
public class EsriProvider {
	public static abstract class GenericEsriProvider extends AbstractMapTileUrlProvider {

		public GenericEsriProvider() {
			super(new MercatorProjection(26, new Transformation(1.068070779e7, 0.0, 3.355443185e7, 0.0,
					-1.068070890e7, 3.355443057e7)));
		}

		public String getZoomString(Coordinate coordinate) {
			return (int) coordinate.zoom + "/" + (int) coordinate.row + "/" + (int) coordinate.column;
		}

		public int tileWidth() {
			return 256;
		}

		public int tileHeight() {
			return 256;
		}

		public abstract String[] getTileUrls(Coordinate coordinate);
	}

	public static class WorldStreetMap extends GenericEsriProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/" + getZoomString(coordinate) + ".jpg";
			return new String[] { url };
		}
	}

	public static class DeLorme extends GenericEsriProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://server.arcgisonline.com/ArcGIS/rest/services/Specialty/DeLorme_World_Base_Map/MapServer/tile/" + getZoomString(coordinate) + ".jpg";
			return new String[] { url };
		}
	}

	public static class WorldTopoMap extends GenericEsriProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://server.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/" + getZoomString(coordinate) + ".jpg";
			return new String[] { url };
		}
	}

	public static class WorldTerrain extends GenericEsriProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://server.arcgisonline.com/ArcGIS/rest/services/World_Terrain_Base/MapServer/tile/" + getZoomString(coordinate) + ".jpg";
			return new String[] { url };
		}
	}
	
	
	public static class WorldShadedRelief extends GenericEsriProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://server.arcgisonline.com/ArcGIS/rest/services/World_Shaded_Relief/MapServer/tile/" + getZoomString(coordinate) + ".jpg";
			return new String[] { url };
		}
	}
	
	public static class WorldPhysical extends GenericEsriProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://server.arcgisonline.com/ArcGIS/rest/services/World_Physical_Map/MapServer/tile/" + getZoomString(coordinate) + ".jpg";
			return new String[] { url };
		}
	}
	
	public static class OceanBasemap extends GenericEsriProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://server.arcgisonline.com/ArcGIS/rest/services/Ocean_Basemap/MapServer/tile/" + getZoomString(coordinate) + ".jpg";
			return new String[] { url };
		}
	}
	
	public static class NatGeoWorldMap extends GenericEsriProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://server.arcgisonline.com/ArcGIS/rest/services/NatGeo_World_Map/MapServer/tile/" + getZoomString(coordinate) + ".jpg";
			return new String[] { url };
		}
	}
	
	public static class WorldGrayCanvas extends GenericEsriProvider {
		public String[] getTileUrls(Coordinate coordinate) {
			String url = "http://server.arcgisonline.com/ArcGIS/rest/services/Canvas/World_Light_Gray_Base/MapServer/tile/" + getZoomString(coordinate) + ".jpg";
			return new String[] { url };
		}
	}
}

