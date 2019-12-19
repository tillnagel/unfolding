package de.fhpotsdam.unfolding.examples.data.temporal;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import processing.core.PApplet;
import processing.core.PVector;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoRSSReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Displays earthquake markers from an RSS feed over time.
 *
 * Animates through earthquakes in 1h steps, and fades out dots.
 *
 * Press SPACE for starting or stopping the animation. Press LEFT ARROW or RIGHT
 * ARROW to step through time.
 */
public class AnimatedTemporalDotsApp extends PApplet {

    String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/atom/4.5/week";

    UnfoldingMap map;
    List<Marker> markers;

    DateTime startTime;
    DateTime endTime;
    DateTime currentTime;
    boolean animating = true;

    public void setup() {
        size(900, 600, OPENGL);
        smooth();

        map = new UnfoldingMap(this);
        map.zoomToLevel(2);
        MapUtils.createMouseEventDispatcher(this, map);

        print("Loading earthquakes from web feed ... ");
        List<Feature> features = GeoRSSReader.loadDataGeoRSS(this, earthquakesURL);
        println("done.");
        markers = MapUtils.createSimpleMarkers(features);

        // Earthquakes are ordered from latest to oldest
        startTime = new DateTime(features.get(features.size() - 1).getProperty("date"));
        endTime = new DateTime(features.get(0).getProperty("date"));
        currentTime = startTime.plus(0);
        println("Dates of earthquakes ranges from " + startTime + " to " + endTime);
    }

    public void draw() {
        background(0);
        map.draw();

        for (Marker marker : markers) {
            DateTime markerTime = new DateTime(marker.getProperty("date"));
            if (markerTime.isBefore(currentTime)) {
                ScreenPosition pos = map.getScreenPosition(marker.getLocation());
                drawGrowingEarthquakeDots(pos, markerTime);
            }
        }

        // Each 4 frame (and if currently animating)
        if (animating && frameCount % 1 == 0) {
            currentTime = currentTime.plusMinutes(10);
            // Loop: If end is reached start at beginning again.
            if (currentTime.isAfter(endTime)) {
                currentTime = startTime.plus(0);
            }
        }

        noStroke();
        fill(0, 200);
        rect(10, 10, 270, 20);
        fill(255);
        text("Time: " + currentTime, 13, 24);
    }

    public void drawEarthquakeDots(PVector pos, DateTime time) {
        fill(255, 0, 0, 100);
        stroke(255, 0, 0, 200);
        strokeWeight(1);

        // Size of circle depends on age of earthquake, with 12h = max (20px)
        int hours = Hours.hoursBetween(time, currentTime).getHours();
        float size = constrain(map(hours, 0, 12, 20, 0), 0, 20);

        ellipse(pos.x, pos.y, size, size);
    }

    public void drawGrowingEarthquakeDots(PVector pos, DateTime time) {

        // Marker grows over time
        int minutes = Minutes.minutesBetween(time, currentTime).getMinutes();
        int maxMinutes = 12 * 60;
        float size = constrain(map(minutes, 0, maxMinutes, 0, 30), 0, 30);

        // But fades away the colors
        float alphaValue = constrain(map(minutes, 0, maxMinutes, 100, 0), 0, 100);
        float alphaStrokeValue = constrain(map(minutes, 0, maxMinutes, 255, 0), 0, 255);

        // Draw outer (growing) ring
        fill(255, 0, 0, alphaValue);
        stroke(255, 0, 0, alphaStrokeValue);
        strokeWeight(1);
        ellipse(pos.x, pos.y, size, size);

        // Always draw the epicenter
        fill(255, 0, 0);
        ellipse(pos.x, pos.y, 4, 4);
    }

    public void keyPressed() {
        if (key == ' ') {
            animating = !animating;
        }
        if (key == CODED) {
            if (keyCode == LEFT) {
                currentTime = currentTime.minusHours(1);
            }
            if (keyCode == RIGHT) {
                currentTime = currentTime.plusHours(1);
            }
        }
    }

}
