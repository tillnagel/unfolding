package de.fhpotsdam.unfolding.examples.marker.multimarker;

import java.util.List;

import codeanticode.glgraphics.GLGraphicsOffScreen;

import processing.core.PConstants;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.marker.AbstractMultiMarker;

public class PolyMarker extends AbstractMultiMarker{

	@Override
	public void draw(PGraphics pg, List<ObjectPosition> objectPositions) {
		pg.pushStyle();
		pg.fill(100,90,240,100);
		pg.stroke(50,50,50,200);
		pg.beginShape();
		for(ObjectPosition op : objectPositions){
			pg.vertex(op.x, op.y);
		}
		pg.endShape(PConstants.CLOSE);
		pg.popStyle();
	}

	@Override
	public void drawOuter(PGraphics pg, List<ObjectPosition> objectPositions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isInside(float checkX, float checkY, float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

}
