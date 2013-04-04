package com.coalmine.jstately.graph.composite;

import java.util.HashSet;
import java.util.Set;

import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.Transition;


/** Defines a super state that wraps multiple child states, allowing API users to define behavior for when the group is
 * entered or exited.  Composite states can also have transitions, which can be traversed from any of the child states
 * if none of the child state's transitions are valid for a given input but the composite state's transition is. */
public class CompositeState<TransitionInput> {
	private CompositeState<TransitionInput> parent;
	private Set<Transition<TransitionInput>> transitions = new HashSet<Transition<TransitionInput>>();


	public void addComposite(CompositeState<TransitionInput> composite) {
		composite.setParent(this);
	}

	public void addState(State<TransitionInput> state) {
		state.setComposite(this);
	}

	public void addStates(State<TransitionInput>... states) {
		for(State<TransitionInput> state : states) {
			addState(state);
		}
	}

	public void addTransition(Transition<TransitionInput> transition) {
		transitions.add(transition);
	}

	public void addTransitions(Transition<TransitionInput>... transitions) {
		for(Transition<TransitionInput> transition : transitions) {
			this.transitions.add(transition);
		}
	}

	public Transition<TransitionInput> getFirstValidTransition(TransitionInput input) {
		for(Transition<TransitionInput> transition : transitions) {
			if(transition.isValid(input)) {
				return transition;
			}
		}

		return null;
	}

	public CompositeState<TransitionInput> getParent() {
		return parent;
	}
	public void setParent(CompositeState<TransitionInput> parent) {
		this.parent = parent;
	}

	public void onEnter() { }

	public void onExit() { }
}


