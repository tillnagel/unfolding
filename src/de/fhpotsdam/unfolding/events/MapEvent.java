package de.fhpotsdam.unfolding.events;

import java.util.EventObject;

import de.fhpotsdam.unfolding.UnfoldingMap;

/**
 * A MapEvent stores various information about map events, and can be used to broadcast and listen to. The information
 * consist of the event type (such as zoom, pan, etc), a sub-type (such as zoomBy, zoomTo, etc), and the necessary data
 * to actually perform the map event (such as new zoom level value).
 * <p>
 * See sub classes for implementation details.
 * </p>
 */
public abstract class MapEvent extends EventObject {

	private String type;
	private String scopeId;
	private String subType;
	private boolean tweening = true;

	public MapEvent(Object source, String type, String scopeId) {
		super(source);
		this.type = type;
		this.scopeId = scopeId;
	}

	/**
	 * Returns the type of this event.
	 * 
	 * NB This is not an Enum to allow simple extension.
	 * 
	 * @return The id/name of this type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns the sub type of this event.
	 * 
	 * @return The name of this sub type.
	 */
	public String getSubType() {
		return subType;
	}

	/**
	 * Sets the sub type of this event.
	 * 
	 * @param subType
	 *            The name of this sub type.
	 */
	public void setSubType(String subType) {
		this.subType = subType;
	}

	/**
	 * Gets the scope of this event.
	 * 
	 * See {@link EventDispatcher#register(MapEventListener, String, String...)}.
	 * 
	 * @return The ID of the scope.
	 */
	public String getScopeId() {
		return scopeId;
	}

	public void setTweening(boolean tweening) {
		this.tweening = tweening;
	}

	public boolean isTweening() {
		return tweening;
	}

	public abstract void executeManipulationFor(UnfoldingMap map);

}
