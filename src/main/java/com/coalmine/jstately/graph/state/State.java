package com.coalmine.jstately.graph.state;

import java.util.List;

import com.coalmine.jstately.graph.composite.CompositeState;

/** Base representation of a state.
 * @see FinalState */
public interface State<TransitionInput> {
	/** Implementors must always return a list. */
	List<CompositeState<TransitionInput>> getComposites();
	void addComposite(CompositeState<TransitionInput> composite);

	/** Called by a state machine when the machine enters the state. */
	void onEnter();

	/** Called by a state machine when the machine exits the state. */
	void onExit();
}


