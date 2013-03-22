package com.coalmine.jstately.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.coalmine.jstately.graph.state.FinalState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.Transition;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;


/** Representation of a <a href="http://en.wikipedia.org/wiki/State_diagram">state graph</a>. */
public class StateGraph<TransitionInput> {
	protected State<TransitionInput>										startState;
	protected Map<String,State<TransitionInput>>							statesByIdentifier;
	protected Multimap<State<TransitionInput>,Transition<TransitionInput>>	transitionsByTail;


	public State<TransitionInput> getStartState() {
		return startState;
	}
	public void setStartState(State<TransitionInput> startState) {
		if(startState instanceof FinalState) {
			throw new IllegalArgumentException("A graph's start state cannot be a final state.");
		}
		this.startState = startState;
	}

	public Set<State<TransitionInput>> getStates() {
		return new HashSet<State<TransitionInput>>(statesByIdentifier.values());
	}
	public void setStates(Iterable<State<TransitionInput>> states) {
		statesByIdentifier = new HashMap<String,State<TransitionInput>>();
		for(State<TransitionInput> state : states) {
			statesByIdentifier.put(state.getIdentifier(), state);
		}
	}
	public void setStates(State<TransitionInput>... states) {
		statesByIdentifier = new HashMap<String,State<TransitionInput>>();
		for(State<TransitionInput> state : states) {
			statesByIdentifier.put(state.getIdentifier(), state);
		}
	}
	public void addState(State<TransitionInput> state) {
		if(statesByIdentifier==null) {
			statesByIdentifier = new HashMap<String,State<TransitionInput>>();
		}
		statesByIdentifier.put(state.getIdentifier(), state);
	}

	/** Convenience method to add a state and immediately set it as the graph's start state. */
	public void addStartState(State<TransitionInput> state) {
		addState(state);
		setStartState(state);
	}

	public Set<Transition<TransitionInput>> getTransitions() {
		return new HashSet<Transition<TransitionInput>>(transitionsByTail.values());
	}
	public void setTransitions(Iterable<Transition<TransitionInput>> transitions) {
		transitionsByTail = HashMultimap.create(); 
		for(Transition<TransitionInput> transition : transitions) {
			transitionsByTail.put(transition.getTail(), transition);
		}
	}
	public void setTransitions(Transition<TransitionInput>... transitions) {
		transitionsByTail = HashMultimap.create(); 
		for(Transition<TransitionInput> transition : transitions) {
			transitionsByTail.put(transition.getTail(), transition);
		}
	}
	public void addTransition(Transition<TransitionInput> transition) {
		if(transitionsByTail==null) {
			transitionsByTail = HashMultimap.create();
		}
		transitionsByTail.put(transition.getTail(),transition);
	}

	public Set<Transition<TransitionInput>> getTransitionsFromTail(State<TransitionInput> tailState) {
		return new HashSet<Transition<TransitionInput>>(transitionsByTail.get(tailState));
	}

	public Set<State<TransitionInput>> getStatesFromTail(State<TransitionInput> tailState) {
		Set<State<TransitionInput>> validStates = new HashSet<State<TransitionInput>>();
		for(Transition<TransitionInput> transition : transitionsByTail.get(tailState)) {
			validStates.add(transition.getHead());
		}
		return validStates;
	}

	public Set<Transition<TransitionInput>> getValidTransitionsFromTail(State<TransitionInput> tailState, TransitionInput transitionInput) {
		Set<Transition<TransitionInput>> validTransitions = new HashSet<Transition<TransitionInput>>();
		for(Transition<TransitionInput> transition : transitionsByTail.get(tailState)) {
			if(transition.isValid(transitionInput)) {
				validTransitions.add(transition);
			}
		}
		return validTransitions;
	}

	public Set<State<TransitionInput>> getValidStatesFromTail(State<TransitionInput> tailState, TransitionInput transitionInput) {
		Set<State<TransitionInput>> validStates = new HashSet<State<TransitionInput>>();
		for(Transition<TransitionInput> transition : transitionsByTail.get(tailState)) {
			if(transition.isValid(transitionInput)) {
				validStates.add(transition.getHead());
			}
		}
		return validStates;
	}

	public Transition<TransitionInput> getFirstValidTransitionFromTail(State<TransitionInput> tailState, TransitionInput transitionInput) {
		for(Transition<TransitionInput> transition : transitionsByTail.get(tailState)) {
			if(transition.isValid(transitionInput)) {
				return transition;
			}
		}
		return null;
	}
}




