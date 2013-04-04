package com.coalmine.jstately.graph.transition;

import java.util.Set;

import com.coalmine.jstately.graph.state.State;
import com.google.common.collect.Sets;

/** Transition implementation that is valid if any one of its <code>testValue</code>s is equal to the transition input. */
public class DisjunctiveEqualityTransition<TransitionInput> extends AbstractTransition<TransitionInput> implements Transition<TransitionInput> {
	private Set<TransitionInput> validityTestObjects;


	public DisjunctiveEqualityTransition() { }

	public DisjunctiveEqualityTransition(State<TransitionInput> head, Set<TransitionInput> validityTestObjects) {
		this.head					= head;
		this.validityTestObjects	= validityTestObjects;
	}

	public DisjunctiveEqualityTransition(State<TransitionInput> head, TransitionInput... validityTestObjects) {
		this(head, Sets.newHashSet(validityTestObjects));
	}

	public DisjunctiveEqualityTransition(State<TransitionInput> head, String description, Set<TransitionInput> validityTestObjects) {
		this.head					= head;
		this.validityTestObjects	= validityTestObjects;
		this.description			= description;
	}

	public DisjunctiveEqualityTransition(State<TransitionInput> tail, State<TransitionInput> head, String description, TransitionInput... validityTestObjects) {
		this(head, description, Sets.newHashSet(validityTestObjects));
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




