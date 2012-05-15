---
layout: page
title: "Unfolding in 5 Minutes"
---
{% include JB/setup %}

Description: This documents gives a short overview about how to use Unfolding for very simple applications.

Target audience: Everybody who wants to get a first impression of Unfolding.

Prerequisites: We assume you are familiar with Java and Processing.

# Content
We are going to write a really simple application that uses Unfolding to
display a map. Furthermore we can move around in the map. This document
basically describes SimpleMapApp.java

TODO: add image of the app here

To create an Unfolding app our class needs to inherit from PApplet:

	public class SimpleMapApp extends PApplet {
		...
	}

Furthermore we need to implement two functions:
1. `public void setup()`: initialize all our Unfolding stuff.
2. `public void draw()`: gets called periodically and should update our map.

First of all we tell the PApllet how big the screen should be:

	public void setup() {
		size(800, 600);
	}

Now we want a map, so let's create one as a member variable of `SimpleMapApp`:

	public class SimpleMapApp extends PApplet {
		Map map;
		...

In `setup()` we initialize the map and pass this so the map knows where to draw
on:

	public void setup() {
		size(800, 600);
		map = new Map(this);

Map is the main concept in Unfolding. Map represents a map and offers lots of
functionality like zooming, panning, translation of coordinates and much more.
To demonstrate this let's pan to a certain location on the map.

	map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);

Location is a helper class which represents locations on the map.
They are specified by longitude and latitude.
The "10" in the line above represents the zoom level of the map.
Zoom levels can be between XXX and XXX.

In order to get the behavior that we expect from a map (moving with mouse and
keyboard, etc.) we call the following:

	MapUtils.createDefaultEventDispatcher(this, map);

This basically activates the expected behavior.
Under the hood a lot more is happening, but that shouldn't concern us yet :)

We are basically done. We only need to implement `draw()` which is pretty
simple

	public void draw() {
		map.draw(); // draw the map
	}

and start the aplication:

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.SimpleMapApp" });
	}

That's it. We wrote our first Unfolding application which only 15 lines of
code including class definition etc.

Here is the complete code for the sake of completeness:

{% highlight java %}
public class SimpleMapApp extends PApplet {
	Map map;
	public void setup() {
		size(800, 600);
		map = new Map(this);
		map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);
		MapUtils.createDefaultEventDispatcher(this, map);
	}
	public void draw() {
		map.draw();
	}
	public static void main(String[] args) {
		PApplet.main(new String[] { "de.fhpotsdam.unfolding.examples.SimpleMapApp" });
	}
}
{% endhighlight %}


# Reference
- See examples.de.fhpotsdam.unfolding.examples.SimpleMapApp.java
