package com.coalmine.jstately.graph.state;

import com.coalmine.jstately.graph.StateGraph;


public class DefaultCompositeState<TransitionInput> extends DefaultState<TransitionInput> implements CompositeState<TransitionInput> {
	private StateGraph<TransitionInput> stateGraph;

	public DefaultCompositeState() { }

	public DefaultCompositeState(String identifier, StateGraph<TransitionInput> stateGraph) {
		super(identifier);
		this.stateGraph = stateGraph;
	}


	public StateGraph<TransitionInput> getStateGraph() {
		return stateGraph;
	}
	public void setStateGraph(StateGraph<TransitionInput> stateGraph) {
		this.stateGraph = stateGraph;
	}
}


