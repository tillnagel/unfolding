
package de.fhpotsdam.unfolding.providers;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.MercatorProjection;
import de.fhpotsdam.unfolding.geo.Transformation;

public class OpenStreetMap
{
	public static abstract class GenericOpenStreetMapProvider extends AbstractMapProvider
	{

		public GenericOpenStreetMapProvider()
		{
			super(new MercatorProjection(26, new Transformation(1.068070779e7f, 0.0f, 3.355443185e7f, 0.0f, -1.068070890e7f, 3.355443057e7f)));
		}

		public String getZoomString(Coordinate coordinate)
		{
			return toOpenStreetMap((int) coordinate.column, (int) coordinate.row, (int) coordinate.zoom);
		}

		public int tileWidth()
		{
			return 256;
		}

		public int tileHeight()
		{
			return 256;
		}

		public abstract String[] getTileUrls(Coordinate coordinate);
	}

	public static class OpenStreetMapProvider extends GenericOpenStreetMapProvider
	{
		public String[] getTileUrls(Coordinate coordinate)
		{
			String url = "http://tile.openstreetmap.org/" + (int)coordinate.zoom + "/" + (int)coordinate.column + "/" + (int)coordinate.row + ".png";
			return new String[] { url };
		}
	}
	
	public static class CloudmadeProvider extends GenericOpenStreetMapProvider
	{
		private String api_key;
		private int style_id;
		
		public CloudmadeProvider(String _api_key, int _style_id)
		{
			api_key = _api_key;
			style_id = _style_id;
		}
		
		public String[] getTileUrls(Coordinate coordinate)
		{
			String url = "http://a.tile.cloudmade.com/" + api_key + "/" + style_id + "/256/" + (int)coordinate.zoom + "/" + (int)coordinate.column + "/" + (int)coordinate.row + ".png";
			//PApplet.println(url);
			return new String[] { url };
		}
	}

	public static Coordinate fromOpenStreetMap(String s)
	{
		// Return column, row, zoom for OpenStreetMap tile string.
		String rowS = "";
		String colS = "";
		for (int i = 0; i < s.length(); i++)
		{
			int v = Integer.parseInt("" + s.charAt(i));
			String bv = PApplet.binary(v, 2);
			rowS += bv.charAt(0);
			colS += bv.charAt(1);
		}
		return new Coordinate(PApplet.unbinary(colS), PApplet.unbinary(rowS), s.length());
	}

	public static String toOpenStreetMap(int col, int row, int zoom)
	{
		// Return string for OpenStreetMap tile column, row, zoom
		String y = PApplet.binary(row, zoom);
		String x = PApplet.binary(col, zoom);
		String out = "";
		for (int i = 0; i < zoom; i++)
		{
			out += PApplet.unbinary("" + y.charAt(i) + x.charAt(i));
		}
		return out;
	}
}
