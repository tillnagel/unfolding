package de.fhpotsdam.unfolding.examples.overviewdetail.connection;

import processing.core.PApplet;
import processing.core.PVector;

public class ConvexHullConnection extends ConvexHull implements OverviewPlusDetailConnection {
	
	protected float padding = 0;
	
	// Detail
	protected PVector detailPosition = new PVector();
	protected float detailWidth;
	protected float detailHeight;
	
	// Corners of the detail rectangle
	protected PVector detailtl = new PVector();
	protected PVector detailtr = new PVector();
	protected PVector detailbl = new PVector();
	protected PVector detailbr = new PVector();


	// Overview
	protected PVector overviewPosition = new PVector();
	protected float overviewWidth;
	protected float overviewHeight;

	// Corners of the overview rectangle
	protected PVector overviewtl = new PVector();
	protected PVector overviewtr = new PVector();
	protected PVector overviewbl = new PVector();
	protected PVector overviewbr = new PVector();

	
	public ConvexHullConnection(PApplet p) {
		super(p);
		
		points.add(detailtl);
		points.add(detailtr);
		points.add(detailbl);
		points.add(detailbr);

		points.add(overviewtl);
		points.add(overviewtr);
		points.add(overviewbl);
		points.add(overviewbr);
	}
	
	@Override
	public void draw() {
		super.draw();
		
		p.noFill();
		p.stroke(50, 200);
		p.strokeWeight(1);
		p.rect(overviewtl.x, overviewtl.y, overviewWidth, overviewHeight);
	}


	@Override
	public void setDetailSize(float width, float height) {
		this.detailWidth = width;
		this.detailHeight = height;
	}
	
	@Override
	public void setOverviewSize(float width, float height) {
		this.overviewWidth = width;
		this.overviewHeight = height;
	}

	@Override
	public void setPadding(float padding) {
		this.padding = padding;
	}

	@Override
	public void setDetailPosition(float x, float y) {
		detailPosition.x = x;
		detailPosition.y = y;

		detailtl.x = detailPosition.x - padding;
		detailtl.y = detailPosition.y - padding;

		detailtr.x = detailPosition.x + detailWidth + padding;
		detailtr.y = detailPosition.y - padding;

		detailbl.x = detailPosition.x - padding;
		detailbl.y = detailPosition.y + detailHeight + padding;

		detailbr.x = detailPosition.x + detailWidth + padding;
		detailbr.y = detailPosition.y + detailHeight + padding;
	}

	@Override
	public void setOverviewPosition(float x, float y) {
		overviewPosition.x = x;
		overviewPosition.y = y;
		
		overviewtl.x = overviewPosition.x - padding;
		overviewtl.y = overviewPosition.y - padding;

		overviewtr.x = overviewPosition.x + overviewWidth + padding;
		overviewtr.y = overviewPosition.y - padding;

		overviewbl.x = overviewPosition.x - padding;
		overviewbl.y = overviewPosition.y + overviewHeight + padding;

		overviewbr.x = overviewPosition.x + overviewWidth + padding;
		overviewbr.y = overviewPosition.y + overviewHeight + padding;
	}

}
