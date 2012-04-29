package de.fhpotsdam.unfolding.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class EventDispatcher {

	public static final Logger log = Logger.getLogger(EventDispatcher.class);

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

	/**
	 * Registers an listener to an event type within one or more event scopes.
	 * 
	 * @param listener
	 *            The listener to register.
	 * @param type
	 * @param scopeIds
	 *            IDs of scopes to register for. If none is provided the scope of the listener is
	 *            used. Note, that if you provide some scopes the listener's scope is not added,
	 *            automatically.
	 */
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
				if (!scopedListeners.listeners.contains(listener)) {
					scopedListeners.listeners.add(listener);
				} else {
					log.info("Listener not registered anew: '" + listener.getId() + "' already listens to type '" + type
							+ "' in scopes '" + Arrays.toString(scopeIds) + "'");
				}
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

	public void register(List<MapEventListener> listeners, String type, String... scopeIds) {
		for (MapEventListener listener : listeners) {
			register(listener, type, scopeIds);
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
