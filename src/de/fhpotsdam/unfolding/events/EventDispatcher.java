package de.fhpotsdam.unfolding.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import de.fhpotsdam.unfolding.interactions.MouseHandler;

/**
 * The event dispatcher is the core class for handling the event system.
 * <p>
 * In the simplest case all user input only affects a single UnfoldingMap. For instance, a user clicks and drags the
 * mouse, so that the map gets panned. In this case, the {@link MouseHandler}, a {@link MapEventBroadcaster}, reacts to
 * the user's mouse-dragging, and creates a PanMapEvent with the respective geo-location to pan to. That event is fired,
 * and send via the EventDispatcher to the only listener, the single map.
 * </p>
 * <p>
 * In other cases, applications can set up more sophisticated behaviors. For instance, in an overview+detail setting
 * with a large detail map and a small overview map, the small map shall only show an overview but not be interactive
 * itself. For this case, the MouseHandler is created for the large map, but with a scope of both maps. The
 * EventDispatcher is initiated with both maps listening to pan-events. Thus, any panning on the large overview map also
 * affects the small detail map.<br/>
 * See the OverviewAndDetailMapApp and the ComplexMapEventApp in the examples section for this.
 * </p>
 * <p>
 * Your application then can create EventDispatcher to connect broadcasters and listeners, and customize the event
 * handling in an application.
 * </p>
 * 
 * 
 * 
 */
public class EventDispatcher {

	public static final Logger log = Logger.getLogger(EventDispatcher.class);

	public java.util.Map<String, List<ScopedListeners>> typedScopedListeners;

	public EventDispatcher() {
		typedScopedListeners = new HashMap<String, List<ScopedListeners>>();
	}

	/**
	 * Fires a map event. Sends it to all listeners
	 * 
	 * @param event
	 */
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

	/**
	 * Adds broadcaster by setting this EventDispatcher in the MapEventDispatcher. (Convenience method).
	 * 
	 * @param broadcaster
	 *            The broadcaster to add.
	 */
	public void addBroadcaster(MapEventBroadcaster broadcaster) {
		broadcaster.setEventDispatcher(this);
	}

	/**
	 * Registers listener to an event type within one or more event scopes.
	 * 
	 * @param listener
	 *            The listener to register, e.g. a map.
	 * @param type
	 *            The type of the event, e.g. pan or zoom.
	 * @param scopeIds
	 *            IDs of scopes to register for. If none is provided the scope of the listener is used. Note, that if
	 *            you provide some scopes the listener's own scope is not added, automatically.
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

		// Adds listener to scope, either to existing or to new.
		boolean foundExistingScope = false;

		// Adds to existing ScopedListeners (if exist for scopeIds)
		List<String> scopeIdList = Arrays.asList(scopeIds);
		for (ScopedListeners scopedListeners : scopedListenersList) {
			if (scopedListeners.scopeIds.size() == scopeIdList.size()
					&& scopedListeners.scopeIds.containsAll(scopeIdList)) {
				if (!scopedListeners.listeners.contains(listener)) {
					scopedListeners.listeners.add(listener);
				} else {
					log.info("Listener not registered anew: '" + listener.getId() + "' already listens to type '"
							+ type + "' in scopes '" + Arrays.toString(scopeIds) + "'");
				}
				foundExistingScope = true;
			}
		}

		if (!foundExistingScope) {
			// Creates ScopedListeners for scopeIDs and register listener if none found
			ScopedListeners scopedListeners = new ScopedListeners();
			scopedListeners.scopeIds.addAll(scopeIdList);
			scopedListeners.listeners.add(listener);
			scopedListenersList.add(scopedListeners);
		}
	}

	/**
	 * Registers multiple listener for an event type within one or more event scopes.
	 * 
	 * @param listeners
	 *            The listeners to register, e.g. some maps.
	 * @param type
	 *            The type of the event, e.g. pan or zoom.
	 * @param scopeIds
	 *            IDs of scopes to register for. If none is provided the scope of the listener is used. Note, that if
	 *            you provide some scopes the listener's own scope is not added, automatically.
	 */
	public void register(List<MapEventListener> listeners, String type, String... scopeIds) {
		for (MapEventListener listener : listeners) {
			register(listener, type, scopeIds);
		}
	}

	/**
	 * Removes listener if ScopedListeners exist for scopeIds to unregister.
	 * 
	 * @param listener
	 *            The listener to unregister, e.g. a maps.
	 * @param type
	 *            The type of the event, e.g. pan or zoom.
	 * @param scopeIds
	 *            IDs of scopes to unregister for.
	 */
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
