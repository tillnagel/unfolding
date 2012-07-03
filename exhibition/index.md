---
layout: page
title: "Exhibition"
description: "This Site will contain the exhibition"
group: navigation
video_src: "http://video-js.zencoder.com/oceans-clip.mp4"
video_img_src: "http://video-js.zencoder.com/oceans-clip.jpg"
video_caption: "foo bar"
---

{% include JB/setup %}

{% assign pages_teasers_template = "grid" %}
{% assign pages_teasers = site.pages %}
{% assign group = "exhibition" %}
{% include pages_teasers %}