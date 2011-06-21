package de.fhpotsdam.unfolding.tiles;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import de.fhpotsdam.unfolding.core.Coordinate;

public class DebugTileUtils {

	private static final int TILE_WIDTH = 255;
	private static final int TILE_HEIGHT = 255;

	public static PImage getDebugTile(Coordinate coord, PImage tileImage, PApplet p) {
		PGraphics pg = p.createGraphics(TILE_WIDTH, TILE_HEIGHT, PApplet.P2D);
		pg.beginDraw();
		// pg.textFont(font);

		if (tileImage != null) {
			pg.image(tileImage, 0, 0);
		} else {
			pg.background(250);
			pg.stroke(0);
			pg.rect(0, 0, pg.width, pg.height);
			pg.ellipse(pg.width / 2, pg.height / 2, pg.width - 5, pg.height - 5);
		}

		pg.fill(0);
		String infoText = coord.column + ", " + coord.row + "\nz: " + coord.zoom;
		pg.text(infoText, pg.width / 2 - pg.textWidth(infoText) / 2, pg.height / 2 - 10);
		pg.endDraw();
		String tempName = coord.column + "-" + coord.row + "-" + coord.zoom + ".png";
		pg.save(tempName);

		PImage img = p.loadImage(tempName);
		return img;
	}

}
