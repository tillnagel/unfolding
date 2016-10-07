package de.fhpotsdam.unfolding.examples.data.vectortiles;

import java.util.List;

import org.apache.log4j.Logger;
import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.examples.interaction.snapshot.CircularMapSnapshot;
import de.fhpotsdam.unfolding.examples.interaction.snapshot.MapSnapshot;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.GeoUtils;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Experiment to select building, then zoom+pan to fit and show in thumbnail.
 */
public class SelectBuildingsApp extends PApplet {

    private static final Logger LOGGER = Logger.getLogger(SelectBuildingsApp.class);
    /** Name of the features layer (in OpenStreetMap). */
    public static final String FEATURE_LAYER = "buildings";
    private MapSnapshot mapSnapshot = null;
    private UnfoldingMap map;
    private VectorTilesUtils vectorTilesUtils;
    private Location[] boundingBox;

    @Override
    public void settings() {
        size(800, 600, P2D);
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{SelectBuildingsApp.class.getName()});
    }

    @Override
    public void setup() {
        map = new UnfoldingMap(this, 0, 0, 600, 600);

        map.zoomAndPanTo(16, new Location(52.501, 13.395));
        MapUtils.createDefaultEventDispatcher(this, map);
        map.setZoomRange(10, 19);

        vectorTilesUtils = new VectorTilesUtils(this, map);
        final List<Marker> markers = vectorTilesUtils.loadMarkersForScreenPos(FEATURE_LAYER, width / 2, height / 2);
        map.addMarkers(markers);
    }

    @Override
    public void draw() {
        background(0);
        map.draw();

        if (mapSnapshot != null)
            mapSnapshot.draw(620, 20, 200, 200);

        if (boundingBox != null) {
            final ScreenPosition nwPos = map.getScreenPosition(boundingBox[0]);
            final ScreenPosition sePos = map.getScreenPosition(boundingBox[1]);
            stroke(0, 255, 0, 200);
            noFill();
            rect(nwPos.x, nwPos.y, sePos.x - nwPos.x, sePos.y - nwPos.y);
        }
    }

    @Override
    public void mouseClicked() {
        List<Marker> markers = map.getMarkers();
        for (final Marker marker : markers)
            marker.setHidden(true);

        final Marker hitMarker = map.getFirstHitMarker(mouseX, mouseY);
        if (hitMarker != null) {

            map.zoomAndPanToFit(GeoUtils.getLocations(hitMarker));
            map.draw();

            boundingBox = GeoUtils.getBoundingBox(GeoUtils.getLocations(hitMarker));

            mapSnapshot = new CircularMapSnapshot(this, map, 300, 5);
            mapSnapshot.snapshot(map, 0, 0, 600, 600);

            mapSnapshot.draw(0, 0, 600, 600);

            hitMarker.setHidden(false);
            final String buildingName = hitMarker.getStringProperty("name");
            LOGGER.info(buildingName != null ? buildingName : "n/a");
        }
    }

    @Override
    public void keyPressed() {
        if (key == 't') {
            List<Marker> markers = map.getMarkers();
            for (Marker marker : markers)
                marker.setHidden(false);
        }
        if (key == 's') {
            mapSnapshot = new CircularMapSnapshot(this);
            mapSnapshot.snapshot(map);
        }
    }

}
