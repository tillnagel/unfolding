package de.fhpotsdam.unfolding.texture;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;
import codeanticode.glgraphics.GLGraphics;
import codeanticode.glgraphics.GLModel;
import codeanticode.glgraphics.GLTexture;

public class TextureDistorter {

	GLModel meshModel;

	// Mesh parameters
	protected int meshWidth;
	protected int meshHeight;
	protected int meshStep;

	protected Distorter distorter;

	protected int uSteps;
	protected int vSteps;
	protected PVector[][] origGrid;
	protected PVector[][] zoomGrid;

	protected ArrayList<PVector> vertices;
	protected ArrayList<PVector> texCoords;
	protected ArrayList<PVector> normals;

	public TextureDistorter(PApplet p, int meshWidth, int meshHeight, int meshStep) {
		this.meshWidth = meshWidth;
		this.meshHeight = meshHeight;
		this.meshStep = meshStep;

		this.uSteps = meshWidth / meshStep + 1;
		this.vSteps = meshHeight / meshStep + 1;

		initGrids();
		createMesh(zoomGrid);

		meshModel = new GLModel(p, vertices.size(), PConstants.TRIANGLE_STRIP, GLModel.DYNAMIC);
	}

	public void setDistorter(Distorter distorter) {
		this.distorter = distorter;
	}

	public void draw(PGraphics g, GLTexture texture) {
		distortZoomGrid();
		createMesh(zoomGrid);

		GLGraphics renderer = (GLGraphics) g;
		renderer.beginGL();
		meshModel.updateVertices(vertices);
		meshModel.initTexures(1);
		meshModel.setTexture(0, texture);
		meshModel.updateTexCoords(0, texCoords);
		meshModel.initNormals();
		meshModel.updateNormals(normals);
		renderer.model(meshModel);
		renderer.endGL();
	}

	protected void initGrids() {
		origGrid = new PVector[uSteps][vSteps];
		zoomGrid = new PVector[uSteps][vSteps];

		for (int u = 0; u < uSteps; u++) {
			for (int v = 0; v < vSteps; v++) {
				origGrid[u][v] = new PVector(u * meshStep, v * meshStep, 0);
				zoomGrid[u][v] = new PVector(0, 0, 0);
			}
		}
	}

	protected void distortZoomGrid() {
		for (int u = 0; u < uSteps; u++) {
			for (int v = 0; v < vSteps; v++) {
				zoomGrid[u][v] = distorter.distort(origGrid[u][v]);
			}
		}
	}

	protected void createMesh(PVector[][] grid) {
		vertices = new ArrayList<PVector>();
		texCoords = new ArrayList<PVector>();
		normals = new ArrayList<PVector>();

		for (int v = 0; v < vSteps - 1; v++) {
			int lastU = 0;
			for (int u = 0; u < uSteps; u++) {
				addVertex(grid[u][v].x, grid[u][v].y, 0, u, v);
				addVertex(grid[u][v + 1].x, grid[u][v + 1].y, 0, u, v + 1);
				lastU = u;
			}
			// Adds degenerate triangle to jump back to left side of grid
			addVertex(grid[lastU][v + 1].x, grid[lastU][v + 1].y, 0, lastU, v + 1);
			addVertex(grid[0][v + 1].x, grid[0][v + 1].y, 0, 0, v + 1);
		}
	}

	/**
	 * Adds vertex, texture coordinates, and normals.
	 */
	protected void addVertex(float x, float y, float z, float u, float v) {
		PVector vert = new PVector(x, y, z);
		PVector texCoord = new PVector(u / (uSteps - 1), v / (vSteps - 1));
		PVector vertNorm = PVector.div(vert, vert.mag());
		vertices.add(vert);
		texCoords.add(texCoord);
		normals.add(vertNorm);
	}

}
