package de.fhpotsdam.unfolding.marker.gllines;

import java.nio.FloatBuffer;
import java.util.List;

import javax.media.opengl.GL;

import processing.core.PGraphics;
import codeanticode.glgraphics.GLGraphicsOffScreen;

import com.sun.opengl.util.BufferUtil;

import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.utils.MapPosition;

public class GLLinesMarker extends AbstractShapeMarker {

	@Override
	public void draw(PGraphics pg, List<MapPosition> mapPositions) {
		GL gl = ((GLGraphicsOffScreen) pg).beginGL();
		int n = mapPositions.size();

		// REVISIT reuse buffers and update if loations.size() changes
		FloatBuffer vertices = BufferUtil.newFloatBuffer(n * 2 * 2);
		FloatBuffer colors = BufferUtil.newFloatBuffer(n * 3 * 2);

		MapPosition last = mapPositions.get(0);
		for (int i = 1; i < mapPositions.size(); ++i) {
			MapPosition op = mapPositions.get(i);
			vertices.put(last.x);
			vertices.put(last.y);
			vertices.put(op.x);
			vertices.put(op.y);
			last = op;
			for (int j = 0; j < 6; ++j) {
				colors.put(0);
			}
		}

		gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
		vertices.rewind();
		gl.glVertexPointer(2, GL.GL_FLOAT, 0, vertices);

		gl.glEnableClientState(GL.GL_COLOR_ARRAY);
		colors.rewind();
		gl.glColorPointer(3, GL.GL_FLOAT, 0, colors);

		gl.glDrawArrays(GL.GL_LINES, 0, n * 2);
		((GLGraphicsOffScreen) pg).endGL();
	}

	@Override
	protected boolean isInside(float checkX, float checkY, float x, float y) {
		return false;
	}

}
