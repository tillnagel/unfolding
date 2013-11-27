package de.fhpotsdam.unfolding.examples.ui;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;

public class ZoomSlider {

	PApplet p;
	UnfoldingMap map;

	float x = 50;
	float y = 30;
	float w = 100;
	float h = 10;

	float sliderX;
	float sliderW = 10;
	boolean dragging = false;

	ZoomSlider(PApplet p, UnfoldingMap map, float x, float y) {
		this.p = p;
		this.map = map;

		this.x = x;
		this.y = y;
		sliderX = x;
	}

	public void draw() {
		p.fill(200);
		p.stroke(180);
		p.rect(x, y, w, h);

		p.fill(180);
		p.rect(sliderX, y, sliderW, 10);
	}

	public void setZoomLevel(int zoomLevel) {
		sliderX = PApplet.map(zoomLevel, 1, 16, x, x + w);
	}

	public boolean isDragging() {
		return dragging;
	}

	public boolean contains(float checkX, float checkY) {
		return (checkX > x && checkX < x + w && checkY > y && checkY < y + h);
	}

	public void startDrag(float dragX, float dragY) {
		sliderX = PApplet.constrain(dragX - sliderW / 2, x, x + w - sliderW);
		dragging = true;
	}

	public void drag(float dragX, float dragY) {
		sliderX = PApplet.constrain(dragX - sliderW / 2, x, x + w - sliderW);
		updateMap();
	}

	public void endDrag() {
		dragging = false;
		updateMap();
	}
	
	protected void updateMap() {
		int level = (int) PApplet.map(sliderX, x, x + w, 1, 16);
		map.zoomToLevel(level);
	}
}