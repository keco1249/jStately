package com.coalmine.jstately.graph;

import java.util.Set;

import com.google.common.collect.Sets;

/** Transition implementation that is valid if any one of its <code>testValue</code>s is equal to the transition input. */
public class DisjunctiveEqualityTransition<TransitionInput> extends AbstractTransition<TransitionInput> implements Transition<TransitionInput> {
	private Set<TransitionInput> validityTestObjects;


	public DisjunctiveEqualityTransition() { }

	public DisjunctiveEqualityTransition(State tail, State head, Set<TransitionInput> validityTestObjects) {
		this.tail					= tail;
		this.head					= head;
		this.validityTestObjects	= validityTestObjects;
	}

	public DisjunctiveEqualityTransition(State tail, State head, TransitionInput... validityTestObjects) {
		this(tail, head, Sets.newHashSet(validityTestObjects));
	}

	public DisjunctiveEqualityTransition(State tail, State head, String description, Set<TransitionInput> validityTestObjects) {
		this.tail					= tail;
		this.head					= head;
		this.validityTestObjects	= validityTestObjects;
		this.description			= description;
	}

	public DisjunctiveEqualityTransition(State tail, State head, String description, TransitionInput... validityTestObjects) {
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


	/** Convenience method for creating a transition to and from the same state. */
	public static <TransitionInput> DisjunctiveEqualityTransition<TransitionInput> selfTransition(State state, TransitionInput... testObjects) {
		return new DisjunctiveEqualityTransition<TransitionInput>(state, state, testObjects);
	}
}




