package com.coalmine.jstately.graph.state;

/** Representation of a state in a finite state machine. */
public interface State {
	/** @return A String that uniquely identifies a state in a machine. */
	String getIdentifier();

	/** @return Optional human readable description of the state. */
	String getDescription();

	/** @return Whether the machine is in a state marked as an accept state. */
	boolean isAcceptState();

	/** Called by a state machine when the machine enters the state. */
	void onEnter();

	/** Called by a state machine when the machine exits the state. */
	void onExit();
}


