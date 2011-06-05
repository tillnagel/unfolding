package de.fhpotsdam.unfolding.mapdisplay;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.imageio.ImageIO;

import processing.core.PConstants;
import processing.core.PImage;

public class MBTilesLoaderUtils {

	public static final String jdbcConnectionString = "jdbc:sqlite:../unfolding/data/muse-dark-2-8.mbtiles";

	public static PImage getMBTile(int column, int row, int zoomLevel) {
		PImage img = null;
		try {
			byte[] tileData = getMBTileData(column, row, zoomLevel);
			img = getAsImage(tileData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}

	public static byte[] getMBTileData(int column, int row, int zoomLevel) throws Exception {
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection(jdbcConnectionString);
		Statement stat = conn.createStatement();
		PreparedStatement prep = conn
				.prepareStatement("select * from tiles where tile_column = ? and tile_row = ? and zoom_level = ?;");
		prep.setInt(1, column);
		prep.setInt(2, row);
		prep.setInt(3, zoomLevel);

		ResultSet rs = prep.executeQuery();
		byte[] tileData = new byte[0];
		while (rs.next()) {
			tileData = rs.getBytes("tile_data");
		}
		rs.close();
		stat.close();
		conn.close();

		return tileData;
	}

	// Adapted from toxi
	protected static PImage getAsImage(byte[] bytes) {
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
		}
		return null;
	}

	private static void getMBTileNoBlob(int column, int row, int zoomLevel) throws Exception {
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:data/muse-dark.mbtiles");
		Statement stat = conn.createStatement();
		PreparedStatement prep = conn
				.prepareStatement("select * from tiles where tile_column = ? and tile_row = ? and zoom_level = ?;");
		prep.setInt(1, column);
		prep.setInt(2, row);
		prep.setInt(3, zoomLevel);

		ResultSet rs = prep.executeQuery();
		while (rs.next()) {
			System.out.println("zoomLevel = " + rs.getInt("zoom_level"));
			System.out.println("tileCol = " + rs.getInt("tile_column"));
		}
		rs.close();
		stat.close();
		conn.close();
	}

}
