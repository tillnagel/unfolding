package de.fhpotsdam.unfolding.tiles;

import processing.core.PApplet;
import processing.core.PImage;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;

/**
 * Loads tiles from the MapProvider. Will be used in a background thread.
 * 
 * Two kinds of MapProviders are supported:
 * - AbstractMapTileProvider returning an tile image, directly.
 * - AbstractMapTileUrlProvider returning an URL from which this TileLoader loads the tile from. 
 * 
 */
public class TileLoader implements Runnable {

	PApplet p;
	AbstractMapProvider provider;
	TileLoaderListener listener;

	Coordinate coordinate;

	public TileLoader(PApplet p, AbstractMapProvider provider, TileLoaderListener listener, Coordinate coordinate) {
		this.p = p;
		this.provider = provider;
		this.listener = listener;
		this.coordinate = coordinate;
	}

	public void run() {
		PImage img = provider.getTile(coordinate);

		if (img == null) {
			// If provider does not return an image try to get the URLs and load the tile images.
			String[] urls = provider.getTileUrls(coordinate);
			if (urls != null) {
				img = getTileFromUrl(urls);
			}
		}

		listener.tileLoaded(coordinate, img);
	}

	protected PImage getTileFromUrl(String[] urls) {
		// Load image from URL (local file included)
		// NB: Use 'unknown' as content-type to let loadImage decide
		PImage img = p.loadImage(urls[0], "unknown");

		// if (OVERLAY_DEBUG_TILES) {
		// // Create debug tile, and draw it on top of the loaded tile
		// img = DebugTileUtils.getDebugTile(coord, img, p);
		// }

		if (img != null) {
			// If array contains multiple URLs, load all images and blend them together
			for (int i = 1; i < urls.length; i++) {
				PImage img2 = p.loadImage(urls[i], "unknown");
				if (img2 != null) {
					img.blend(img2, 0, 0, img.width, img.height, 0, 0, img.width, img.height, PApplet.BLEND);
				}
			}
		}

		return img;
	}

}
