package com.coalmine.jstately.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.coalmine.jstately.graph.state.BaseState;
import com.coalmine.jstately.graph.state.NonFinalState;
import com.coalmine.jstately.graph.transition.Transition;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;


/** Representation of a <a href="http://en.wikipedia.org/wiki/State_diagram">state graph</a>. */
public class StateGraph<TransitionInput> {
	protected NonFinalState											startState;
	protected Map<String,BaseState>									statesByIdentifier;
	protected Multimap<NonFinalState,Transition<TransitionInput>>	transitionsByTail;


	public NonFinalState getStartState() {
		return startState;
	}
	public void setStartState(NonFinalState startState) {
		this.startState = startState;
	}

	public Set<BaseState> getStates() {
		return new HashSet<BaseState>(statesByIdentifier.values());
	}
	public void setStates(Iterable<BaseState> states) {
		statesByIdentifier = new HashMap<String,BaseState>();
		for(BaseState state : states) {
			statesByIdentifier.put(state.getIdentifier(), state);
		}
	}
	public void setStates(BaseState... states) {
		statesByIdentifier = new HashMap<String,BaseState>();
		for(BaseState state : states) {
			statesByIdentifier.put(state.getIdentifier(), state);
		}
	}
	public void addState(BaseState state) {
		if(statesByIdentifier==null) {
			statesByIdentifier = new HashMap<String,BaseState>();
		}
		statesByIdentifier.put(state.getIdentifier(), state);
	}

	/** Convenience method to add a state and immediately set it as the graph's start state. */
	public void addStartState(NonFinalState state) {
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

	public Set<Transition<TransitionInput>> getTransitionsFromTail(NonFinalState tailState) {
		return new HashSet<Transition<TransitionInput>>(transitionsByTail.get(tailState));
	}

	public Set<BaseState> getStatesFromTail(NonFinalState tailState) {
		Set<BaseState> validStates = new HashSet<BaseState>();
		for(Transition<TransitionInput> transition : transitionsByTail.get(tailState)) {
			validStates.add(transition.getHead());
		}
		return validStates;
	}

	public Set<Transition<TransitionInput>> getValidTransitionsFromTail(NonFinalState tailState, TransitionInput transitionInput) {
		Set<Transition<TransitionInput>> validTransitions = new HashSet<Transition<TransitionInput>>();
		for(Transition<TransitionInput> transition : transitionsByTail.get(tailState)) {
			if(transition.isValid(transitionInput)) {
				validTransitions.add(transition);
			}
		}
		return validTransitions;
	}

	public Set<BaseState> getValidStatesFromTail(NonFinalState tailState, TransitionInput transitionInput) {
		Set<BaseState> validStates = new HashSet<BaseState>();
		for(Transition<TransitionInput> transition : transitionsByTail.get(tailState)) {
			if(transition.isValid(transitionInput)) {
				validStates.add(transition.getHead());
			}
		}
		return validStates;
	}

	public Transition<TransitionInput> getFirstValidTransitionFromTail(NonFinalState tailState, TransitionInput transitionInput) {
		for(Transition<TransitionInput> transition : transitionsByTail.get(tailState)) {
			if(transition.isValid(transitionInput)) {
				return transition;
			}
		}
		return null;
	}
}




