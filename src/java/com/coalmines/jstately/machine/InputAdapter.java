package com.coalmines.jstately.machine;

/**
 * Used to allow a machine to accept an input of a type other than its graph's transition type.  For instance, perhaps
 * the most common academic example of a state machine is one that takes a string of characters as its input but
 * iterates over the those characters, transitioning on individual characters.
 * 
 * @param <MachineInput>
 * @param <TransitionInput>
 */
public interface InputAdapter<MachineInput,TransitionInput> {
	void queueInput(MachineInput machineInput);

	boolean hasNext();
	TransitionInput next();
	TransitionInput peek();
}


