package de.fhpotsdam.unfolding.overviewdetail;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.examples.overviewdetail.connection.OverviewPlusDetailConnection;

public class KiteConnectionApp extends PApplet {

    OverviewPlusDetailConnection connection;

    @Override
    public void settings() {
        size(800, 600, OPENGL);
    }
    
    @Override
    public void setup() {
        connection = new KiteConnection(this);
    }

    @Override
    public void draw() {
        background(255);

        connection.draw();
        //kiteConnection.drawDebug();
    }

    @Override
    public void mouseMoved() {
        connection.setOverviewPosition(mouseX, mouseY);
    }

    @Override
    public void mouseDragged() {
        connection.setDetailPosition(mouseX, mouseY);
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{KiteConnectionApp.class.getName()});
    }
}
