package de.fhpotsdam.unfolding.events;

import java.util.EventObject;

import de.fhpotsdam.unfolding.UnfoldingMap;

public abstract class MapEvent extends EventObject {

	private static final long serialVersionUID = -181345946612208818L;

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

	public String getSubType() {
		return subType;
	}

	public String getScopeId() {
		return scopeId;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public void setTweening(boolean tweening) {
		this.tweening = tweening;
	}

	public boolean isTweening() {
		return tweening;
	}

	public abstract void executeManipulationFor(UnfoldingMap map);

}
