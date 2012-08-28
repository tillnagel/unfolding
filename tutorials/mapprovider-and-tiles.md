---
layout: page
title: MapProvider and Tiles
description: The basics of including different MapProviders.
group: tutorials-beginner
thumbnail: /assets/images/tutorials/mapprovider_thumb.png
finalimage: /assets/images/tutorials/mapprovider.png
finalfiles: #someurlatgithub
---

{% include JB/setup %}

## Specify map provider
Unfolding supports different map providers and styles.   
Select a specific map provider at the initialisation of a new map object if you want to have another map provider than the standard (CloudMade: Open Street Maps).

For instance if you want to include the Microsoft Aerial Provider in a processing sketch:

	import processing.opengl.*;
	import codeanticode.glgraphics.*;
	import de.fhpotsdam.unfolding.*;
	import de.fhpotsdam.unfolding.geo.*;
	import de.fhpotsdam.unfolding.utils.*;
	import de.fhpotsdam.unfolding.providers.*;

	UnfoldingMap map;

	void setup() {
	  size(800, 600, GLConstants.GLGRAPHICS);
	  map = new UnfoldingMap(this, new Microsoft.AerialProvider());
	  MapUtils.createDefaultEventDispatcher(this, map);
	}

	void draw() {
	  map.draw();
	}


## Various map styles

You can use the following map styles with the "Cc-by-SA 2.0" License:
OpenStreetMap.OpenStreetMapProvider(  );   
OpenStreetMap.CloudmadeProvider( API KEY, STYLE ID );
OpenStreetMap.StamenTonerProvider(  ); 

Furthermore, the following map provider are for educational purposes only:
Google.GoogleTerrainProvider();   
Google.GoogleMapProvider();   
Google.GoogleSimplifiedProvider();   
Google.GoogleSimplified2Provider();   

Microsoft.RoadProvider();   
Microsoft.AerialProvider();   
Microsoft.HybridProvider();   

Yahoo.RoadProvider();   
Yahoo.AerialProvider();   
Yahoo.HybridProvider();   

ImmoScout.ImmoScoutProvider();   
ImmoScout.HeatMapProvider();   

MapBox.MapBoxProvider();   
MapBox.WorldLightProvider();   
MapBox.ControlRoomProvider();   
MapBox.LacquerProvider();   
MapBox.MuseDarkStyleProvider();   
MapBox.PlainUSAProvider();   
MapBox.BlankProvider();   

