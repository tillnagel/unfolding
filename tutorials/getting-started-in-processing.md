---
layout: page
title: Getting Started in Processing
description: This Site will show how to set up the Unfolding library in Processing and how you create your first simple map in minutes.
group: tutorials-starter
author: Sebastian Sadowski
thumbnail: http://placehold.it/330x250
finalimage: http://placehold.it/610x390&text=Bild+2+610x390
finalfiles: #someurlatgithub
---

{% include JB/setup %}

## Download Library
Download the latest version of the [Unfolding library](https://github.com/tillnagel/unfolding/downloads "Download Unfolding") at Github.  
Unzip the archive and put the extracted Unfolding folder into the libraries folder of your Processing sketches.  

To find the Processing sketches location on your computer, open the Preferences window of the 
Processing application and look for the "Sketchbook location" item at the top. 
You will need to create the "libraries" folder if this is your first contributed library. 

Unfolding also needs the library GLGraphics to simplify the handling of OpenGL. Download the [GLGraphics library](http://sourceforge.net/projects/glgraphics/ "GLGraphics"), unzip it and put the GLGraphics folder in the libraries folder.

Restart Processing, start a new sketch and create your first simple map. 


## Hello World

To get started, you need to include the Unfolding, OpenGl and GLGraphics library via   
Sketch » Import Library and put the import statements at the top of your code.

	import processing.opengl.*;
	import codeanticode.glgraphics.*;
	import de.fhpotsdam.unfolding.*;
	import de.fhpotsdam.unfolding.geo.*;
	import de.fhpotsdam.unfolding.utils.*;

Create a reference to a “Map” object, i.e.

	de.fhpotsdam.unfolding.Map map;

In setup() you have to specified the GLGraphics OpenGL renderer as the third parameter of the size() function.  
Initialize a new map object and add the default event function for basic interaction (double-click to zoom  and drag to pan the map).

	void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		map = new de.fhpotsdam.unfolding.Map(this);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

Once you’ve done this you can begin to draw the map and run the sketch.

	void draw() {
		map.draw();
	}

You should get something like this.
![Hello World](/assets/images/helloworld.png)
