package de.fhpotsdam.unfolding.examples.misc;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;

public class Unfolding3DMapApp extends PApplet {

    private UnfoldingMap map;
    private float rotateX = 0.9f;
    private float rotateZ = (float) 0;
    private float rotateVelocityZ = 0.003f;

    @Override
    public void settings() {
        size(800, 600, P3D);
    }

    @Override
    public void setup() {
        map = new UnfoldingMap(this);
        map.zoomAndPanTo(4, new Location(51.5, 0));
    }

    @Override
    public void draw() {
        background(40);

        translate(width / 2, height / 3, 0);
        rotateX(rotateX);
        rotateZ(rotateZ);
        translate(-map.getWidth() / 2, -map.getHeight() / 2);
        map.draw();

        rotateZ += rotateVelocityZ;
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{Unfolding3DMapApp.class.getName()});
    }

}
