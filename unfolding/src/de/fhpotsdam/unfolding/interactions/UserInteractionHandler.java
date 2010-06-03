package de.fhpotsdam.unfolding.interactions;

import java.util.List;

import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.EventDispatcher;

public class UserInteractionHandler {

	List<Map> maps;
	EventDispatcher eventDispatcher;

	public UserInteractionHandler(List<Map> maps, EventDispatcher eventDispatcher) {
		this.maps = maps;
		this.eventDispatcher = eventDispatcher;
	}

}
