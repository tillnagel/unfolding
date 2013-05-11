package de.fhpotsdam.unfolding.events;

import java.util.EventListener;

/**
 * Listens and reacts to map events.
 * 
 * Most important implementing class is the main UnfoldingMap, but your application or any other class can implement
 * this, too.
 */
public interface MapEventListener extends EventListener {

	public String getId();

	public void onManipulation(MapEvent event);

}
