---
layout: page
title: "Tutorials"
description: "This site will contain the tutorials"
group: navigation
---
{% include JB/setup %}

## Starter Tutorials
{% assign pages_teasers_template = "list" %}
{% assign pages_teasers = site.pages %}
{% assign group = "tutorials-starter" %}
{% include pages_teasers %}

## Beginner Tutorials
{% assign pages_teasers_template = "list" %}
{% assign pages_teasers = site.pages %}
{% assign group = "tutorials-beginner" %}
{% include pages_teasers %}

## Advanced Tutorials
{% assign pages_teasers_template = "list" %}
{% assign pages_teasers = site.pages %}
{% assign group = "tutorials-advanced" %}
{% include pages_teasers %}