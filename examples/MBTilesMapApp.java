import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Simple demo app.
 * Used in the unfolding_app_template.
 */
public class MBTilesMapApp extends PApplet {

	// Connection to SQLite/MBTiles in development environment (link to the
	// project)
	public static final String JDBC_CONN_STRING = "jdbc:sqlite:../data/muse-dark-2-8.mbtiles";

	UnfoldingMap map;

	@Override
	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new UnfoldingMap(this, 0, 0, width, height,
				new MBTilesMapProvider(JDBC_CONN_STRING));
		MapUtils.createDefaultEventDispatcher(this, map);
		map.setZoomRange(2, 9);
	}

	@Override
	public void draw() {
		background(0);

		map.draw();
	}

}
