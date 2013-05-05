package de.fhpotsdam.unfolding.examples.threed;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.gl2.GLUgl2;

import processing.core.PApplet;
import processing.core.PVector;
import processing.opengl.PGL;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * An application with a basic interactive map. You can zoom and pan the map.
 * 
 * 
 * Does not work as Processing's internal matrix is not the same as GL2's one.

 */
public class Map3DApp extends PApplet {

	protected UnfoldingMap map;
	protected Location berlinLocation = new Location(52.5, 13.4);
	protected Location hamburgLocation = new Location(53.5505f, 9.993f);

	protected float[] mousePos;
	protected float pmx = 0;
	protected float pmy = 0;

	// protected GL gl;
	// protected GLU glu;

	public void setup() {
		size(1024, 768, OPENGL);

		map = new UnfoldingMap(this);
		map.zoomAndPanTo(berlinLocation, 10);
		// MapUtils.createDefaultEventDispatcher(this, map);

		SimplePointMarker marker = new SimplePointMarker(berlinLocation);
		map.addMarker(marker);

		// List<Feature> countries = GeoJSONReader.loadData(this, "countries.geo.json");
		// List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);
		// map.addMarkers(countryMarkers);

		init3D();
	}

	public void init3D() {
		// gl = (PGraphicsOpenGL) g;
		// glu = ((PGraphicsOpenGL) g).glu;

		// See https://github.com/processing/processing/issues/1461
		addMouseWheelListener(new java.awt.event.MouseWheelListener() {
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
				mouseWheel(evt.getWheelRotation());
			}
		});
	}

	float zAngle = 0;

	public void draw() {
		background(0);

		pushMatrix();
		rotateX(0.7f);
		translate(0, -160, -100);

		// translate(width/2, height/2, 0);
		// rotateZ(zAngle += 0.01f); //
		// translate(-width/2, -height/2, 0);

		map.draw();

		mousePos = getMouse3D();

		fill(255, 0, 0, 100);
		noStroke();
		ScreenPosition hamburgPos = map.getScreenPosition(hamburgLocation);
		translate(hamburgPos.x, hamburgPos.y);
		sphere(20);

		popMatrix();
	}

	public void mouseClicked() {
		if (mouseEvent.getClickCount() == 2) {
			float mx = mousePos[0];
			float my = mousePos[1];
			Location location = map.getLocation(mx, my);
			map.panTo(location);
		}
	}

	public void mousePressed() {
		pmx = mousePos[0];
		pmy = mousePos[1];

		println("mouse: " + pmx + "," + pmy);
	}

	public void mouseDragged() {
		float mx = mousePos[0];
		float my = mousePos[1];
		map.panBy(-(pmx - mx), -(pmy - my));
		pmx = mx;
		pmy = my;
	}

	public void mouseWheel(float delta) {
		println("mouseWheel");
		map.mapDisplay.setInnerTransformationCenter(new PVector(mousePos[0], mousePos[1]));
		if (delta < 0) {
			map.zoomLevelIn();
		} else {
			map.zoomLevelOut();
		}
	}

	public float[] getMouse3D() {

		PGL pgl = g.beginPGL();
		GL2 gl = pgl.gl.getGL2();
		GLU glu = new GLUgl2();
		//GLU glu = pgl.glu;

		int viewport[] = new int[4];
		double[] proj = new double[16];
		double[] model = new double[16];
		gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);

		gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, proj, 0);
		gl.glGetDoublev(GL2.GL_MODELVIEW, model, 0);
		FloatBuffer fb = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		// gl.glReadPixels(mouseX, height - mouseY, 1, 1, GL2.GL_DEPTH_COMPONENT, GL2.GL_FLOAT, fb);
		gl.glReadPixels(mouseX, mouseY, 1, 1, GL2.GL_DEPTH_COMPONENT, GL2.GL_FLOAT, fb);
		fb.rewind();
		double[] mousePosArr = new double[4];
		glu.gluUnProject((double) mouseX, (double) mouseY, (double) fb.get(0), model, 0, proj, 0, viewport, 0,
				mousePosArr, 0);

		// ((PGraphicsOpenGL) g).endGL();
		g.endPGL();

		return new float[] { (float) mousePosArr[0], (float) mousePosArr[1], (float) mousePosArr[2] };
	}

}
