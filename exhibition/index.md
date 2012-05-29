---
layout: page
title: "Exhibition"
description: "This Site will contain the exhibition"
group: navigation
---

{% include JB/setup %}

### test
Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.

{% assign pages_teasers_template = "grid" %}
{% assign pages_teasers = site.pages %}
{% assign group = "exhibition" %}
{% include pages_teasers %}