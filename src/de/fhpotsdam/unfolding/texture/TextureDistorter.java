package de.fhpotsdam.unfolding.texture;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

public class TextureDistorter {

	public boolean mouse3DRotate = false;
	public boolean osc3DRotate = false;
	public float lightX;
	public float lightY;

	PShape meshModel;

	// Mesh parameters
	protected int meshWidth;
	protected int meshHeight;
	protected int meshStep;

	public Distorter distorter;

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

		meshModel = null;
	}

	public TextureDistorter(PApplet papplet, float width, float height, int meshStep) {
		this(papplet, (int) width, (int) height, meshStep);
	}

	public void setDistorter(Distorter distorter) {
		this.distorter = distorter;
	}

	int frameCount = 0;

	public void draw(PGraphics g, PImage texture) {
		frameCount++;
		
		// REVISIT
		// Distort by texture to extrude by pixel brightness
		distortGridByTexture(texture);

		// distortGrid();

		distortMesh();

		// createMesh(distortedGrid);
		
		if (meshModel == null) {
		  // create
		  meshModel = g.createShape();
		  meshModel.beginShape(PConstants.TRIANGLE_STRIP);
		  meshModel.texture(texture);
		  for (int i = 0; i < vertices.size(); i++) {
		    PVector vert = vertices.get(i);		    
		    PVector tcoord = texCoords.get(i);
		    if (g.is3D()) {
		      PVector norm = normals.get(i);
		      meshModel.normal(norm.x, norm.y, norm.z);
		      meshModel.vertex(vert.x, vert.y, vert.z, tcoord.x, tcoord.y);
		    } else {
		      meshModel.vertex(vert.x, vert.y, tcoord.x, tcoord.y);
		    }		    
      }
		  meshModel.endShape();		  
		} else {
		  // update using setter methods
		  meshModel.setTexture(texture);
		  for (int i = 0; i < vertices.size(); i++) {
        PVector vert = vertices.get(i);       
        PVector tcoord = texCoords.get(i);
        if (g.is3D()) {
          PVector norm = normals.get(i);
          meshModel.setNormal(i, norm.x, norm.y, norm.z);
          meshModel.setVertex(i, vert.x, vert.y, vert.z);
        } else {
          meshModel.setVertex(i, vert.x, vert.y);          
        }       		    
        meshModel.setTextureUV(i, tcoord.x, tcoord.y);
		  }
		}
		
		g.background(0);

		if (mouse3DRotate || osc3DRotate) {
			PApplet p = PAppletFactory.getInstance();

			if (showLight) {
				// Simple 3D lighting
				g.directionalLight(204, 204, 204, lightX, lightY, -1);
			}

			g.translate(400, 300);

			if (mouse3DRotate) {
				float rotX = (p.mouseX / (float) p.width - 0.5f) * 2f * PApplet.PI;
				float rotZ = (p.mouseY / (float) p.height - 0.5f) * 2f * PApplet.PI;
				g.rotateX(rotX);
				g.rotateZ(rotZ);
			} else if (osc3DRotate) {
				g.rotateX(rotX);
				g.rotateY(rotY);
				g.rotateZ(rotZ);
			}

			g.translate(-400, -300);
		}

		g.shape(meshModel);
	}

	public float rotX, rotY, rotZ;
	public boolean showLight;

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

	protected void distortGridByTexture(PImage texture) {
		texture.loadPixels();
		for (int u = 0; u < uSteps; u++) {
			for (int v = 0; v < vSteps; v++) {
				int x = u * meshStep;
				int y = v * meshStep;
				int col = texture.get(x, y);
				// PApplet.println(x + "," + y + ": " + col);
				distorter.distort(origGrid[u][v], distortedGrid[u][v], col);
			}
		}
	}

	protected void distortGrid() {
		for (int u = 0; u < uSteps; u++) {
			for (int v = 0; v < vSteps; v++) {
				// distortedGrid[u][v] = distorter.distort(origGrid[u][v]);

				// FIXME Test distorting a single grid vector
				int value = (u > 30 && u < 40 && v == 21) ? 1 : 0;
				distorter.distort(origGrid[u][v], distortedGrid[u][v], value);
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
