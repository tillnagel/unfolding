package de.fhpotsdam;

import processing.core.PApplet;
import processing.core.PImage;

public class ImageLoadFromJarTestApp extends PApplet {

    PImage img;

    @Override
    public void settings() {
        size(800, 600);
    }

    @Override
    public void setup() {
        img = loadImage("ui/unfolding-mini-icon.png");
    }

    @Override
    public void draw() {
        background(240);

        image(img, mouseX, mouseY);
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{ImageLoadFromJarTestApp.class.getName()});
    }
}
