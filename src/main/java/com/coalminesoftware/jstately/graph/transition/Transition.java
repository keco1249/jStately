package com.coalminesoftware.jstately.graph.transition;

import com.coalminesoftware.jstately.graph.StateGraph;
import com.coalminesoftware.jstately.graph.composite.CompositeState;
import com.coalminesoftware.jstately.graph.state.State;

/**
 * Defines a transition to a {@link State} from either another state, a {@link CompositeState}
 * or globally (from any state if another valid transition is not found).
 * 
 * @see StateGraph#addTransition(State, Transition)
 * @see StateGraph#addGlobalTransition(Transition)
 * @see CompositeState#addTransition(Transition) */
public interface Transition<TransitionInput> {
	/** @return State that transition transitions to. */
	State<TransitionInput> getHead();

	/** @param input Input from a state machine used to determine which state (if any) the machine can transition to.
	 *  @return Whether or not the transition is valid for the given input. */
	boolean isValid(TransitionInput input);

	/** Called by a state machine when transitioning.
	 * @param input Input that caused the transition. */
	void onTransition(TransitionInput input);
}


