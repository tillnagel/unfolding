package de.fhpotsdam.unfolding.examples.animation;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.looksgood.ani.Ani;

/**
 * Custom map animation with easing functions. Click anywhere to smoothly pan
 * there. Press 'z' or 'Z' to zoom in and out smoothly.
 *
 * Demonstrates how to create own animations, instead of the built-in tweening
 * functionality. In this example, the external Ani library is used.
 */
public class AnimationWithAniLibApp extends PApplet {

    UnfoldingMap map;

    float lat = 52.5f, lon = 13.4f;
    Location location = new Location(lat, lon);

    float currentZoom = 10;
    float targetZoom = currentZoom;

    public void setup() {
        size(1200, 600, P2D);

        map = new UnfoldingMap(this, new Microsoft.AerialProvider());
        map.zoomAndPanTo(location, (int) currentZoom);

        Ani.init(this);
    }

    public void draw() {
        // NB Zoom before pan

        map.zoomTo(currentZoom);

        location.setLat(lat);
        location.setLon(lon);
        map.panTo(location);

        map.draw();
    }

    public void keyPressed() {
        if (key == 'z' || key == 'Z') {
            if (key == 'z') {
                targetZoom++;
            }
            if (key == 'Z') {
                targetZoom--;
            }
            Ani.to(this, 4.5f, "currentZoom", targetZoom, Ani.ELASTIC_OUT);
        }
    }

    public void mouseReleased() {
        Location targetLocation = map.getLocation(mouseX, mouseY);

        Ani.to(this, 1.5f, "lat", targetLocation.getLat(), Ani.ELASTIC_OUT);
        Ani.to(this, 1.5f, "lon", targetLocation.getLon(), Ani.ELASTIC_OUT);
    }

}
