import processing.core.PApplet;
import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class SimpleMapApp extends PApplet {

	UnfoldingMap map;

	@Override
	public void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);

		MapUtils.createDefaultEventDispatcher(this, map);
	}

	@Override
	public void draw() {
		background(0);
		map.draw();
	}

}
