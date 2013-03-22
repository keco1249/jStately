package com.coalmine.jstately.graph.transition;

import java.util.Set;

import com.coalmine.jstately.graph.state.State;


public class DisjunctiveEqualitySelfTransition<TransitionInput> extends DisjunctiveEqualityTransition<TransitionInput> {
	public DisjunctiveEqualitySelfTransition() { }

	public DisjunctiveEqualitySelfTransition(State<TransitionInput> state, Set<TransitionInput> validityTestObjects) {
		super(state, state, validityTestObjects);
	}

	public DisjunctiveEqualitySelfTransition(State<TransitionInput> state, TransitionInput... validityTestObjects) {
		super(state, state, validityTestObjects);
	}

	public DisjunctiveEqualitySelfTransition(State<TransitionInput> state, String description, Set<TransitionInput> validityTestObjects) {
		super(state, state, description, validityTestObjects);
	}

	public DisjunctiveEqualitySelfTransition(State<TransitionInput> state, String description, TransitionInput... validityTestObjects) {
		super(state, state, description, validityTestObjects);
	}
}


