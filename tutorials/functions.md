---
layout: page
title: Functions
description: This site I just created for temporary use. It shows some of the functions.
group: tutorials-internal
author: Florian Dornberger
thumbnail: http://placehold.it/330x250
---

## Include a video
Example code to embedd a video:
{%raw%}
    {% assign video_src = "http://video-js.zencoder.com/oceans-clip.mp4" %}
    {% assign video_img_src = "http://video-js.zencoder.com/oceans-clip.jpg" %}
    {% include video %}
{%endraw%}

Result:
{% assign video_src = "http://video-js.zencoder.com/oceans-clip.mp4" %}
{% assign video_img_src = "http://video-js.zencoder.com/oceans-clip.jpg" %}
{% include video %}

### If you want u can specify following parameters as well.
{%raw%}
    {% assign video_id = "video-1" %}
{%endraw%}
Sets the id of a the video and have to be set if you want to display multiple videos

{%raw%}
    {% assign video_width = "620" %}
{%endraw%}
Sets the width of the video.

{%raw%}
    {% assign video_height = "390" %}
{%endraw%}
Sets the height of the video.

Sets the id for a video if u want to use multiple videos at once.

{%raw%}
    {% assign video_controls = "true" %}
{%endraw%}
Shows the video controls on true and hides them on false

{%raw%}
    {% assign video_loop = "true" %}
{%endraw%}
Repeats the video after playing if set to true.


## Include a list of teasers
Example code to embedd a list of teasers:
{%raw%}
    {% assign pages_teasers_template = "list" %}
    {% assign pages_teasers = site.pages %}
    {% assign group = "tutorials-starter" %}
    {% include pages_teasers %}
{%endraw%}
Most important here is the 3rd line, here we set wich group of pages we want to display.

Result:
{% assign pages_teasers_template = "list" %}
{% assign pages_teasers = site.pages %}
{% assign group = "tutorials-starter" %}
{% include pages_teasers %}

### If you want u can specify a Template in the first line:
{%raw%}
    {% assign pages_teasers_template = "list" %}
{%endraw%}
This displays the teasers as seen on tutorials

{%raw%}
    {% assign pages_teasers_template = "grid" %}
{%endraw%}
This displays the teasers as seen on exhibition
