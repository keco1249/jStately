package com.coalminesoftware.jstately.test;

import com.coalminesoftware.jstately.graph.state.State;
import com.coalminesoftware.jstately.graph.transition.Transition;

/** Default no-op Transition implementation for testing purposes only, when you need a Transition
 * but the implementation isn't important. The transition's validity is set to a fixed value. */
public class DefaultTransition<TransitionInput> implements Transition<TransitionInput> {
	private boolean valid;
	private State<TransitionInput> head;


	public DefaultTransition() {
		this(true);
	}

	public DefaultTransition(boolean valid) {
		this.valid = valid;
	}

	public DefaultTransition(State<TransitionInput> head) {
		this();

		this.head = head;
	}

	@Override
	public State<TransitionInput> getHead() {
		return head;
	}

	@Override
	public boolean isValid(TransitionInput input) {
		return valid;
	}

	@Override
	public void onTransition(TransitionInput input) { }
}
