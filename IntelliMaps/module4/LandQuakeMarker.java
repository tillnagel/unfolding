package module4;

import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

/**
 * Implements a visual marker for land earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 *
 */
public class LandQuakeMarker extends EarthquakeMarker {

	public LandQuakeMarker(final PointFeature quake) {

		// calling EarthquakeMarker constructor
		super(quake);

		// setting field in earthquake marker
		isOnLand = true;
	}

	@Override
	public void drawEarthquake(final PGraphics pg, final float x, final float y) {
		// Draw a centered circle for land quakes
		// DO NOT set the fill color here. That will be set in the EarthquakeMarker
		// class to indicate the depth of the earthquake.
		// Simply draw a centered circle.

		// HINT: Notice the radius variable in the EarthquakeMarker class
		// and how it is set in the EarthquakeMarker constructor

		// TODO: Implement this method
		pg.ellipse(x, y, getMagnitude() * 2, getMagnitude() * 2);
	}

	// Get the country the earthquake is in
	public String getCountry() {
		return (String) getProperty("country");
	}

}