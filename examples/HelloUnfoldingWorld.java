import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Hello Unfolding World.
 * 
 * Download the distribution with examples for many more examples and features.
 * <p>
 * Works correctly under Processing 2.2.1. If you want to use Processing 3.1.2 or later version 
 * you should see {@link de.fhpotsdam.unfolding.examples.SimpleMapApp}
 */
public class HelloUnfoldingWorld extends PApplet {

	UnfoldingMap map;

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(10, new Location(52.5f, 13.4f));

		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);
		map.draw();
	}

}
