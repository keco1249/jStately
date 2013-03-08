package com.coalmine.jstately.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.Transition;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;


/** Representation of a <a href="http://en.wikipedia.org/wiki/State_diagram">state graph</a>. */
public class StateGraph<TransitionInput> {
	protected State											startState			= null;
	protected Map<String, State>							statesByIdentifier;
	protected Multimap<State,Transition<TransitionInput>>	transitionsByTail;


	public State getStartState() {
		return startState;
	}
	public void setStartState(State startState) {
		this.startState = startState;
	}

	public Set<State> getStates() {
		return new HashSet<State>(statesByIdentifier.values());
	}
	public void setStates(Iterable<State> states) {
		statesByIdentifier = new HashMap<String, State>();
		for(State state : states) {
			statesByIdentifier.put(state.getIdentifier(), state);
		}
	}
	public void setStates(State... states) {
		statesByIdentifier = new HashMap<String, State>();
		for(State state : states) {
			statesByIdentifier.put(state.getIdentifier(), state);
		}
	}
	public void addState(State state) {
		if(statesByIdentifier==null) {
			statesByIdentifier = new HashMap<String, State>();
		}
		statesByIdentifier.put(state.getIdentifier(), state);
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

	public Set<Transition<TransitionInput>> getTransitionsFromTail(State tailState) {
		return new HashSet<Transition<TransitionInput>>(transitionsByTail.get(tailState));
	}

	public Set<State> getStatesFromTail(State tailState) {
		Set<State> validStates = new HashSet<State>();
		for(Transition<TransitionInput> transition : transitionsByTail.get(tailState)) {
			validStates.add(transition.getHead());
		}
		return validStates;
	}

	public Set<Transition<TransitionInput>> getValidTransitionsFromTail(State tailState, TransitionInput transitionInput) {
		Set<Transition<TransitionInput>> validTransitions = new HashSet<Transition<TransitionInput>>();
		for(Transition<TransitionInput> transition : transitionsByTail.get(tailState)) {
			if(transition.isValid(transitionInput)) {
				validTransitions.add(transition);
			}
		}
		return validTransitions;
	}

	public Set<State> getValidStatesFromTail(State tailState, TransitionInput transitionInput) {
		Set<State> validStates = new HashSet<State>();
		for(Transition<TransitionInput> transition : transitionsByTail.get(tailState)) {
			if(transition.isValid(transitionInput)) {
				validStates.add(transition.getHead());
			}
		}
		return validStates;
	}

	public Transition<TransitionInput> getFirstValidTransitionFromTail(State tailState, TransitionInput transitionInput) {
		for(Transition<TransitionInput> transition : transitionsByTail.get(tailState)) {
			if(transition.isValid(transitionInput)) {
				return transition;
			}
		}
		return null;
	}
}




