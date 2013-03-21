package com.coalmine.jstately.graph.state;

import java.util.List;

import com.coalmine.jstately.graph.StateGraph;
import com.coalmine.jstately.machine.DefaultInputAdapter;
import com.coalmine.jstately.machine.StateMachine;
import com.coalmine.jstately.machine.listener.StateMachineEventListener;

public class CompositeState<TransitionInput> extends DefaultState {
	private StateGraph<TransitionInput> stateGraph;
	private StateMachine<TransitionInput,TransitionInput> stateMachine;
	List<StateMachineEventListener<TransitionInput>> eventListeners;

	public CompositeState() { }

	public CompositeState(String identifier, StateGraph<TransitionInput> stateGraph) {
		super(identifier);
		this.stateGraph = stateGraph;
	}

	public CompositeState(String identifier, StateGraph<TransitionInput> stateGraph, List<StateMachineEventListener<TransitionInput>> eventListeners) {
		super(identifier);
		this.stateGraph = stateGraph;
	}


	public void evaluateInput(TransitionInput input) {
		stateMachine.evaluateInput(input);
	}

	@Override
	public void onEnter() {
		stateMachine = new StateMachine<TransitionInput,TransitionInput>(stateGraph, new DefaultInputAdapter<TransitionInput>());
		if(eventListeners != null) {
			stateMachine.setEventListeners(eventListeners); // TODO Not sure I like this.
		}
		stateMachine.start();
	}

	public BaseState getSubState() {
		return stateMachine.getState();
	}

	@Override
	public void onExit() {
		stateMachine = null; // Let the machine be garbage collected
	}

	public StateGraph<TransitionInput> getStateGraph() {
		return stateGraph;
	}
	public void setStateGraph(StateGraph<TransitionInput> stateGraph) {
		this.stateGraph = stateGraph;
	}
}


