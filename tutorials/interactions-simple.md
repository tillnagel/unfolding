---
layout: page
title: Simple Interactions (draft)
description: How to set up simple user interactions
group: tutorials-beginner
thumbnail: ../assets/images/tutorials/interactions-thumb.png

---
{% include JB/setup %}

As you have seen in the Getting Started tutorial every map you create is already interactive. By creating the default event dispatcher (as shown above), users already can interact with your map.

Users can pan the map by dragging it with the mouse, or by using the arrow keys on the keyboard. Using the mouse wheel zooms in or out, which also works by pressing + or - keys. Double-clicking on the map centers it around that location, and zooms in one level. 

## Zooming and panning the map

Now, let's say you want to focus your visualization on a city. Manually set the location and zoom level in the setup() method.

	map.zoomAndPanTo(new Location(52.5f, 13.4f), 10);

Here, we pan to Berlin and zoom to a level users can see the whole city area.

There are many other methods, e.g.

	map.zoomLevelOut()
	map.zoomLevelIn()
	map.zoomIn()
	map.zoomOut()
	map.panTo(Location location);
	map.panTo(ScreenPosition pos);
	
and many more. Check the [`UnfoldingMap` API documentation](http://tillnagel.github.com/unfolding/javadoc/index.html?de/fhpotsdam/unfolding/UnfoldingMap.html) for all.


## Restricting maps

You might want to restrict the map interactions, for instance because you only have data for a specific area.

### Panning restrictions
For that, we create a Location for the city, and use it to center the map (as before), but we are using that Location also as center for the panning restriction. 
	
	Location berlinLocation = new Location(52.5f, 13.4f);
	map.zoomAndPanTo(berlinLocation, 10);
	float maxPanningDistance = 30; // in km
	map.setPanningRestriction(berlinLocation, maxPanningDistance);

Users now can drag the map only for 30 km around the city center.

### Zooming restrictions

You can also restrict zooming by specifying minimum and maximum zoom levels.
		
	map.setZoomRange(12, 15);

The first parameter defines the lower, the second the upper zoom level. Play around with the numbers to see what zoom levels they are. Roughly, 12 is city-level, and 15 is street-level.

This works well in combination with rendered TileMill tiles, as often you only want to export some zoom levels in order to save disk and memory space.


## Tweening

For simple animations between different zoom levels simply switch on map tweening.

	map.setTweening(true);
	
Note: This is only working for panning, and not for zooming at the moment! See [Issue 5](https://github.com/tillnagel/unfolding/issues/5) for updates.

(More to come soon.)


## Animation

Take a look at the SimpleAnimatedMapApp example for a very simple panning animation. Of course, you might want to use a easing function or some animation library for more sophisticated animations.

Also check out the CenteredTrackApp example. The map is centered around the current position of an animated bike trail loaded from a GPX file.

(More to come soon.)


<!--
## Handling interactions

If you want your users to only be able to use the mouse you can disable the keyboard input.

	map.disableKeyboard();

Analogous, you can also disable mouse interactions

	map.disableMouse();

-->