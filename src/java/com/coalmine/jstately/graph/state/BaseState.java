package com.coalmine.jstately.graph.state;

/** Base representation of a state without methods that are specific to a final or non-final state.
 * @see FinalState
 * @see NonFinalState */
public interface BaseState {
	/** @return A String that uniquely identifies a state in a machine. */
	String getIdentifier();

	/** @return Optional human readable description of the state. */
	String getDescription();

	/** Called by a state machine when the machine enters the state. */
	void onEnter();
}


