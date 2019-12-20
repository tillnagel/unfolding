package de.fhpotsdam.matrix;

import processing.core.PApplet;

public class GridTest extends PApplet {

    Grid grid;

    @Override
    public void settings() {
        size(800, 600, OPENGL);
        smooth();
    }
    
    @Override
    public void setup() {
        grid = new Grid(this, 50, 50, 300, 200);
    }

    @Override
    public void draw() {
        background(240);

        if (mousePressed) {
            if (mouseButton == LEFT) {
                grid.angle += radians(45);
            } else {
                grid.angle -= 0.01f;
            }
        }
        if (keyPressed) {
            if (key == '+') {
                grid.scale += 0.01f;
            } else if (key == '-') {
                grid.scale -= 0.01f;
            }
        }

        grid.draw();
    }

    @Override
    public void mouseMoved() {
        grid.centerX = mouseX;
        grid.centerY = mouseY;
    }

    @Override
    public void mouseDragged() {
        mouseMoved();
    }

    @Override
    public void keyPressed() {
        if (key == ' ') {

            grid.setCenter(50, 50);
            grid.rotate(radians(10));

            grid.centerX = 350;
            grid.rotate(radians(-10));

            grid.centerX = 50;
            grid.rotate(radians(10));

            grid.centerX = 350;
            grid.rotate(radians(-10));

            grid.centerX = 50;
            grid.rotate(radians(10));

            grid.centerX = 350;
            grid.rotate(radians(-10));
        }
    }
    
    public static void main(String args[]) {
        PApplet.main(new String[]{GridTest.class.getName()});
    }
}
