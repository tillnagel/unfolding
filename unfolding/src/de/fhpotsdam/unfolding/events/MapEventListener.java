package de.fhpotsdam.unfolding.events;

import java.util.EventListener;

public interface MapEventListener extends EventListener {

	public String getId();

	public void onManipulation(MapEvent event);
	
}
