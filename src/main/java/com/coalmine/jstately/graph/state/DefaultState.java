package com.coalmine.jstately.graph.state;

import java.util.ArrayList;
import java.util.List;

import com.coalmine.jstately.graph.composite.CompositeState;

/** A basic implementation of State with a getter and setter for its description.
 * The onEnter() and onExit() methods do nothing and can be overridden as needed. */
public class DefaultState<TransitionInput> implements State<TransitionInput> {
	private String description;
	private List<CompositeState<TransitionInput>> composites = new ArrayList<CompositeState<TransitionInput>>();


	public DefaultState() { }

	public DefaultState(String description) {
		this.description = description;
	}


	public void onEnter() { }

	public void onExit() { }

	@Override
	public List<CompositeState<TransitionInput>> getComposites() {
		return composites;
	}

	@Override
	public void addComposite(CompositeState<TransitionInput> composite) {
		composites.add(composite);
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




