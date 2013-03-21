package com.coalmine.jstately.graph.state;

/** A non-final state.  
 * @see FinalState*/
public interface NonFinalState extends BaseState {
	/** Called by a state machine when the machine exits the state. */
	void onExit();
}


