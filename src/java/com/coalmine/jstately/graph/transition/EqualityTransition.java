package com.coalmine.jstately.graph.transition;

import com.coalmine.jstately.graph.state.State;

/**
 * Basic implementation of Transition where the validity of the transition is determined by the
 * equality of <code>isValid()</code>'s argument and a given Object.
 */
public class EqualityTransition<TransitionInput> extends AbstractTransition<TransitionInput> implements Transition<TransitionInput> {
	private TransitionInput validityTestObject;


	public EqualityTransition() { }

	public EqualityTransition(State<TransitionInput> head, TransitionInput validityTestObject) {
		this.head				= head;
		this.validityTestObject	= validityTestObject;
	}


	public TransitionInput getValidityTestObject() {
		return validityTestObject;
	}
	public void setValidityTestObject(TransitionInput validityKey) {
		this.validityTestObject = validityKey;
	}

	public boolean isValid(TransitionInput input) {
		return validityTestObject==null?
				input==null :
				validityTestObject.equals(input);
	}
}


