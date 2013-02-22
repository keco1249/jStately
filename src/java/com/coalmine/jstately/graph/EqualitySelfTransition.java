package com.coalmine.jstately.graph;

public class EqualitySelfTransition<TransitionInput> extends EqualityTransition<TransitionInput> {
	public EqualitySelfTransition() {
		super();
	}

	public EqualitySelfTransition(State state, TransitionInput validityTestObject) {
		super(state,state,validityTestObject);
	}

	public EqualitySelfTransition(State state, TransitionInput validityTestObject, String description) {
		super(state, state, validityTestObject, description);
	}
}


