package de.fhpotsdam.unfolding.events;

import java.util.Arrays;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;

public class MapEventBroadcaster {

	protected List<UnfoldingMap> maps;
	protected EventDispatcher eventDispatcher;

	public MapEventBroadcaster(UnfoldingMap... maps) {
		this(null, maps);
	}

	public MapEventBroadcaster(List<UnfoldingMap> maps) {
		this(null, maps);
	}

	public MapEventBroadcaster(EventDispatcher eventDispatcher, UnfoldingMap... maps) {
		this(eventDispatcher, Arrays.asList(maps));
	}

	public MapEventBroadcaster(EventDispatcher eventDispatcher, List<UnfoldingMap> maps) {
		this.eventDispatcher = eventDispatcher;
		this.maps = maps;
	}

	public void setEventDispatcher(EventDispatcher eventDispatcher) {
		this.eventDispatcher = eventDispatcher;
	}

}
