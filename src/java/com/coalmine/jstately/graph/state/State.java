package com.coalmine.jstately.graph.state;

import com.coalmine.jstately.graph.composite.CompositeState;

/** Base representation of a state.
 * @see FinalState */
public interface State<TransitionInput> {
	/** @return A String that uniquely identifies a state in a machine. */
	String getIdentifier();

	CompositeState<TransitionInput> getComposite();
	void setComposite(CompositeState<TransitionInput> composite);

	/** Called by a state machine when the machine enters the state. */
	void onEnter();

	/** Called by a state machine when the machine exits the state. */
	void onExit();
}


