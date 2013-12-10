//package de.fhpotsdam.unfolding.performance;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import processing.core.PApplet;
//import processing.core.PVector;
//
//
//
//import de.fhpotsdam.unfolding.UnfoldingMap;
//import de.fhpotsdam.unfolding.events.MapEvent;
//import de.fhpotsdam.unfolding.geo.Location;
//import de.fhpotsdam.unfolding.utils.MapUtils;
//
///**
// * Displays a million markers on the map.
// * 
// * (c) 2012 Till Nagel, unfoldingmaps.org
// * 
// * Used for various performance tests.
// * <ul>
// * <li>pure drawing (fps for 10k, 100k, 1000k.)</li>
// * <li>pure drawing markers (ditto)</li>
// * <li>filter cut-off on map border</li>
// * <li>filtering only on map change</li>
// * <li>calculating ScreenPosition only on map change</li>
// * <li>Different visual representations (e.g. rect vs ellipse</li>
// * <li>Use GLModel (see ltavis)</li>
// * <li>...</li>
// * </ul>
// * 
// * Outcomes - rect is faster than ellipse (20k. rect: 24fps, ellipse: 7fps)
// * 
// */
//public class MillionDotsMapApp7GLModel2 extends PApplet {
//
//	int dotNumber = 500000;
//
//	UnfoldingMap map;
//	// Original dots (loc + time)
//	List<Dot> dots = new ArrayList<Dot>();
//
//	// Visible points
//	List<PVector> visibleDotVertices = new ArrayList<PVector>();
//	// GLGraphics model containing vertices
//	GLModel model;
//	// Indicates whether model was updated and should be drawn next frame (to circumvent error on updating outside
//	// draw())
//	boolean updateModelOnNextDraw = false;
//
//	Location tlLoc;
//	Location brLoc;
//
//	public void setup() {
//		size(800, 600, OPENGL);
//		smooth();
//
//		dots = createRandomDots(dotNumber);
//
//		map = new UnfoldingMap(this);
//		map.zoomToLevel(3);
//		MapUtils.createDefaultEventDispatcher(this, map);
//
//		model = new GLModel(this, dotNumber * 4, GLModel.QUADS, GLModel.STATIC);
//		mapChanged(null);
//	}
//
//	public void draw() {
//		background(0);
//		map.draw();
//
//		synchronized (visibleDotVertices) {
//			if (updateModelOnNextDraw) {
//				updateModelVertices();
//				updateModelOnNextDraw = false;
//			}
//			drawModel();
//		}
//
//
//		fill(255);
//		rect(5, 5, 180, 20);
//		fill(0);
//		text("fps: " + nfs(frameRate, 0, 2) + " (" + visibleDotVertices.size() + " dots)", 10, 20);
//	}
//
//	public void drawModel() {
//		GLGraphics renderer = (GLGraphics) g;
//		renderer.beginGL();
//
//		model.setTint(0, 100);
//
//		// renderer.model(model);
//		// Using render(int, int, GLEffect) method, due to bug (see
//		// https://forum.processing.org/topic/getting-extra-vertices-when-building-a-mesh-with-glmodel#25080000001046326)
//		int verticesPerSegment = 4;
//		int numVertices = visibleDotVertices.size() * verticesPerSegment;
//		model.render(0, numVertices, null);
//		renderer.endGL();
//	}
//
//	public void updateModelVertices() {
//		int verticesPerSegment = 4;
//		int numVertices = visibleDotVertices.size() * verticesPerSegment;
//
//		if (model.getSize() != numVertices) {
//			// If number of visible dots has changed, create new model with new number of vertices.
//			model = new GLModel(this, visibleDotVertices.size() * 4, GLModel.QUADS, GLModel.STATIC);
//		}
//
//		model.beginUpdateVertices();
//		synchronized (visibleDotVertices) {
//			for (int i = 0; i < numVertices; i += verticesPerSegment) {
//				PVector pos = visibleDotVertices.get(i / verticesPerSegment);
//				model.updateVertex(i, pos.x + 4, pos.y + 4);
//				model.updateVertex(i + 1, pos.x - 4, pos.y + 4);
//				model.updateVertex(i + 2, pos.x - 4, pos.y - 4);
//				model.updateVertex(i + 3, pos.x + 4, pos.y - 4);
//			}
//		}
//		model.endUpdateVertices();
//	}
//
//	public void mapChanged(MapEvent mapEvent) {
//		// Check map area only once after user interaction.
//		// Additionally, instead of calculating the screen position each frame, store it in new list.
//		brLoc = map.getBottomRightBorder();
//		tlLoc = map.getTopLeftBorder();
//		synchronized (visibleDotVertices) {
//			visibleDotVertices.clear();
//			for (Dot dot : dots) {
//				if (dot.location.getLat() > brLoc.getLat() && dot.location.getLat() < tlLoc.getLat()
//						&& dot.location.getLon() > tlLoc.getLon() && dot.location.getLon() < brLoc.getLon()) {
//					PVector pos = map.getScreenPosition(dot.location);
//					visibleDotVertices.add(pos);
//				}
//			}
//		}
//
//		updateModelOnNextDraw = true;
//	}
//
//	private List<Dot> createRandomDots(int dotNumbers) {
//		List<Dot> dots = new ArrayList<Dot>();
//		for (int i = 0; i < dotNumbers; i++) {
//			Dot dot = new Dot(new Location(random(-85, 85), random(-180, 180)), new Date());
//			dots.add(dot);
//		}
//		return dots;
//	}
//}
