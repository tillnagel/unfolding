import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;

/**
 * Simple demo app.
 * Used in the unfolding_app_template.
 */
public class HelloUnfoldingWorld extends PApplet {

	UnfoldingMap map;

	@Override
	public void setup() {
		size(600, 600);

		map = new UnfoldingMap(this);
	}

	@Override
	public void draw() {
		background(255);

		map.draw();

		ellipse(mouseX, mouseY, 20, 20);
	}

}
