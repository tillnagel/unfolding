import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Hello Unfolding World.
 * 
 * Download the distribution with examples for many more examples and features.
 */
public class HelloUnfoldingWorld extends PApplet {

	UnfoldingMap map;

	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new UnfoldingMap(this, new OpenStreetMap.OpenStreetMapProvider());
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);

		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		map.draw();
	}

}
