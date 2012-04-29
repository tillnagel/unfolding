package de.fhpotsdam.unfolding.events;

import java.util.Arrays;
import java.util.List;

import de.fhpotsdam.unfolding.Map;

public class MapEventBroadcaster {

	protected List<Map> maps;
	protected EventDispatcher eventDispatcher;

	public MapEventBroadcaster(Map... maps) {
		this(null, maps);
	}

	public MapEventBroadcaster(List<Map> maps) {
		this(null, maps);
	}

	public MapEventBroadcaster(EventDispatcher eventDispatcher, Map... maps) {
		this(eventDispatcher, Arrays.asList(maps));
	}

	public MapEventBroadcaster(EventDispatcher eventDispatcher, List<Map> maps) {
		this.eventDispatcher = eventDispatcher;
		this.maps = maps;
	}

	public void setEventDispatcher(EventDispatcher eventDispatcher) {
		this.eventDispatcher = eventDispatcher;
	}

}
