package de.fhpotsdam.unfolding.examples.data.vectortiles;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.mapdisplay.OpenGLMapDisplay;
import de.fhpotsdam.unfolding.mapdisplay.shaders.MapDisplayShader;
import de.fhpotsdam.unfolding.mapdisplay.shaders.MaskedMapDisplayShader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePolygonMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;

import static de.fhpotsdam.unfolding.examples.data.vectortiles.SelectBuildingsApp.FEATURE_LAYER;

/**
 * Shows a mask for the map with buildings drawn behind the mask. Click to select a building which will be drawn in
 * green and before the mask.
 */
public class SelectedBuildingsMaskApp extends PApplet {

    private Marker selectedMarker = null;
    private UnfoldingMap map;
    private MapDisplayShader mapDisplayShader;
    private VectorTilesUtils vectorTilesUtils;

    @Override
    public void settings() {
        size(800, 600, P2D);
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{SelectedBuildingsMaskApp.class.getName()});
    }

    @Override
    public void setup() {
        map = new UnfoldingMap(this);
        MapUtils.createDefaultEventDispatcher(this, map);
        map.zoomAndPanTo(16, new Location(52.501, 13.395));
        map.setZoomRange(10, 19);

        vectorTilesUtils = new VectorTilesUtils(this, map);
        final List<Marker> markers = vectorTilesUtils.loadMarkersForScreenPos(FEATURE_LAYER, width / 2, height / 2);
        map.addMarkers(markers);

        final PImage maskImage = loadImage("test/mask-circular.png");
        mapDisplayShader = new MaskedMapDisplayShader(this, 400, 400, maskImage);
        ((OpenGLMapDisplay) map.mapDisplay).setMapDisplayShader(mapDisplayShader);
    }

    @Override
    public void draw() {
        background(0);
        map.draw();
        g.resetShader();

        if (selectedMarker != null) {
            final SimplePolygonMarker polygonMarker = (SimplePolygonMarker) selectedMarker;
            strokeWeight(1);
            stroke(28, 102, 120);
            fill(116, 188, 157);
            beginShape();
            for (final Location location : polygonMarker.getLocations()) {
                final ScreenPosition pos = map.getScreenPosition(location);
                vertex(pos.x, pos.y);
            }
            endShape();
        }
    }

    @Override
    public void keyPressed() {
        if (key == 'l') {
            final List<Marker> markers = vectorTilesUtils.loadMarkersForScreenPos(FEATURE_LAYER, mouseX, mouseY);
            map.addMarkers(markers);
        }
    }

    @Override
    public void mouseClicked() {
        final List<Marker> markers = map.getMarkers();
        for (final Marker marker : markers)
            marker.setHidden(true);

        selectedMarker = map.getFirstHitMarker(mouseX, mouseY);
    }

}
