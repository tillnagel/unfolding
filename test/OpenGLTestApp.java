import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;

public class OpenGLTestApp extends PApplet {

	public void setup() {
		size(800, 600, OPENGL);

	}

	public void draw() {
		ellipse(random(width), random(height), 20, 20);
	}

	@Override
	public void destroy() {
		// Overwrites p.destroy() and thus, p.dispose() is not called, and thus, g.dispose() is not called.
		println("destroy");
		((PGraphicsOpenGL) g).dispose();
	}

}
