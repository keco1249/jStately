package com.coalmine.jstately.graph.transition;

import java.util.Set;

import com.coalmine.jstately.graph.state.State;
import com.google.common.collect.Sets;

/** Transition implementation that is valid if any one of its <code>validInputs</code> values is equal to the transition input. */
public class DisjunctiveEqualityTransition<TransitionInput> extends AbstractTransition<TransitionInput> implements Transition<TransitionInput> {
	private Set<TransitionInput> validInputs;


	public DisjunctiveEqualityTransition() { }

	public DisjunctiveEqualityTransition(State<TransitionInput> head, Set<TransitionInput> validInputs) {
		this.head			= head;
		this.validInputs	= validInputs;
	}

	public DisjunctiveEqualityTransition(State<TransitionInput> head, TransitionInput... validInputs) {
		this(head, Sets.newHashSet(validInputs));
	}

	public Set<TransitionInput> getValidInputs() {
		return validInputs;
	}
	public void setValidInputs(Set<TransitionInput> validInputs) {
		this.validInputs = validInputs;
	}

	public boolean isValid(TransitionInput input) {
		return validInputs.contains(input);
	}
}


