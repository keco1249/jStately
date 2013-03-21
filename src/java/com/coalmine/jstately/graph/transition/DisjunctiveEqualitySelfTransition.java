package com.coalmine.jstately.graph.transition;

import java.util.Set;

import com.coalmine.jstately.graph.state.NonFinalState;


public class DisjunctiveEqualitySelfTransition<TransitionInput> extends DisjunctiveEqualityTransition<TransitionInput> {
	public DisjunctiveEqualitySelfTransition() { }

	public DisjunctiveEqualitySelfTransition(NonFinalState state, Set<TransitionInput> validityTestObjects) {
		super(state, state, validityTestObjects);
	}

	public DisjunctiveEqualitySelfTransition(NonFinalState state, TransitionInput... validityTestObjects) {
		super(state, state, validityTestObjects);
	}

	public DisjunctiveEqualitySelfTransition(NonFinalState state, String description, Set<TransitionInput> validityTestObjects) {
		super(state, state, description, validityTestObjects);
	}

	public DisjunctiveEqualitySelfTransition(NonFinalState state, String description, TransitionInput... validityTestObjects) {
		super(state, state, description, validityTestObjects);
	}
}


