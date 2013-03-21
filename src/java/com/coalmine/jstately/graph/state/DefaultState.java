package com.coalmine.jstately.graph.state;

import com.coalmine.jstately.util.EqualityUtil;

/**
 * Basic implementation of State with getters and setters for identifier, description and
 * acceptState. The onEnter() and onExit() methods do nothing and can be overridden as needed.
 */
public class DefaultState implements NonFinalState {
	private String	identifier;
	private String	description;


	public DefaultState() { }

	public DefaultState(String identifier) {
		this.identifier = identifier;
	}

	public DefaultState(String identifier, String description) {
		this.identifier		= identifier;
		this.description	= description;
	}


	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public void onEnter() { }

	public void onExit() { }


	public String toString() {
		return getClass().getSimpleName()+"[identifier="+getIdentifier()+"]";
	}


	@Override
	public boolean equals(Object obj) {
		if(obj instanceof NonFinalState) {
			return EqualityUtil.objectsAreEqual(identifier, ((NonFinalState)obj).getIdentifier());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return identifier.hashCode();
	}
}




