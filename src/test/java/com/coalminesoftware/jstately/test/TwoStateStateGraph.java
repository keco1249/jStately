package com.coalminesoftware.jstately.test;

import com.coalminesoftware.jstately.graph.StateGraph;
import com.coalminesoftware.jstately.graph.state.DefaultState;
import com.coalminesoftware.jstately.graph.state.State;

/** Defines a StaetGraph with two states, the second of which is a SubmachineState. */
public class TwoStateStateGraph extends StateGraph<Object> {
	private State<Object> firstState;
	private State<Object> secondState; 

	public TwoStateStateGraph(String stateDescriptionPrefix) {
		firstState = new DefaultState<>(stateDescriptionPrefix+"/first");
		setStartState(firstState);

		secondState = new DefaultState<>(stateDescriptionPrefix+"/second");
		addTransition(firstState, new DefaultTransition<>(secondState));
	}

	public State<Object> getFirstState() {
		return firstState;
	}

	public State<Object> getSecondState() {
		return secondState;
	}
}


