package com.coalmine.jstately.graph.transition;

import com.coalmine.jstately.graph.state.State;

/** Basic implementation of Transition, the validity of which is determined by the equality of an input and a particular object. */
public class EqualityTransition<TransitionInput> extends AbstractTransition<TransitionInput> implements Transition<TransitionInput> {
	private TransitionInput validInput;


	public EqualityTransition() { }

	public EqualityTransition(State<TransitionInput> head, TransitionInput validInput) {
		this.head		= head;
		this.validInput	= validInput;
	}


	public TransitionInput getValidInput() {
		return validInput;
	}
	public void setValidInput(TransitionInput validInput) {
		this.validInput = validInput;
	}

	public boolean isValid(TransitionInput input) {
		return validInput==null?
				input==null :
				validInput.equals(input);
	}
}


