package com.coalmine.jstately.test;

import com.coalmine.jstately.graph.StateGraph;
import com.coalmine.jstately.graph.state.DefaultState;
import com.coalmine.jstately.graph.state.DefaultSubmachineState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.state.SubmachineState;

/** Defines a StaetGraph with two states, the second of which is a SubmachineState. */
public class TwoStateStateGraphWithSubmachineState extends StateGraph<Object> {
	private State<Object> firstState;
	private SubmachineState<Object> secondState; 

	public TwoStateStateGraphWithSubmachineState(StateGraph<Object> submachineStateGraph, String stateDescriptionPrefix) {
		firstState = new DefaultState<Object>(stateDescriptionPrefix+"/first");
		setStartState(firstState);

		secondState = new DefaultSubmachineState<Object>(stateDescriptionPrefix+"/second", submachineStateGraph);
		addTransition(firstState, new DefaultTransition<Object>(secondState));
	}

	public State<Object> getFirstState() {
	    return firstState;
    }

	public SubmachineState<Object> getSecondState() {
	    return secondState;
    }
}


