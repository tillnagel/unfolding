package de.fhpotsdam.unfolding.examples.data.vectortiles;

import java.util.ArrayList;
import java.util.List;

import org.gicentre.handy.HandyRenderer;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.MarkerFactory;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.utils.DebugDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;

import static de.fhpotsdam.unfolding.examples.data.vectortiles.SelectBuildingsApp.FEATURE_LAYER;

/**
 * Displays markers of a single vector tile.
 * <p>
 * Click on the map to load all buildings of vector tile for the area.
 */
public class SketchyVectorTilesApp extends PApplet {

    private UnfoldingMap map;
    private DebugDisplay debugDisplay;
    private VectorTilesUtils vectorTilesUtils;
    private List<Feature> features = new ArrayList<Feature>();
    private HandyRenderer handy;

    @Override
    public void settings() {
        size(1920, 1080, JAVA2D);
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{SketchyVectorTilesApp.class.getName()});
    }

    @Override
    public void setup() {
        handy = new HandyRenderer(this);

        map = new UnfoldingMap(this, "myMap");
        map.zoomAndPanTo(16, new Location(52.501, 13.395));
        MapUtils.createDefaultEventDispatcher(this, map);

        // map.setBackgroundColor(bgColor);

        debugDisplay = new DebugDisplay(this, map);

        vectorTilesUtils = new VectorTilesUtils(this, map);
        features = vectorTilesUtils.loadFeaturesForScreenPos(FEATURE_LAYER, width / 2, height / 2);

        map.addMarkers(createSketchMarkers(features));
    }

    private List<Marker> createSketchMarkers(List<Feature> features) {
        final MarkerFactory markerFactory = new MarkerFactory();
        markerFactory.setPolygonClass(SketchyPolygonMarker.class);
        final List<Marker> markers = markerFactory.createMarkers(features);
        for (final Marker m : markers) {
            if (m instanceof SketchyPolygonMarker) {
                ((SketchyPolygonMarker) m).setHandyRenderer(handy);
            } else if (m instanceof MultiMarker) {

                final MultiMarker mm = (MultiMarker) m;
                for (final Marker subMarker : mm.getMarkers()) {
                    if (subMarker instanceof SketchyPolygonMarker) {
                        ((SketchyPolygonMarker) subMarker).setHandyRenderer(handy);
                    }
                }
            }
        }
        return markers;
    }

    @Override
    public void draw() {
        background(240);
        map.draw();
        // debugDisplay.draw();
    }

    @Override
    public void mouseClicked() {
        features = vectorTilesUtils.loadFeaturesForScreenPos(FEATURE_LAYER, mouseX, mouseY);
        map.addMarkers(createSketchMarkers(features));
    }

    @Override
    public void keyPressed() {
        if (key == ' ') {
            map.getDefaultMarkerManager().clearMarkers();
        }
    }

}
