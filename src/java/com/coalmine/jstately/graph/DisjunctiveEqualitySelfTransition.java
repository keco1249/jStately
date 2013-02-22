package com.coalmine.jstately.graph;

import java.util.Set;


public class DisjunctiveEqualitySelfTransition<TransitionInput> extends DisjunctiveEqualityTransition<TransitionInput> {
	public DisjunctiveEqualitySelfTransition() { }

	public DisjunctiveEqualitySelfTransition(State state, Set<TransitionInput> validityTestObjects) {
		super(state, state, validityTestObjects);
	}

	public DisjunctiveEqualitySelfTransition(State state, TransitionInput... validityTestObjects) {
		super(state, state, validityTestObjects);
	}

	public DisjunctiveEqualitySelfTransition(State state, String description, Set<TransitionInput> validityTestObjects) {
		super(state, state, description, validityTestObjects);
	}

	public DisjunctiveEqualitySelfTransition(State state, String description, TransitionInput... validityTestObjects) {
		super(state, state, description, validityTestObjects);
	}
}


