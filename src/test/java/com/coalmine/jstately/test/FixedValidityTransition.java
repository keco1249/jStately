package com.coalmine.jstately.test;

import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.Transition;


/** Default no-op Transition implementation for testing purposes only, when
 * you need a Transition reference but the implementation isn't important. */
public class FixedValidityTransition<TransitionInput> implements Transition<TransitionInput> {
	private boolean valid;

	public FixedValidityTransition() {
		valid = false;
    }

	public FixedValidityTransition(boolean valid) {
		this.valid = valid;
    }

	@Override
    public State<TransitionInput> getHead() {
	    return null;
    }

	@Override
    public boolean isValid(TransitionInput input) {
	    return valid;
    }

	@Override
    public void onTransition(TransitionInput input) { }
}


