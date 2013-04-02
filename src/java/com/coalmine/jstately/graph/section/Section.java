package com.coalmine.jstately.graph.section;

import com.coalmine.jstately.graph.state.State;


/** Analogous to a composite state or super state. */
public class Section {
	private Section parent;

	public Section getParent() {
		return parent;
	}
	public void setParent(Section parent) {
		this.parent = parent;
	}

	public void addState(State<?> state) {
		state.setSection(this);
	}

	public void addStates(State<?>... states) {
		for(State<?> state : states) {
			addState(state);
		}
	}

	public void addSection(Section section) {
		section.setParent(this);
	}

	public void onEnter() { }

	public void onExit() { }
}


