package com.coalmine.jstately.graph;

import com.coalmine.jstately.util.EqualityUtil;

/**
 * Basic implementation of State with getters and setters for identifier, description and
 * acceptState. The onEnter() and onExit() methods do nothing and can be overridden as needed.
 */
public class DefaultState implements State {
	private String	identifier;
	private String	description;
	private boolean	acceptState	= false;


	public DefaultState() { }

	public DefaultState(String identifier) {
		this.identifier = identifier;
	}

	public DefaultState(String identifier, boolean acceptState) {
		this.identifier		= identifier;
		this.acceptState	= acceptState;
	}

	public DefaultState(String identifier, String description) {
		this.identifier		= identifier;
		this.description	= description;
	}

	public DefaultState(String identifier, String description, boolean acceptState) {
		this.identifier		= identifier;
		this.description	= description;
		this.acceptState	= acceptState;
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

	public boolean isAcceptState() {
		return acceptState;
	}
	public void setAcceptState(boolean acceptState) {
		this.acceptState = acceptState;
	}

	public void onEnter() { }

	public void onExit() { }


	@Override
	public boolean equals(Object obj) {
		if(obj instanceof State) {
			return EqualityUtil.objectsAreEqual(identifier, ((State)obj).getIdentifier());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return identifier.hashCode();
	}
}




