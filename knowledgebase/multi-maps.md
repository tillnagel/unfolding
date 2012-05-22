---
layout: page
title: Managing multiple maps
description: How to create and use multiple maps
group: knowledgebase
finalimage: http://placehold.it/610x390&text=Bild+2+610x390
finalsrc: #someurlatgithub
finalsrc: #someurlatgithub
---
{% include JB/setup %}

Description: How to create and manage multiple maps in one application.

Target audience: Everybody who wants to use various dependent or independent maps.

Prerequisites: First experience with Unfolding. Read "Unfolding in 5 Minutes for an introduction . (Internal links do not work? See https://github.com/mojombo/jekyll/pull/369 )

## Two Maps
Drawing two maps in one application is very simple. You just need to create two maps, and place them besides each other.

	map1 = new Map(this, "map1", 0, 0, 295, 300);
	map2 = new Map(this, "map2", 305, 0, 295, 300);
	
Now, just draw them both in the `draw` method.

	map1.draw();
	map2.draw();
	
That's it, basically.

![Two maps beside each other](/assets/images/multimap-simple.png)


## Handling interactions for multiple maps
If you add the default interaction methods, both maps can be panned and zoomed independently. Direct interactions (such as mouse, or finger touch) work out of the box.

	MapUtils.createDefaultEventDispatcher(this, map1, map2);

### Special Case: Indirect interactions

But what happens if you want to allow using indirect interactions, such as keyboard or, say, an external rotary knob? How does Unfolding know which map should react to which input? It does not. So it is up to you to create some switching mechanism.

As an example, let us use the mouse to select the active map.

	public void keyPressed() {
		Map activeMap = null;
		if (map1.isHit(mouseX, mouseY)) {
			activeMap = map1;
		} else if (map2.isHit(mouseX, mouseY)) {
			activeMap = map2;
		}

		if (activeMap != null) {
			if (key == '+') {
				activeMap.zoomLevelIn();
			}
			if (key == '-') {
				activeMap.zoomLevelOut();
			}
		}
	}
	
First, we check which map was hit, i.e. over which map the mouse pointer was when the user pressed a key. Then, that map reacts to the plus and minus keys in zooming in and out.