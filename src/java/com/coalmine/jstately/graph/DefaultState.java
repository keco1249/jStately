package com.coalmine.jstately.graph;

import java.util.Arrays;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

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
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(Object obj) {
		// Ignore everything but identifier when comparing
		return EqualsBuilder.reflectionEquals(this, obj, Arrays.asList("description","acceptState"));
	}

	@Override
	public int hashCode() {
		return identifier.hashCode();
	}
}




