---
layout: page
title: MapProvider and Tiles
description: How to use another map style, switch between them, and how to create your own. Also gives a short introduction to map tiles.
group: tutorials-beginner
thumbnail: /assets/images/tutorials/mapprovider_thumb.png
finalimage: /assets/images/tutorials/mapproviders.png
finalfiles: #someurlatgithub
---

{% include JB/setup %}

## Map tiles

Unfolding maps are based on pre-rendered map tiles. Each tile is a small image, and contains the topographic information of a rectangular map area as pixel-based graphic. 

Each tile consists of 256 × 256 pixels. This tile-based approach is done to manage maps at higher zoom levels. Otherwise, a single image showing the entire earth would be too large, e.g. for a zoom level of 10 it would consist of 131.072 × 131.072 pixels.

By using tiles you easily can develop interactive maps. On top of the basis map you can draw markers, vectors, diagrams, user interface elements, and everything else.

<!--
"A *slippy map* is type of web-browser based map client that allows you to dynamically pan the map simply by grabbing and sliding the map image in any direction. Modern web browsers allow dynamic loading of map tiles in response to user action without requiring a page reload. This dynamic effect makes map viewing more intuitive." (John Frank, MetaCarta)
-->

## Map styles

Unfolding displays maps in a default style, with cartographic data from OpenStreetMaps and tiles from CloudMade. To use another map style, simply specify it as second parameter when constructing an `UnfoldingMap`. This way, you can easily switch to one of the pre-configured map tile providers.

For instance if you want to display a satellite map use the Microsoft Aerial Provider:

	UnfoldingMap map;

	void setup() {
	  size(800, 600, GLConstants.GLGRAPHICS);
	  map = new UnfoldingMap(this, new Microsoft.AerialProvider());
	  MapUtils.createDefaultEventDispatcher(this, map);
	}

	void draw() {
	  map.draw();
	}


### Various map providers

Unfolding comes with a couple of pre-configured map providers.

Keep in mind you need to check the terms and conditions of the map providers on how you are allowed to use their map tiles. We are providing the example providers for educational purposes, only. (Some come with a "CC-BY-SA 2.0" license, some with more restricted licenses.)

The following are some of the map providers included in the Unfolding distribution:

- OpenStreetMap.OpenStreetMapProvider();   
- OpenStreetMap.CloudmadeProvider(API KEY, STYLE ID);
- Stamen.TonerProvider(); 
- Google.GoogleMapProvider();   
- Google.GoogleTerrainProvider();   
- Microsoft.RoadProvider();   
- Microsoft.AerialProvider();   
- Yahoo.RoadProvider();   
- Yahoo.HybridProvider();   

Check the [MapProvider package API](http://tillnagel.github.com/unfolding/javadoc/index.html?de/fhpotsdam/unfolding/providers/package-summary.html) for more styles.


### Create own map provider

You can also create your own `MapProvider` in order to load and use other map tiles.

For that you need to know the URLs to access the tiles, and extend an appropriate MapProvider with a similar request mechanism. Take a look at the `StamenMapProvider` to see how to extend the OpenStreetMapProvider.

(More to come soon.)


## Create your own map style
If you want to create a completely new map style you can use different methods. For simple adaptations you could use the [CloudMade style editor](http://developers.cloudmade.com/projects/show/style-editor) and specify its ID in the `CloudmadeProvider`. For more options you can create a map with TileMill. See the article [TileMill for Processing](http://tillnagel.com/2011/06/tilemill-for-processing/) for a brief introduction on how to create maps with TileMill.

After you exported your styled map to a MBTiles file (a database containing the tiles), you can use it with Unfolding.
For this, you need to add the [SQLlite driver](http://code.google.com/p/sqlite-jdbc/) to your Processing/Java libraries. Then, specify the path to the MBTiles file in the `MBTilesMapProvider`.

	UnfoldingMap map;

	void setup() {
	  size(800, 600, GLConstants.GLGRAPHICS);

	  String connStr = "jdbc:sqlite:" + sketchPath("data/myMapStyle.mbtiles");
	  map = new UnfoldingMap(this, new MBTilesMapProvider(connStr));
	  MapUtils.createDefaultEventDispatcher(this, map);
	  map.setZoomRange(2, 4);
	}

	void draw() {
	  map.draw();
	}

In Processing you need to specify the absolute file path. In line 6 above, we use Processing's `sketchPath()` method to access the MBTiles file. In Eclipse you should put that database file into your data folder, and reference it relatively.

	String connStr = "jdbc:sqlite:jdbc:sqlite:../data/myMapStyle.mbtiles");
	

	
## Switch map provider dynamically

There are two ways to switch between two or more styles dynamically.

1. Switch between maps
2. Switch provider of a single map 

See the DynamicMapSwitch and DynamicProviderSwith examples.

(More to come soon.)



