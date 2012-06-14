package de.fhpotsdam.unfolding.data;

/**
 * 
 * Architecture issue: For ease of use, no common class/interface, so that all data reader implement methods as static.
 * 
 * Is not implemented in GeoRSSReader, GPXReader, etc, as the methods could not be static, then.
 * 
 */
public interface GeoDataReader {

	// public List<Feature> loadData(PApplet p, String fileName);

}
