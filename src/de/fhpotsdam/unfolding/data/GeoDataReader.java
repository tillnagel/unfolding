package de.fhpotsdam.unfolding.data;

import java.util.List;

import processing.core.PApplet;

/**
 * A common GeoDataReader to parse geo-spatial data files and create Features.
 * 
 * Is implemented in GeoRSSReader, GPXReader, etc, and in all custom geo data readers.
 * Architecture issue: For ease of use, implemented as static method.
 */
public abstract class GeoDataReader {

	public static List<Feature> loadData(PApplet p, String fileName) {
		return null;
	}

}
