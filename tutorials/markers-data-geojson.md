---
layout: page
title: Markers &amp; Data
description: Loading and displaying geospatial data from GeoJSON, GPX, and other files.
group: tutorials-beginner
thumbnail: ../assets/images/tutorials/markers-simple-thumb.png
finalimage: 
---

{% include JB/setup %}

## Style your markers

Lorem ipsum

	doSomething();
	marker = featere(255, 0, 0));

That's it folks.

![Styled markers](../assets/images/tutorials/marker-style-2.png)

## Read from files

GeoJSON, GPX, GeoRSS, 

KML

basic support.

For support of full specs use some other library, e.g. the xxx lib.



### Using Shapefiles

Various kinds of geo-data are freely available, and often provided as [Shapefile](http://en.wikipedia.org/wiki/Shapefile). These contain geometries such as points, lines, and polygons, and additional data attributes.

Unfolding does not provide parsing Shapefiles out-of-the-box. You can either use some library to parse a Shapefile and convert it to Unfolding markers. For instance, use the great [GeoTools library](http://geotools.org/), to read a Shapefile (See e.g. [here](http://stackoverflow.com/questions/2044876/does-anyone-know-of-a-library-in-java-that-can-parse-esri-shapefiles)).

Alternatively, you can convert a Shapefile to another format. One easy tool to use is the [OGR Simple Feature Library](http://www.gdal.org/ogr/) (part of [GDAL](http://www.gdal.org)). After installing it on your machine, you can simply convert geo-data from one format to another, e.g.:

	ogr2ogr -f geoJSON countries.json countries.shp

If you don't want to install that library, you can use the [ogr2ogr web client](http://ogre.adc4gis.com/).

For an example, let's use a Shapefile containing borders of all countries. Download the simplified version from [thematicmapping.org](http://thematicmapping.org/downloads/world_borders.php). Then, go to [ogr2ogr web client](http://ogre.adc4gis.com/), upload that zip file, and convert it to GeoJSON. Then, save the result as "countries.geo.json" in the data folder of your Unfolding sketch.

	UnfoldingMap map;
	
	void setup() {
		size(800, 600, GLConstants.GLGRAPHICS);
		
		map = new UnfoldingMap(this);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		List<Feature> countries = GeoJSONReader.loadData(this, "countries.geo.json");
		List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
	}

	void draw() {
		map.draw();
	}



## Features and Markers


### Specify which markers to create automatically

### Create markers from features manually


## Read from database


##



