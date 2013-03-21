package com.coalmine.jstately.graph.transition;

import com.coalmine.jstately.graph.state.NonFinalState;

public class EqualitySelfTransition<TransitionInput> extends EqualityTransition<TransitionInput> {
	public EqualitySelfTransition() {
		super();
	}

	public EqualitySelfTransition(NonFinalState state, TransitionInput validityTestObject) {
		super(state,state,validityTestObject);
	}

	public EqualitySelfTransition(NonFinalState state, TransitionInput validityTestObject, String description) {
		super(state, state, validityTestObject, description);
	}
}


