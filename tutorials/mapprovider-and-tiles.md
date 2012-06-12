---
layout: page
title: MapProvider and Tiles
description: How do I include different MapProviders and what are tiles?
group: tutorials-starter
author: Sebastian Sadowski
thumbnail: http://placehold.it/330x250
finalimage: http://placehold.it/610x390&text=Bild+2+610x390
finalfiles: #someurlatgithub
---

{% include JB/setup %}

## Various map styles
Unfolding supports different map providers and styles:
- Google Maps 
- Google Terrain
- Microsoft Aerial
- CloudMade: Open Street Map
- CloudMade: Midnight Commander
- ImmoScout Heatmap
![mapprovider](/assets/images/tutorials/mapprovider.png)

## Specify map provider
Select a specific map provider at the initialisation of a new map object if you want to have another map provider than the standard (open streets maps). All Providers are listed in the [official documentation](https://github.com/tillnagel/unfolding/downloads "official documentation"). 

For instance if you want to include the Microsoft Aerial Provider in a processing sketch:

	import processing.opengl.*;
	import codeanticode.glgraphics.*;
	import de.fhpotsdam.unfolding.*;
	import de.fhpotsdam.unfolding.geo.*;
	import de.fhpotsdam.unfolding.utils.*;
	import de.fhpotsdam.unfolding.providers.*;

	de.fhpotsdam.unfolding.Map map;

	void setup() {
	  size(800, 600, GLConstants.GLGRAPHICS);
	  map = new de.fhpotsdam.unfolding.Map(this, new Microsoft.AerialProvider());
	  MapUtils.createDefaultEventDispatcher(this, map);
	}

	void draw() {
	  map.draw();
	}


## Tiles
Map tiles are square bitmap graphics displayed in a grid arrangement to show a map.
A "tileset" typically includes enough tiles to form a very large image but generally the idea is not to show them all at once, but to display only a particular area of the map so the map can load faster.
The beaking down of a huge map to manageable pieces is like a pyramid of image tiles
![tilespyramid](/assets/images/tutorials/tilespyramid.jpeg)