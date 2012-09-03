---
layout: page
title: Markers 
description: How to use point, line, and polygon markers, how to style them, and how to handle simple interactions.
group: tutorials-beginner
thumbnail: ../assets/images/tutorials/markers-simple-thumb.png
finalimage: 
---

{% include JB/setup %}


Displaying markers on a map is very straight-forward. Just create a marker with a location and add it to the map once.
Here, for instance, we are creating two point markers, one for Berlin and one for Dublin.

	Location berlinLocation = new Location(52.5, 13.4);
	Location dublinLocation = new Location(53.35, -6.26);
  	
	// Create point markers for locations
	SimplePointMarker berlinMarker = new SimplePointMarker(berlinLocation);
	SimplePointMarker dublinMarker = new SimplePointMarker(dublinLocation);
  	
	// Add markers to the map
	map.addMarkers(berlinMarker, dublinMarker);

They will be drawn automatically on top of the map, always at the correct position.

![Simple Point Markers](../assets/images/tutorials/markers-simple.png)

Unfolding provides a default marker style, and has point, line, and polygon markers out of the box.

## Style your markers

All default markers provide some very basic styling methods, e.g. to set stroke and fill colors.

	// Adapt style
	berlinMarker.setColor(color(255, 0, 0, 100));
	berlinMarker.setStrokeColor(color(255, 0, 0));
	berlinMarker.setStrokeWeight(4);

For more sophisticated marker customization or for creating data glyphs, there are two options:
- Drawing it yourself
- Creating own marker class (advanced)

The easiest method to create a custom style is to draw the marker yourself instead of adding it to the map.

![Styled markers](../assets/images/tutorials/marker-style-2.png)

For this you need to make the marker global, i.e. define the variable first in your sketch (here: line 2), then assign a new marker in setup (line 11).
In draw() get the current position of the marker (line 19), and draw some visual representation with Processing's drawing functions (lines 20-23).

	UnfoldingMap map;
	SimplePointMarker berlinMarker;

	void setup() {
	  size(800, 600, GLConstants.GLGRAPHICS);

	  map = new UnfoldingMap(this);
	  MapUtils.createDefaultEventDispatcher(this, map);

	  Location berlinLocation = new Location(52.5, 13.4);
	  berlinMarker = new SimplePointMarker(berlinLocation);

	  // Do not add marker to the map
	}

	void draw() {
	  map.draw();

	  ScreenPosition berlinPos = berlinMarker.getScreenPosition(map);
	  strokeWeight(16);
	  stroke(67, 211, 227, 100);
	  noFill();
	  ellipse(berlinPos.x, berlinPos.y, 36, 36);
	}

`marker.getScreenPosition(map)` returns the current x,y-position of the marker on the map. The method converts the geo-location of that marker to the current `ScreenPosition` on the canvas of the sketch.


Now, you can style your markers in any way you want. In the following example we draw the marker as two arcs with a text label.

![Marker as ring with label](../assets/images/tutorials/marker-style-1.png)

	ScreenPosition posLondon = markerLondon.getScreenPosition(map);
	strokeWeight(12);
	stroke(200, 0, 0, 200);
	strokeCap(SQUARE);
	noFill();
	float s = 44;
	arc(posLondon.x, posLondon.y, s, s, -PI * 0.9, -PI * 0.1);
	arc(posLondon.x, posLondon.y, s, s, PI * 0.1, PI * 0.9);
	fill(0);
	text("London", posLondon.x - textWidth("London") / 2, posLondon.y + 4);

(By drawing the marker yourself, the marker is rendered after the map. In special cases, such as when using the Processing2D renderer, this can result in slight lags. If you have experienced this you should try the second option: Create your own marker class.) 


## Create your own marker

You can also create your own marker class by extending AbstractMarker, which gives you full control over style and interaction handling.

Besides, by creating your own marker class you will have cleaner code, and can manage hundreds of markers with a more elegant software architecture, as the drawing will be handled automatically by the map.

![Image marker](../assets/images/tutorials/markers-image.png)

See the [Image Marker example](../examples/40_image-marker.html) for code on how to create an own marker class displaying images.


## Line and polygon marker

(Coming soon.)


## Selecting a marker

Unfolding's simple markers support highlighting. This can be used e.g. for enabling an interactive picking mechanism, or to animate through different markers.

You can manually set the status, and it will be displayed in the default highlight style.

	SimplePointMarker marker = new SimplePointMarker(new Location(52.5, 13.4));
	marker.setSelected(true);

If you want to allow users to interactively select and deselect markers, you need to check which marker the mouse pointer is over currently, and set the status accordingly. For instance, like this:

	public void mouseMoved() {
		Marker hitMarker = map.getFirstHitMarker(mouseX, mouseY);
		if (hitMarker != null) {
			// Select current marker 
			hitMarker.setSelected(true);
		} else {
			// Deselect all other markers
			for (Marker marker : map.getMarkers()) {
				marker.setSelected(false);
			}
		}
	}

NB: You can use the same selection mechanism when creating your own marker classes.


## Multi marker

You can also create markers consisting of multiple sub markers.

The following example shows the shape for France and Corsica, both defined as (crude) polygons. Both areas are `SimplePolygonMarker` and combined in one `MultiMarker`. Only the `MultiMarker` is added to the map.

![Multi-Marker with two polygons](../assets/images/tutorials/marker-multi-select.png)

	// Create markers for each area
	SimplePolygonMarker franceMarker = new SimplePolygonMarker(getFranceShapeLocations());
	SimplePolygonMarker corsicaMarker = new SimplePolygonMarker(getCorsicaShapeLocations());

	// Combine as MultiMarker
	MultiMarker multiMarker = new MultiMarker();
	multiMarker.addMarkers(franceMarker, corsicaMarker);

	// Add only MultiMarker to the map
	map.addMarkers(multiMarker);

As you can see, this works also with selection handling. When the user moves the mouse over one of the areas, both are highlighted. 


## Fixed and dynamic sized markers

Note that we draw a zoom dependent marker, i.e. the marker will be bigger in zoomed in, and smaller in zoomed out maps.

	// Zoom dependent marker size
	float s = map.getZoom();


## Creating data markers from GeoJSON, GPX & Co

If you want to load data from GeoJSON or various other file formats, take a look at our [Markers & Data tutorial](markers-data.html).





