package com.coalmine.jstately.graph.section;

import com.coalmine.jstately.graph.state.State;

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

	public void addSection(Section section) {
		section.setParent(this);
	}

	public void onEnter() { }

	public void onExit() { }
}


