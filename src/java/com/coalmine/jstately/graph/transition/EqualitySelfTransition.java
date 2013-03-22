package com.coalmine.jstately.graph.transition;

import com.coalmine.jstately.graph.state.State;

public class EqualitySelfTransition<TransitionInput> extends EqualityTransition<TransitionInput> {
	public EqualitySelfTransition() {
		super();
	}

	public EqualitySelfTransition(State<TransitionInput> state, TransitionInput validityTestObject) {
		super(state,state,validityTestObject);
	}

	public EqualitySelfTransition(State<TransitionInput> state, TransitionInput validityTestObject, String description) {
		super(state, state, validityTestObject, description);
	}
}


