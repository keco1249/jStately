package com.coalmine.jstately.graph;

import java.util.HashSet;
import java.util.Set;

import com.coalmine.jstately.graph.composite.CompositeState;
import com.coalmine.jstately.graph.state.FinalState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.Transition;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;


/** Representation of a state graph/diagram. */
public class StateGraph<TransitionInput> {
	private State<TransitionInput>											startState;
	private Multimap<State<TransitionInput>,Transition<TransitionInput>>	transitionsByTail	= HashMultimap.create();


	public State<TransitionInput> getStartState() {
		return startState;
	}
	public void setStartState(State<TransitionInput> startState) {
		if(startState instanceof FinalState) {
			throw new IllegalArgumentException("A graph's start state cannot be a final state.");
		}
		this.startState = startState;
	}

	public Set<Transition<TransitionInput>> getTransitions() {
		return new HashSet<Transition<TransitionInput>>(transitionsByTail.values());
	}

	public void addTransition(State<TransitionInput> transitionTail, Transition<TransitionInput> transition) {
		if(transitionTail==null) {
			throw new IllegalArgumentException("Tail cannot be be null.");
		}
		transitionsByTail.put(transitionTail,transition);
	}

	public void addSelfTransition(Transition<TransitionInput> transition) {
		if(transition.getHead()==null) {
			throw new IllegalArgumentException("A Transition's head/tail cannot be be null.");
		}
		transitionsByTail.put(transition.getHead(),transition);
	}

	/** Adds a Transitions that maybe be evaluated (and traversed if valid) as a last resort, if no valid Transition is found for the given input from the current State or an ancestor CompositeState. */
	public void addGlobalTransition(Transition<TransitionInput> transition) {
		transitionsByTail.put(null, transition); // Store "global" transitions under null
	}

	/** Gets all of the graph's global Transitions as a Set. */
	public Set<Transition<TransitionInput>> getGlobalTransitions() {
		return new HashSet<Transition<TransitionInput>>(transitionsByTail.get(null));
	}

	/** @return All of the Transitions (valid or not) for the given State */
	public Set<Transition<TransitionInput>> findAllTransitionsFromState(State<TransitionInput> tailState) {
		Set<Transition<TransitionInput>> transitions = new HashSet<Transition<TransitionInput>>(transitionsByTail.get(tailState));

		CompositeState<TransitionInput> composite = tailState.getComposite();
		while(composite != null) {
			transitions.addAll(composite.getTransitions());
			composite = composite.getParent();
		}

		transitions.addAll(transitionsByTail.get(null));

		return transitions;
	}

	public Set<Transition<TransitionInput>> findValidTransitionsFromState(State<TransitionInput> tailState, TransitionInput input) {
		Set<Transition<TransitionInput>> validTransitions = new HashSet<Transition<TransitionInput>>();

		for(Transition<TransitionInput> transition : transitionsByTail.get(tailState)) {
			if(transition.isValid(input)) {
				validTransitions.add(transition);
			}
		}

		CompositeState<TransitionInput> composite = tailState.getComposite();
		while(composite != null) {
			validTransitions.addAll(composite.findValidTransitions(input));
			composite = composite.getParent();
		}

		for(Transition<TransitionInput> transition : transitionsByTail.get(null)) {
			if(transition.isValid(input)) {
				validTransitions.add(transition);
			}
		}

		return validTransitions;
	}

	public Transition<TransitionInput> findFirstValidTransitionFromState(State<TransitionInput> tailState, TransitionInput input) {
		for(Transition<TransitionInput> transition : transitionsByTail.get(tailState)) {
			if(transition.isValid(input)) {
				return transition;
			}
		}

		CompositeState<TransitionInput> composite = tailState.getComposite();
		while(composite != null) {
			Transition<TransitionInput> transition = composite.findFirstValidTransition(input);
			if(transition != null) {
				return transition;
			}

			composite = composite.getParent();
		}

		for(Transition<TransitionInput> transition : transitionsByTail.get(null)) {
			if(transition.isValid(input)) {
				return transition;
			}
		}

		return null;
	}
}




