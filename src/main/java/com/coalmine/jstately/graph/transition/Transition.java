package com.coalmine.jstately.graph.transition;

import com.coalmine.jstately.graph.state.State;

/** Defines a transition from one state to another. */
public interface Transition<TransitionInput> {
	/** @return State that transition transitions to. */
	State<TransitionInput> getHead();

	/** @param input Input from a state machine used to determine which state (if any) the machine can transition to.
	 *  @return Whether or not the transition is valid with the given input. */
	boolean isValid(TransitionInput input);

	/** Called by a state machine when transitioning.
	 * @param input Input that caused the transition. */
	void onTransition(TransitionInput input);
}


