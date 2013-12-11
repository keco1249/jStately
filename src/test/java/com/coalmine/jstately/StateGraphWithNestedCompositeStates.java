package com.coalmine.jstately;

import com.coalmine.jstately.graph.StateGraph;
import com.coalmine.jstately.graph.composite.CompositeState;
import com.coalmine.jstately.graph.state.DefaultState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.EqualityTransition;

public class StateGraphWithNestedCompositeStates extends StateGraph<Integer> {
	public State<Integer> stateS = new DefaultState<Integer>("State S");
	public State<Integer> stateA = new DefaultState<Integer>("State A");
	public State<Integer> stateB = new DefaultState<Integer>("State B");
	public State<Integer> stateF = new DefaultState<Integer>("State F");

	public CompositeState<Integer> outerCompositeState = new CompositeState<Integer>("Outer CompositeState");
	public CompositeState<Integer> firstInnerCompositeState = new CompositeState<Integer>("First inner CompositeState");
	public CompositeState<Integer> secondInnerCompositeState = new CompositeState<Integer>("Second inner CompositeState");

	public StateGraphWithNestedCompositeStates() {
		addTransition(stateS, new EqualityTransition<Integer>(stateA, 1));
		addTransition(stateA, new EqualityTransition<Integer>(stateB, 2));
		addTransition(stateB, new EqualityTransition<Integer>(stateF, 3));

		firstInnerCompositeState.addState(stateA);

		secondInnerCompositeState.addState(stateB);

		outerCompositeState.addComposite(firstInnerCompositeState);
		outerCompositeState.addComposite(secondInnerCompositeState);
	}
}


