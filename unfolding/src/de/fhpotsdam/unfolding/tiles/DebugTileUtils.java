package de.fhpotsdam.unfolding.tiles;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import de.fhpotsdam.unfolding.core.Coordinate;

/**
 * Draws debug information atop the tile image.
 * 
 */
public class DebugTileUtils {

	private static final int TILE_WIDTH = 256;
	private static final int TILE_HEIGHT = 256;

	// Create debug tile, and draw it on top of the loaded tile
	public static PImage getDebugTile(Coordinate coord, PImage tileImage, PApplet p, boolean showDebugBorder,
			boolean showTileCoordinates) {
		PGraphics pg = p.createGraphics(TILE_WIDTH, TILE_HEIGHT, PApplet.P2D);
		pg.beginDraw();

		if (tileImage != null) {
			pg.image(tileImage, 0, 0);
		} else {
			pg.background(250);
			pg.stroke(0);
			pg.rect(0, 0, pg.width, pg.height);
			pg.ellipse(pg.width / 2, pg.height / 2, pg.width - 5, pg.height - 5);
		}

		if (showTileCoordinates) {
			pg.fill(0);
			String infoText = (int) coord.column + ", " + (int) coord.row + "\nz: " + (int) coord.zoom;
			pg.text(infoText, pg.width / 2 - pg.textWidth(infoText) / 2, pg.height / 2 - 10);
		}

		if (showDebugBorder) {
			pg.strokeWeight(2);
			pg.stroke(255, 0, 0, 100);
			pg.noFill();
			pg.rect(0, 0, pg.width, pg.height);
		}

		pg.endDraw();
		return pg.get();
	}

	public static void drawTileBorder(PGraphics pg) {
	}

}
