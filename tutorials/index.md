---
layout: page
title: "Tutorials"
description: "This site will contain the tutorials"
group: navigation
---
{% include JB/setup %}

## Starter Tutorials
Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
{% assign pages_teasers_template = "list" %}
{% assign pages_teasers = site.pages %}
{% assign group = "tutorials-starter" %}
{% include pages_teasers %}

## Beginner Tutorials
Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
{% assign pages_teasers_template = "list" %}
{% assign pages_teasers = site.pages %}
{% assign group = "tutorials-beginner" %}
{% include pages_teasers %}

## Advanced Tutorials
Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
{% assign pages_teasers_template = "list" %}
{% assign pages_teasers = site.pages %}
{% assign group = "tutorials-advanced" %}
{% include pages_teasers %}