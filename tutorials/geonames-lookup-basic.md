---
layout: page
title: Geonames Basic Lookup
description: Setup An Eclipse Project with Geonames Library
group: tutorials-internal
author: Markus Kerschkewicz
thumbnail: http://placehold.it/330x250
finalimage: http://placehold.it/610x390&text=Bild+2+610x390
finalsrc: #someurlatgithub
---

{% include JB/setup %}

## Instruction: Things You Need To Prepare
This tutorial explains how to setup a eclipse project with the geonames library in combination with unfolding. The geonames library gives you the possibility to lookup the coordinates (latitude and longitude) of a certain geo-location just by the name.
First of all you need to prepare some things to follow this tutorial

*  Create an ecplise project with working unfolding library

*  download the geonames.org java library http://www.geonames.org/source-code/ 

*  geonames library also requires jdom to parse the xml web service result

*  create a free account on geonames.org to use the java library features http://www.geonames.org/login

## Step 1: Import Geonames Library
![Here comes the alt text](http://placehold.it/620x390&text=Some+Image "Here comes the title for the image")
*Lorem ipsum: dolor sit amet, consectetur [adipisicing](http://example.com/ "Link title for adipisicing") elit.*

In the first step we need to import the geonames library and setup the WebService with our username.


###### in **geodata/GeoNamesBasicLookup.java**
	
	import org.geonames.*;                                                                                                                                  

    void setup() {
	  size(800,600);

	  WebService.setUserName("username"); // add your username here                                                                                  
    }

## Step 2: Make A Basic/Static Lookup
So now we are really starting to use the library. First thing we want to do is looking up all results for a basic string. In our example we will use "berlin" as the searchCriteria. For this lookup we already need a try/catch - statement do handle exceptions (more infos http://processing.org/reference/try.html).

###### in **geodata/GeoNamesBasicLookup.java**
	
	import org.geonames.*;    
	
	String searchName = "berlin"; // the string we want to lookup in geonames database
	ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria(); // the object we need for our search
	                                                                                                                           

    void setup() {
	  size(800,600);

	  WebService.setUserName("username"); // add your username here    
	     
	  searchCriteria.setQ(searchName); // setup the main search term, in our case "berlin"
	
			if (searchEvent == true) {
				try {
					ToponymSearchResult searchResult = WebService.search(searchCriteria); // a toponym search result as returned by the geonames webservice.

					for (Toponym toponym : searchResult.getToponyms()) {
						println(toponym.getName() + " " + toponym.getCountryName()
								+ " " + toponym.getLongitude() + " "
								+ toponym.getLatitude()); // prints the search results. We have access on certain get-Functions. In our Case the Name, Country, Longitude and Latitude
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}                                                                         
    }

	The code now prints out all the search results we get by our searchCriteria. 

### Specify The Max Rows Of Search Results
The Geonames Java Library already brings some interesting functions with it. One important function you will need for sure is the specification of the maximum amount of rows you want to get as a search result.

	  …
	  searchCriteria.setQ(searchName); // setup the main search term, in our case "berlin"
	  searchCriteria.setMaxRows(1); // setup the maximum amount of rows for your search results
	  …

### Switch Between SearchName
Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.

