---
layout: page
title: "Exhibition"
description: "This Site will contain the exhibition"
group: navigation
---

{% include JB/setup %}

{% assign pages_teasers_template = "grid" %}
{% assign pages_teasers = site.pages %}
{% assign group = "exhibition" %}
{% include pages_teasers %}