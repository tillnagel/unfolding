package de.fhpotsdam.unfolding.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class EventDispatcher {

	public java.util.Map<String, List<ScopedListeners>> typedScopedListeners;

	public EventDispatcher() {
		typedScopedListeners = new HashMap<String, List<ScopedListeners>>();
	}

	public void fireMapEvent(MapEvent event) {
		String type = event.getType();
		List<ScopedListeners> scopedListenersList = typedScopedListeners.get(type);
		if (scopedListenersList != null) {
			for (ScopedListeners scopedListeners : scopedListenersList) {
				if (scopedListeners.isInScope(event)) {
					for (MapEventListener listener : scopedListeners.listeners) {
						listener.onManipulation(event);
					}
				}
			}
		}
	}

	public void addBroadcaster(MapEventBroadcaster broadcaster) {
		broadcaster.setEventDispatcher(this);
	}

	public void register(MapEventListener listener, String type, String... scopeIds) {
		List<ScopedListeners> scopedListenersList = typedScopedListeners.get(type);

		if (scopedListenersList == null) {
			// Creates scopedListenersList and adds to typed list.
			scopedListenersList = new ArrayList<ScopedListeners>();
			typedScopedListeners.put(type, scopedListenersList);
		}

		// Registers to itself: Default scope if no scope was defined
		if (scopeIds.length == 0) {
			scopeIds = new String[] { listener.getId() };
		}

		boolean foundExistingScope = false;

		// Adds listener if ScopedListeners exist for scopeIds to register
		List<String> scopeIdList = Arrays.asList(scopeIds);
		for (ScopedListeners scopedListeners : scopedListenersList) {
			if (scopedListeners.scopeIds.size() == scopeIdList.size()
					&& scopedListeners.scopeIds.containsAll(scopeIdList)) {
				scopedListeners.listeners.add(listener);
				foundExistingScope = true;
			}
		}

		// Creates ScopedListeners for scopeIDs and register listener if none found
		if (!foundExistingScope) {
			ScopedListeners scopedListeners = new ScopedListeners();
			scopedListeners.scopeIds.addAll(scopeIdList);
			scopedListeners.listeners.add(listener);
			scopedListenersList.add(scopedListeners);
		}
	}

	public void unregister(MapEventListener listener, String type, String... scopeIds) {
		List<ScopedListeners> scopedListenersList = typedScopedListeners.get(type);
		if (scopedListenersList == null) {
			return;
		}

		// Removes listener if ScopedListeners exist for scopeIds to unregister
		List<String> scopeIdList = Arrays.asList(scopeIds);
		for (ScopedListeners scopedListeners : scopedListenersList) {
			if (scopedListeners.scopeIds.size() == scopeIdList.size()
					&& scopedListeners.scopeIds.containsAll(scopeIdList)) {
				scopedListeners.listeners.remove(listener);
			}
		}
	}

}
