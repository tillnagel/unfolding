package de.fhpotsdam.unfolding.examples.threed;

import javax.media.opengl.GL2;

import de.fhpotsdam.unfolding.UnfoldingMap;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.opengl.PGraphicsOpenGL;

public class OpenGL2TestApp extends PApplet {

	float a;
	GL2 gl;
	PGraphicsOpenGL pgl;
	float[] projMatrix;
	float[] mvMatrix;
	
	UnfoldingMap map;

	public void setup() {
		size(800, 600, OPENGL);
		pgl = (PGraphicsOpenGL) g; // g may change
		gl = pgl.beginPGL().gl.getGL2();
		projMatrix = new float[16];
		mvMatrix = new float[16];
		
		map = new UnfoldingMap(this);
	}

	public void draw() {
		background(255);
		loadMatrix();
		pgl.beginPGL();
		
		
		// Do some things with gl.xxx functions here.
		// For example, the program above is translated into:
		gl.glColor4f(0.7f, 0.7f, 0.7f, 0.8f);
		gl.glTranslatef(width / 2, height / 2, 0);
		gl.glRotatef(a, 1, 0, 0);
		gl.glRotatef(a * 2, 0, 1, 0);
		
		// Use map as texture in GL2
		//PGraphics pg = map.mapDisplay.getOuterPG();
		

		gl.glRectf(-200, -200, 200, 200);
		gl.glRotatef(90, 1, 0, 0);
		gl.glRectf(-200, -200, 200, 200);

		pgl.endPGL();

		a += 0.5;
	}

	public void loadMatrix() {

		gl.glMatrixMode(GL2.GL_PROJECTION);
		projMatrix[0] = pgl.projection.m00;
		projMatrix[1] = pgl.projection.m10;
		projMatrix[2] = pgl.projection.m20;
		projMatrix[3] = pgl.projection.m30;

		projMatrix[4] = pgl.projection.m01;
		projMatrix[5] = pgl.projection.m11;
		projMatrix[6] = pgl.projection.m21;
		projMatrix[7] = pgl.projection.m31;

		projMatrix[8] = pgl.projection.m02;
		projMatrix[9] = pgl.projection.m12;
		projMatrix[10] = pgl.projection.m22;
		projMatrix[11] = pgl.projection.m32;

		projMatrix[12] = pgl.projection.m03;
		projMatrix[13] = pgl.projection.m13;
		projMatrix[14] = pgl.projection.m23;
		projMatrix[15] = pgl.projection.m33;

		gl.glLoadMatrixf(projMatrix, 0);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		mvMatrix[0] = pgl.modelview.m00;
		mvMatrix[1] = pgl.modelview.m10;
		mvMatrix[2] = pgl.modelview.m20;
		mvMatrix[3] = pgl.modelview.m30;

		mvMatrix[4] = pgl.modelview.m01;
		mvMatrix[5] = pgl.modelview.m11;
		mvMatrix[6] = pgl.modelview.m21;
		mvMatrix[7] = pgl.modelview.m31;

		mvMatrix[8] = pgl.modelview.m02;
		mvMatrix[9] = pgl.modelview.m12;
		mvMatrix[10] = pgl.modelview.m22;
		mvMatrix[11] = pgl.modelview.m32;

		mvMatrix[12] = pgl.modelview.m03;
		mvMatrix[13] = pgl.modelview.m13;
		mvMatrix[14] = pgl.modelview.m23;
		mvMatrix[15] = pgl.modelview.m33;
		gl.glLoadMatrixf(mvMatrix, 0);
	}

}
