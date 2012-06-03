---
layout: page
title: Interactions 1
description: How to set up simple user interactions
group: tutorials-beginner
finalimage: http://placehold.it/610x390&text=Bild+2+610x390
finalfile: #someurlatgithub
---
{% include JB/setup %}

As you have seen in the Getting Started tutorial every map you create is already interactive. Users directly can double-click to zoom in around that location, and dragging pans the map.

Use arrow keys to pan the map in four directions, while plus and minus keys zoom in or out

![Lala](/assets/images/Unfolding-GIF-Test.gif)

## Handling interactions

If you want your users to only be able to use the mouse you can disable the keyboard input.

	map.disableKeyboard();

Analogous, you can also disable mouse interactions

	map.disableMouse();


## Restricting the map

In order to allow only some zoom levels you can restrict the range of the map.

	void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		map = new Map(this);
		map.setZoomRange(12, 15);
	}

The first parameter defines the lower, the second the upper zoom level. Play around with the numbers to see what zoom levels they are. Roughly, 12 is city-level, and 15 is street-level.

// MapUtils.ZOOM_COUNTRY_LEVEL, MapUtils.ZOOM_STREET_LEVEL

This works well in combination with rendered MapBox tiles, as often you only want to export some zoom levels in order to save disk and memory space. See MapBox tutorial.

If you want to create a map for a specific area, you can restrict the panning.

	void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		map = new Map(this);
		Location location = new Location(1.359f, 103.816f);
		float maxPanningDistance = 30; // in km
		map.setPanningRestriction(location, maxPanningDistance);
	}

Define the center location and the radius of the maximum panning.

