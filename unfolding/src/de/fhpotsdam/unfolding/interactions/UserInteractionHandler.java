package de.fhpotsdam.unfolding.interactions;

import java.util.Arrays;
import java.util.List;

import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.EventDispatcher;

public class UserInteractionHandler {

	List<Map> maps;
	EventDispatcher eventDispatcher;

	public UserInteractionHandler(EventDispatcher eventDispatcher, Map... maps) {
		this.eventDispatcher = eventDispatcher;
		this.maps = Arrays.asList(maps);
	}

	public UserInteractionHandler(EventDispatcher eventDispatcher, List<Map> maps) {
		this.eventDispatcher = eventDispatcher;
		this.maps = maps;
	}

}
