package com.coalmine.jstately.test;

import com.coalmine.jstately.graph.StateGraph;
import com.coalmine.jstately.graph.state.DefaultState;
import com.coalmine.jstately.graph.state.State;

/** Defines a StaetGraph with two states, the second of which is a SubmachineState. */
public class TwoStateStateGraph extends StateGraph<Object> {
	private State<Object> firstState;
	private State<Object> secondState; 

	public TwoStateStateGraph(String stateDescriptionPrefix) {
		firstState = new DefaultState<Object>(stateDescriptionPrefix+"/first");
		setStartState(firstState);

		secondState = new DefaultState<Object>(stateDescriptionPrefix+"/second");
		addTransition(firstState, new DefaultTransition<Object>(secondState));
	}

	public State<Object> getFirstState() {
	    return firstState;
    }

	public State<Object> getSecondState() {
	    return secondState;
    }
}


