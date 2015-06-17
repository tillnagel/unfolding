package de.fhpotsdam.unfolding.tiles;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import processing.core.PConstants;
import processing.core.PImage;

/**
 * Loads map tile images from a MBTiles SQLite database.
 * 
 * You need to provide the jdbcConnectionString to connect to the database file. e.g. "./data/my-map.mbtiles"
 * 
 * This class is part of the <a href="http://code.google.com/p/unfolding/">Unfolding</a> map library. See <a
 * href="http://tillnagel.com/2011/06/tilemill-for-processing/">TileMill for Processing</a> for more information.
 */
public class MBTilesLoaderUtils {

	public static final String SQLITE_JDBC_DRIVER = "org.sqlite.JDBC";

	/**
	 * Loads the tile for given parameters as image.
	 * 
	 * @param column
	 *            The column of the tile.
	 * @param row
	 *            The row of the tile.
	 * @param zoomLevel
	 *            The zoom level of the tile.
	 * @param jdbcConnectionString
	 *            The path to the MBTiles database.
	 * @return The tile as PImage, or null if not found.
	 */
	public static PImage getMBTile(int column, int row, int zoomLevel, String jdbcConnectionString) {
		PImage img = null;
		try {
			byte[] tileData = getMBTileData(column, row, zoomLevel, jdbcConnectionString);
			if (tileData != null) {
				img = getAsImage(tileData);
			} else {
				// System.err.println("No tile found for " + column + "," + row + " " + zoomLevel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}

	protected static Map<String, Connection> connectionsMap = new HashMap<String, Connection>();

	/**
	 * Loads the MBTile data from the database as blob, and returns it as byte array.
	 * 
	 * @param column
	 *            The column of the tile.
	 * @param row
	 *            The row of the tile.
	 * @param zoomLevel
	 *            The zoom level of the tile.
	 * @return The tile as byte array with image information, or an empty array if not found.
	 */
	protected static byte[] getMBTileData(int column, int row, int zoomLevel, String jdbcConnectionString)
			throws Exception {
		Class.forName(SQLITE_JDBC_DRIVER);

		Connection conn = null;
		byte[] tileData = null;

		try {
			conn = connectionsMap.get(jdbcConnectionString);
			if (conn == null) {
				conn = DriverManager.getConnection(jdbcConnectionString);
				connectionsMap.put(jdbcConnectionString, conn);
			}

			Statement stat = conn.createStatement();
			PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM tiles WHERE tile_column = ? AND tile_row = ? AND zoom_level = ?;");
			prep.setInt(1, column);
			prep.setInt(2, row);
			prep.setInt(3, zoomLevel);

			ResultSet rs = prep.executeQuery();

			while (rs.next()) {
				tileData = rs.getBytes("tile_data");
			}
			rs.close();
			stat.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
//			try {
//				if (conn != null)
//					conn.close();
//			} catch (SQLException e) {
//				// connection close failed.
//				System.err.println(e);
//			}
		}
		return tileData;
	}

	/**
	 * Converts the byte array into a PImage. Expects the byte array to be in ARGB (RGB with alpha channel).
	 * 
	 * Adapted from toxi
	 * 
	 * @param bytes
	 *            The image information as byte array.
	 * @return A PImage.
	 */
	protected static PImage getAsImage(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}

		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			BufferedImage bimg = ImageIO.read(bis);
			PImage img = new PImage(bimg.getWidth(), bimg.getHeight(), PConstants.ARGB);
			bimg.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);
			img.updatePixels();
			return img;
		} catch (Exception e) {
			System.err.println("Can't create image from buffer");
			e.printStackTrace();
			return null;
		}
	}

}
