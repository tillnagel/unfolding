package de.fhpotsdam.unfolding.texture;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import codeanticode.glgraphics.GLGraphics;
import codeanticode.glgraphics.GLModel;
import codeanticode.glgraphics.GLTexture;

public class TextureDistorter {

	boolean rotateIn3D = true;
	
	GLModel meshModel;

	// Mesh parameters
	protected int meshWidth;
	protected int meshHeight;
	protected int meshStep;

	protected Distorter distorter;

	// 2D grid (for 2D mesh), but each point can be 3D
	protected int uSteps;
	protected int vSteps;
	protected PVector[][] origGrid;
	protected PVector[][] distortedGrid;

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
		createMesh(distortedGrid);

		meshModel = new GLModel(p, vertices.size(), PConstants.TRIANGLE_STRIP, GLModel.DYNAMIC);
	}

	public TextureDistorter(PApplet papplet, float width, float height, int meshStep) {
		this(papplet, (int) width, (int) height, meshStep);
	}

	public void setDistorter(Distorter distorter) {
		this.distorter = distorter;
	}

	int frameCount = 0;

	public void draw(PGraphics g, GLTexture texture) {
		frameCount++;

		//distortGridByTexture(texture);
		distortGrid();
		distortMesh();

		// createMesh(distortedGrid);

		GLGraphics renderer = (GLGraphics) g;
		renderer.beginGL();
		renderer.background(0);
		meshModel.updateVertices(vertices);
		meshModel.initTextures(1);
		meshModel.setTexture(0, texture);
		meshModel.updateTexCoords(0, texCoords);
		meshModel.initNormals();
		meshModel.updateNormals(normals);

		if (rotateIn3D) {
			// Simple 3D lighting
			// PApplet p = PAppletFactory.getInstance();
			// float dirX = (p.mouseX / (float) p.width - 0.5f) * 2f;
			// float dirY = (p.mouseY / (float) p.height - 0.5f) * 2f;
			float dirX = -1f;
			float dirY = -1f;
			renderer.directionalLight(204, 204, 204, -dirX, -dirY, -1);

			renderer.translate(400, 300, 0);
			// renderer.rotateX(frameCount * 0.04f);
			PApplet p = PAppletFactory.getInstance();
			float rotX = (p.mouseX / (float) p.width - 0.5f) * 2f * PApplet.PI;
			float rotZ = (p.mouseY / (float) p.height - 0.5f) * 2f * PApplet.PI;
			// renderer.rotateX(rotX);
			// renderer.rotateZ(rotZ);

			renderer.rotateX(PApplet.PI / 8);
			renderer.translate(-400, -300, 0);
		}
		
		renderer.model(meshModel);

		renderer.endGL();
	}

	protected void initGrids() {
		origGrid = new PVector[uSteps][vSteps];
		distortedGrid = new PVector[uSteps][vSteps];

		for (int u = 0; u < uSteps; u++) {
			for (int v = 0; v < vSteps; v++) {
				origGrid[u][v] = new PVector(u * meshStep, v * meshStep, 0);
				distortedGrid[u][v] = new PVector(0, 0, 0);
			}
		}
	}

	protected void distortGridByTexture(GLTexture texture) {
		for (int u = 0; u < uSteps; u++) {
			for (int v = 0; v < vSteps; v++) {
				int x = u * meshStep;
				int y = v * meshStep;
				int col = texture.get(x, y);
				PApplet.println(x + "," + y + ": " + col);
				distorter.distort(origGrid[u][v], distortedGrid[u][v], col);
			}
		}
	}

	protected void distortGrid() {
		for (int u = 0; u < uSteps; u++) {
			for (int v = 0; v < vSteps; v++) {
				// distortedGrid[u][v] = distorter.distort(origGrid[u][v]);
				distorter.distort(origGrid[u][v], distortedGrid[u][v]);
			}
		}
	}

	protected void createMesh(PVector[][] grid) {
		vertices = new ArrayList<PVector>();
		texCoords = new ArrayList<PVector>();
		normals = new ArrayList<PVector>();

		// create all mesh vectors (triangles)
		for (int v = 0; v < vSteps - 1; v++) {
			int lastU = 0;
			for (int u = 0; u < uSteps; u++) {
				addVertex(grid[u][v].x, grid[u][v].y, grid[u][v].z, u, v);
				addVertex(grid[u][v + 1].x, grid[u][v + 1].y, grid[u][v + 1].z, u, v + 1);
				lastU = u;
			}
			// Adds degenerate triangle to jump back to left side of grid
			addVertex(grid[lastU][v + 1].x, grid[lastU][v + 1].y, grid[lastU][v + 1].z, lastU, v + 1);
			addVertex(grid[0][v + 1].x, grid[0][v + 1].y, grid[0][v + 1].z, 0, v + 1);
		}
	}

	int index = 0;

	protected void distortMesh() {
		// Uses global index to access vertices in global lists
		index = 0;

		for (int v = 0; v < vSteps - 1; v++) {
			int lastU = 0;
			for (int u = 0; u < uSteps; u++) {
				distortVertex(distortedGrid[u][v].x, distortedGrid[u][v].y, distortedGrid[u][v].z, u, v);
				distortVertex(distortedGrid[u][v + 1].x, distortedGrid[u][v + 1].y, distortedGrid[u][v + 1].z, u, v + 1);
				lastU = u;
			}
			// Adds degenerate triangle to jump back to left side of grid
			distortVertex(distortedGrid[lastU][v + 1].x, distortedGrid[lastU][v + 1].y, distortedGrid[lastU][v + 1].z,
					lastU, v + 1);
			distortVertex(distortedGrid[0][v + 1].x, distortedGrid[0][v + 1].y, distortedGrid[0][v + 1].z, 0, v + 1);
		}
	}

	protected void distortVertex(float x, float y, float z, float u, float v) {
		PVector vert = vertices.get(index);
		vert.set(x, y, z);

		// texCoord stays the same

		PVector vertNorm = normals.get(index);
		PVector.div(vert, vert.mag(), vertNorm);

		index++;
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

	// protected void createMesh(PVector[][] grid) {
	// vertices = new ArrayList<PVector>();
	// texCoords = new ArrayList<PVector>();
	// normals = new ArrayList<PVector>();
	//
	// for (int v = 0; v < vSteps - 1; v++) {
	// int lastU = 0;
	// for (int u = 0; u < uSteps; u++) {
	// addVertex(grid[u][v].x, grid[u][v].y, grid[u][v].z, u, v);
	// addVertex(grid[u][v + 1].x, grid[u][v + 1].y, grid[u][v + 1].z, u, v + 1);
	// lastU = u;
	// }
	// // Adds degenerate triangle to jump back to left side of grid
	// addVertex(grid[lastU][v + 1].x, grid[lastU][v + 1].y, grid[lastU][v + 1].z, lastU, v + 1);
	// addVertex(grid[0][v + 1].x, grid[0][v + 1].y, grid[0][v + 1].z, 0, v + 1);
	// }
	// }
	//
	// /**
	// * Adds vertex, texture coordinates, and normals.
	// */
	// protected void addVertex(float x, float y, float z, float u, float v) {
	// PVector vert = new PVector(x, y, z);
	// PVector texCoord = new PVector(u / (uSteps - 1), v / (vSteps - 1));
	// PVector vertNorm = PVector.div(vert, vert.mag());
	// vertices.add(vert);
	// texCoords.add(texCoord);
	// normals.add(vertNorm);
	// }

}
