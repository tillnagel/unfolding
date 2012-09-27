---
layout: "page"
title: "Example: Debug Display"
description: "A widget with information about the map."
group: "examples"
thumbnail: "../assets/images/examples/debug-display-thumb.jpg"
gh_link: "https://github.com/tillnagel/unfolding/blob/master/examples/de/fhpotsdam/unfolding/examples/misc/MultiDebugDisplayApp.java"

---

{% include JB/setup %}

Displays various information about the map. It shows the current state of the map (e.g. zoom level), the position of the mouse pointer (e.g. geo-location), and some general information (e.g. the map tile provider).

In the lower area of the widget you see map events such as zoom and pan in real-time. This can be useful to debug more complex interaction connections between maps, e.g. for an Overview + Detail scenario.

This example uses DebugDisplays to show information about two independent maps from different providers.


![Maps with DebugDisplays](../assets/images/examples/debug-display.jpg)

