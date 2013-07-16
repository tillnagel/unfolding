import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import de.fhpotsdam.unfolding.UnfoldingMap;

/**
 * Stores a circular thumbnail of the current map, and current map status data (from MapSnapshot).  
 */
public class CircularMapSnapshot extends MapSnapshot {

	public CircularMapSnapshot(PApplet p, UnfoldingMap map) {
		super(p, map);
	}

	@Override
	public void snapshot(UnfoldingMap map) {
		super.snapshot(map);

		thumbnail = getCircularImage(thumbnail, 200, 5);
	}

	// By amnon.owed, http://forum.processing.org/topic/extract-circle-texture-from-background-with-alpha-channel
	public PImage getCircularImage(PImage img, int radius, int feather) {
		PGraphics temp = p.createGraphics(img.width, img.height, PConstants.JAVA2D);
		temp.beginDraw();
		temp.smooth();
		temp.translate(temp.width / 2, temp.height / 2);
		temp.imageMode(PConstants.CENTER);
		temp.image(img, 0, 0);
		temp.endDraw();
		PImage saveArea = p.createImage(temp.width, temp.height, PConstants.ARGB);
		for (int y = 0; y < saveArea.height; y++) {
			for (int x = 0; x < saveArea.width; x++) {
				int index = y + x * saveArea.width;
				float d = PApplet.dist(x, y, radius, radius);
				if (d > radius) {
					saveArea.pixels[index] = 0;
				} else if (d >= radius - feather) {
					int c = temp.pixels[index];
					int r = (c >> 16) & 0xff;
					int g = (c >> 8) & 0xff;
					int b = (c) & 0xff;
					c = p.color(r, g, b, PApplet.map(d, radius - feather, radius, 255, 0));
					saveArea.pixels[index] = c;
				} else {
					saveArea.pixels[index] = temp.pixels[index];
				}
			}
		}
		saveArea.updatePixels();
		return saveArea;
	}

}
