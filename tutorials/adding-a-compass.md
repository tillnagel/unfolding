---
layout: page
title: Adding a Compass
description: This little Tutorial will explain how to add a simple compass to your map
group: tutorials-beginner
author: Christopher Pietsch & 
thumbnail: http://placehold.it/330x250
finalimage: http://placehold.it/610x390&text=Bild+2+610x390
finalfiles: #someurlatgithub
---

{% include JB/setup %}

## How to add a compass

To test the compass you can rotate the map via **a** and **d**.


![compass](https://github.com/tillnagel/unfolding/raw/compass/web/screenshots/compass.png)

## Simple
Check out the [SimpleCompassApp.java](https://github.com/tillnagel/unfolding/blob/compass/examples/de/fhpotsdam/unfolding/examples/ui/SimpleCompassApp.java).

To add a compass you only need the compass object

	CompassUI compass;

initialize it in your setup()

	compass = new CompassUI(this,map);

and draw it in your draw()

	compass.draw();

Thats it, easy peasy.

## Advanced

We have also a more complex constructor for you, to make various changes like display a custom compass image. [ComplexCompassApp.java](https://github.com/tillnagel/unfolding/blob/compass/examples/de/fhpotsdam/unfolding/examples/ui/ComplexCompassApp.java)

	public CompassUI(PApplet p, AbstractMapDisplay mapDisplay, PImage img, float x, float y)

* mapDisplay: your mapDisplay
* img: the PImage you want to use as a compass
* x: absolute x
* y: absolute y


To change the compass image you need to initialize a new PImage and initialize the compass with it:

	PImage compassImg = loadImage("compass_grey.png");
	compass = new CompassUI(this, map.mapDisplay, compassImg, 700, 100, 1);
