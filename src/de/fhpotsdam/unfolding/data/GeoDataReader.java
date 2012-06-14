package de.fhpotsdam.unfolding.data;

import java.util.List;

import processing.core.PApplet;

/**
 * 
 * REVISIT Is not implemented in GeoRSSReader, GPXReader, etc, as the methods could not be static, then.
 * 
 */
public interface GeoDataReader {

	public List<Feature> loadData(PApplet p, String fileName);

}
