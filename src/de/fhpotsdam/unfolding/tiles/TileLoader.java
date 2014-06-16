package de.fhpotsdam.unfolding.tiles;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.mapdisplay.AbstractMapDisplay;
import de.fhpotsdam.unfolding.mapdisplay.Java2DMapDisplay;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;

/**
 * Loads tiles from the MapProvider. Will be used in a background thread.
 * 
 * Two kinds of MapProviders are supported:
 * <ul>
 * <li>AbstractMapTileProvider returning a tile image, directly.</li>
 * <li>AbstractMapTileUrlProvider returning an URL from which this TileLoader loads the tile from.</li>
 * </ul>
 * 
 * Tile organization is handled in {@link AbstractMapDisplay} (caching) and
 * {@link Java2DMapDisplay} (rendering).
 */
public class TileLoader implements Runnable {

	/** Shows borders around each tile. */
	private static final boolean SHOW_DEBUG_BORDER = false;
	/** Shows coordinate information for tile. */
	private static final boolean SHOW_TILE_COORDINATES = false;

	/** Shows coordinate information for tile, i.e. placed atop original tile image. */
	public boolean showDebugBorder = SHOW_DEBUG_BORDER;
	public boolean showTileCoordinates = SHOW_TILE_COORDINATES;

	/** The parent applet. */
	protected PApplet p;
	/** The tile provider. */
	protected AbstractMapProvider provider;
	/** The listener to call method after tile has been loaded. */
	protected TileLoaderListener listener;
	/** The actual coordinates of the tile to load. */
	protected Coordinate coordinate;

	/** Empty image in tile dimension to be used as place holder. */
	private PImage cachedEmpyImage;

	public TileLoader(PApplet p, AbstractMapProvider provider, TileLoaderListener listener, Coordinate coordinate) {
		this.p = p;
		this.provider = provider;
		this.listener = listener;
		this.coordinate = coordinate;

		cachedEmpyImage = new PImage(provider.tileWidth(), provider.tileHeight(), PConstants.ARGB);
	}

	/**
	 * Gets tile from provider, and calls {@link TileLoaderListener#tileLoaded(Coordinate, Object)}
	 * afterwards.
	 */
	public void run() {

		// Gets tile as image directly from provider (e.g. loaded from a database)
		PImage tileImg = provider.getTile(coordinate);

		if (tileImg == null) {
			// Loads images via the tile URLs from provider (e.g. from a web map service)
			String[] urls = provider.getTileUrls(coordinate);
			if (urls != null) {
				tileImg = getTileFromUrl(urls);
			}
		}

		if (tileImg == null) {
			// If no tile was provided, use transparent image.
			tileImg = cachedEmpyImage;
		}

		if (showDebugBorder || showTileCoordinates) {
			// Shows debug information atop original tile image.
			tileImg = DebugTileUtils.getDebugTile(coordinate, tileImg, p, showDebugBorder, showTileCoordinates);
		}

		listener.tileLoaded(coordinate, tileImg);
	}

	/**
	 * Loads tile from URL(s) by using Processing's loadImage function. If multiple URLs are
	 * provided, all tile images are blended into each other.
	 * 
	 * @param urls
	 *            The URLs (local or remote) to load the tiles from.
	 * @return The tile image.
	 */
	protected PImage getTileFromUrl(String[] urls) {
		// Load image from URL (local file included)
		// NB: Use 'unknown' as content-type to let loadImage decide
		PImage img = p.loadImage(urls[0], "unknown");
		//PImage img = p.loadImage(urls[0]); // test for Android

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

	public void showDebugBorder() {
		this.showDebugBorder = true;
	}

	public void showTileCoordinates() {
		this.showTileCoordinates = true;
	}

}
