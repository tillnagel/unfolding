/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhpotsdam.unfolding.providers;

import java.net.URI;

import org.apache.log4j.Logger;

import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.MercatorProjection;
import de.fhpotsdam.unfolding.geo.Transformation;

/**
 *
 * @author marcus
 */
public class CartoDBProvider extends AbstractMapTileUrlProvider {

	private String subdomain = null;
	private String table = null;
	private String sql = null;
	private String carto = null;
	public static Logger log = Logger.getLogger(CartoDBProvider.class);
	public AbstractMapProvider masterProvider = null;

	public CartoDBProvider(String account, String dbTable) {
		super(new MercatorProjection(26, new Transformation(1.068070779e7, 0.0f, 3.355443185e7, 0.0,
				-1.068070890e7, 3.355443057e7)));
		this.subdomain = account;
		this.table = dbTable;
	}

	public String getZoomString(Coordinate coordinate) {
		return (int) coordinate.zoom + "/" + (int) coordinate.column + "/" + (int) coordinate.row;
	}

	@Override
	public int tileWidth() {
		return 256;
	}

	@Override
	public int tileHeight() {
		return 256;
	}

	public String getStyle() {
		return carto;
	}

	public void setStyle(String carto) {
		this.carto = carto;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	@Override
	public String[] getTileUrls(Coordinate coordinate) {
		/*
		 * CartoDB Url Template
		 * http://{account}.cartodb.com/tiles/{table_name}/{z}/{x}/{y}.png?sql={SQL
		 * statement}&style={Carto style}
		 */
		try {
			// Build the query part of the URL
			String query = "";
			if (this.sql != null) {
				query += "sql=" + this.sql;
			}
			if (this.carto != null) {
				query += (this.sql != null ? "&" : "") + "style=" + this.carto;
			}

			// This should do proper URI encoding for the sql query and the carto css
			URI uri = new URI(
					"http",
					this.subdomain + ".cartodb.com",
					"/tiles/" + this.table + "/" + getZoomString(coordinate) + ".png",
					query,
					null);
			String request = uri.toASCIIString();

			// This should be our final url
			log.debug("CartoDB-Tile: " + request);

			// Now we can optionally blend the cartodb-tiles onto a basemap layer
			if (this.masterProvider != null) {
				// We use the internal mechanism of the Map-Class, so we just have to provide the additional urls in the String-Array 
				String[] url = this.masterProvider.getTileUrls(coordinate);
				String[] blend = new String[url.length + 1];
				for (int i = 0; i < url.length; i++) {
					blend[i] = url[i];
				}
				blend[blend.length - 1] = request;				
				return blend;
			} else {
				// Return the plain url
				return new String[]{request};
			}

		} catch (Exception e) {
			// Problem with url encoding, how to crash properly?
			log.error("Unable to create CartoDB Urls: " + e.getLocalizedMessage());
			return new String[]{""};
		}
	}
}