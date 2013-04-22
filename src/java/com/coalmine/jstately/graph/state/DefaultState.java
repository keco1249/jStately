package com.coalmine.jstately.graph.state;

import com.coalmine.jstately.graph.composite.CompositeState;

/** A basic implementation of State with a getter and setter for its description.
 * The onEnter() and onExit() methods do nothing and can be overridden as needed. */
public class DefaultState<TransitionInput> implements State<TransitionInput> {
	private String	description;
	private CompositeState<TransitionInput> composite;


	public DefaultState() { }

	public DefaultState(String description) {
		this.description = description;
	}


	public void onEnter() { }

	public void onExit() { }

	@Override
	public CompositeState<TransitionInput> getComposite() {
		return composite;
	}
	@Override
	public void setComposite(CompositeState<TransitionInput> composite) {
		this.composite = composite;
	}


	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String toString() {
		return getClass().getName()+"[description="+getDescription()+"]";
	}
}




