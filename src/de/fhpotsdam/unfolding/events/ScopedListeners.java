package de.fhpotsdam.unfolding.events;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScopedListeners {

	public Set<String> scopeIds;
	public List<MapEventListener> listeners;

	public ScopedListeners() {
		scopeIds = new HashSet<String>();
		listeners = new ArrayList<MapEventListener>();
	}

	public boolean isInScope(MapEvent event) {
		return scopeIds.contains(event.getScopeId());
	}

}
