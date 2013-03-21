package com.coalmine.jstately.graph.transition;

import java.util.Set;

import com.coalmine.jstately.graph.state.NonFinalState;
import com.google.common.collect.Sets;

/** Transition implementation that is valid if any one of its <code>testValue</code>s is equal to the transition input. */
public class DisjunctiveEqualityTransition<TransitionInput> extends AbstractTransition<TransitionInput> implements Transition<TransitionInput> {
	private Set<TransitionInput> validityTestObjects;


	public DisjunctiveEqualityTransition() { }

	public DisjunctiveEqualityTransition(NonFinalState tail, NonFinalState head, Set<TransitionInput> validityTestObjects) {
		this.tail					= tail;
		this.head					= head;
		this.validityTestObjects	= validityTestObjects;
	}

	public DisjunctiveEqualityTransition(NonFinalState tail, NonFinalState head, TransitionInput... validityTestObjects) {
		this(tail, head, Sets.newHashSet(validityTestObjects));
	}

	public DisjunctiveEqualityTransition(NonFinalState tail, NonFinalState head, String description, Set<TransitionInput> validityTestObjects) {
		this.tail					= tail;
		this.head					= head;
		this.validityTestObjects	= validityTestObjects;
		this.description			= description;
	}

	public DisjunctiveEqualityTransition(NonFinalState tail, NonFinalState head, String description, TransitionInput... validityTestObjects) {
		this(tail, head, description, Sets.newHashSet(validityTestObjects));
	}

	public Set<TransitionInput> getValidityTestObjects() {
		return validityTestObjects;
	}
	public void setValidityTestObjects(Set<TransitionInput> validityTestObjects) {
		this.validityTestObjects = validityTestObjects;
	}

	public boolean isValid(TransitionInput input) {
		return validityTestObjects.contains(input);
	}
}




