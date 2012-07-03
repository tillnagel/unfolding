---
layout: page
title: Unfolding Overview
description: What is Unfolding and how is the background?
group: tutorials-starter
author: Till Nagel, Felix, Sebastian Sadowski
thumbnail: http://placehold.it/330x250
finalimage: http://placehold.it/610x390&text=Bild+2+610x390
finalfiles: #someurlatgithub
---

{% include JB/setup %}

## Unfolding - A Map library for Processing & Java
Unfolding is a map library that was created to handle and interact with maps in processing and java. Initially founded by researcher and lecturer Till Nagel in 200X, Unfolding has been mainly used by interfacedesign students at the university of applied sciences to create simple interactive maps on large multitouch tables. Since 200X several projects were realized with unfolding


## Features
The advantages are


## Background & Technology
Unfolding is based on Java ...

Unfolding is a tile-based map library. Map tiles can have various geographic features, and come in all kind of styles. It comes with various map providers, such as OpenStreetMap or TileMill.


Architecture
Tile mechanism: Provider, SlippyMaps, Coordinates etc
Transformation: Matrices, Levels (Geo/inner, Map/outer, screen/canvas), etc


## Tiles
Map tiles are square bitmap graphics displayed in a grid arrangement to show a map.
A "tileset" typically includes enough tiles to form a very large image but generally the idea is not to show them all at once, but to display only a particular area of the map so the map can load faster.
The beaking down of a huge map to manageable pieces is like a pyramid of image tiles
![tilespyramid](/assets/images/tutorials/tilespyramid.jpeg)