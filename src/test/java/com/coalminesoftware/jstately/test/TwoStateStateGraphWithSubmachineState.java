package com.coalminesoftware.jstately.test;

import com.coalminesoftware.jstately.graph.StateGraph;
import com.coalminesoftware.jstately.graph.state.DefaultState;
import com.coalminesoftware.jstately.graph.state.DefaultSubmachineState;
import com.coalminesoftware.jstately.graph.state.State;
import com.coalminesoftware.jstately.graph.state.SubmachineState;

/** Defines a StaetGraph with two states, the second of which is a SubmachineState. */
public class TwoStateStateGraphWithSubmachineState extends StateGraph<Object> {
	private State<Object> firstState;
	private SubmachineState<Object> secondState; 

	public TwoStateStateGraphWithSubmachineState(StateGraph<Object> submachineStateGraph, String stateDescriptionPrefix) {
		firstState = new DefaultState<>(stateDescriptionPrefix+"/first");
		setStartState(firstState);

		secondState = new DefaultSubmachineState<>(stateDescriptionPrefix+"/second", submachineStateGraph);
		addTransition(firstState, new DefaultTransition<>(secondState));
	}

	public State<Object> getFirstState() {
		return firstState;
	}

	public SubmachineState<Object> getSecondState() {
		return secondState;
	}
}


