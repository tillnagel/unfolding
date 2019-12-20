package de.fhpotsdam.unfolding.examples.mask.custom;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.mapdisplay.OpenGLMapDisplay;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Map is shown inside a fuzzy circle. Uses an gray-scale image as map mask.
 */
public class DeformApp extends PApplet {

    UnfoldingMap map;

    DeformMapDisplayShader mapDisplayShader;
    
    @Override
    public void settings() {
        size(800, 600, OPENGL);
    }
    
    @Override
    public void setup() {
        map = new UnfoldingMap(this);
        MapUtils.createDefaultEventDispatcher(this, map);

        mapDisplayShader = new DeformMapDisplayShader(this, width, height);
        ((OpenGLMapDisplay) map.mapDisplay).setMapDisplayShader(mapDisplayShader);
    }

    @Override
    public void draw() {
        background(0);

        mapDisplayShader.setPos(mouseX, mouseY);

        map.draw();
    }
    
    public static void main(String args[]) {
        PApplet.main(new String[]{DeformApp.class.getName()});
    }
}
