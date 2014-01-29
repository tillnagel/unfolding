---
layout: page
title: Multitouch maps
description: How to create interactive maps for multitouch devices.
group: tutorials-advanced
thumbnail: ../assets/images/tutorials/multitouch-thumb.png
---
{% include JB/setup %}


Unfolding can handle multitouch interactions and provides basic map manipulation. 
One finger pans the map, while two or more fingers rotate and zoom the map.

## Multitouch map

Besides handlers for mouse and keyboard, Unfolding provides one for [TUIO](http://www.tuio.org) cursors (fingers or other small objects).

First, download and install the [TUIO library for Processing](http://www.tuio.org/?processing). Then, in your sketch, initiate the `TuioCursorHandler` which listens to TUIO events on the default port. Then add it to the `EventDispatcher` as an handler able to broadcast map events. Lastly, register the map to both _pan_ and _zoom_ events. 

Now, users can drag to pan the map to a new position. They can pinch close to zoom in, and pinch open to zoom out. They can also rotate the map with two fingers, e.g. to align the map towards the user's standing position.


	import TUIO.*;
	import de.fhpotsdam.unfolding.*;
	import de.fhpotsdam.unfolding.events.*;
	import de.fhpotsdam.unfolding.interactions.*;

	UnfoldingMap map;
	TuioCursorHandler tuioCursorHandler;

	void setup() {
		size(800, 600);

		map = new UnfoldingMap(this);

		tuioCursorHandler = new TuioCursorHandler(this, map);
		EventDispatcher eventDispatcher = new EventDispatcher();
		eventDispatcher.addBroadcaster(tuioCursorHandler);
		eventDispatcher.register(map, "pan");
		eventDispatcher.register(map, "zoom");
	}

	void draw() {
		map.draw();
		
		// For debugging purpopses
		tuioCursorHandler.drawCursors();
	}

Instead of the default drawCursors() you could also draw the touch points yourself, e.g. for a subtle visual feedback.

	fill(255, 50);
	for (TuioCursor tcur : tuioCursorHandler.getTuioClient().getTuioCursors()) {
		ellipse(tcur.getScreenX(width), tcur.getScreenY(height), 20, 20);
	}


## Multitouch map and user interface

In order to create application having multitouch functionality for more than map navigation, you need to make your app itself multitouch-capable. 

For instance, you might want to allow your users to tap on markers to select them, to manipulate a time range slider, or to switch between different map layers.

For this, you need to create your own TUIO listener in the app, and connect it to Unfolding's `TuioCursorHandler`.
Now the main app listens to TUIO events, and forwards the multitouch events to the handler. This allows reacting to touch interactions in the application, as well as having an interactive multitouch map. 

See the [MultitouchMapExternalTuioApp](https://github.com/tillnagel/unfolding/blob/master/examples/de/fhpotsdam/unfolding/examples/interaction/multitouch/MultitouchMapExternalTuioApp.java) example for source code.

(More to come soon.)


### Fullscreen

Visualizations for tabletops or other large-screen multitouch devices you might want to go fullscreen.
Check out the [Processing Wiki on Window Size and Full Screen](http://wiki.processing.org/w/Window_Size_and_Full_Screen).

In Eclipse you could do it like this:
	
	public static void main(String[] args) {
		String[] params = new String[] { "--present", "--bgcolor=#000000", "--hide-stop",
			"--exclusive", "de.fhpotsdam.unfolding.examples.MyMapApp" };
		PApplet.main(params);
	}

Keep in mind, that your layout either has to be dynamic, i.e. adapt to various screen sizes, or you have to customize your sketch for the specific target resolution.

## Exhibition

Take a look the [exhibition](../exhibition/). Many of the exhibited projects are designed for multitouch interaction.

See the [LiquiData video](https://vimeo.com/43120464#at=24) (jumps to 24s) for a nice example of two finger map navigation.

