package com.coalmine.jstately.graph.state;

import com.coalmine.jstately.graph.composite.CompositeState;
import com.coalmine.jstately.util.EqualityUtil;

/**
 * Basic implementation of State with getters and setters for identifier, description and
 * acceptState. The onEnter() and onExit() methods do nothing and can be overridden as needed.
 */
public class DefaultState<TransitionInput> implements State<TransitionInput> {
	private String	identifier;
	private String	description;
	private CompositeState<TransitionInput> composite;


	public DefaultState() { }

	public DefaultState(String identifier) {
		this.identifier = identifier;
	}

	public DefaultState(String identifier, String description) {
		this.identifier		= identifier;
		this.description	= description;
	}


	public void onEnter() { }

	public void onExit() { }

	@Override
	public CompositeState<TransitionInput> getComposite() {
		return composite;
	}
	@Override
	public void setComposite(CompositeState<TransitionInput> section) {
		this.composite = section;
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

	public String toString() {
		return getClass().getName()+"[identifier="+getIdentifier()+"]";
	}

	@SuppressWarnings("rawtypes")
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




