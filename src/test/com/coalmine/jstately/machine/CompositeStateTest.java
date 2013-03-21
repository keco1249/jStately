package com.coalmine.jstately.machine;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.coalmine.jstately.graph.StateGraph;
import com.coalmine.jstately.graph.state.CompositeState;
import com.coalmine.jstately.graph.state.DefaultFinalState;
import com.coalmine.jstately.graph.state.DefaultState;
import com.coalmine.jstately.graph.state.NonFinalState;
import com.coalmine.jstately.graph.transition.EqualityTransition;
import com.coalmine.jstately.machine.listener.ConsoleStateMachineEventListener;

public class CompositeStateTest {
	private static StateGraph<Integer> outerGraph;
	private static NonFinalState outerStart;
	private static CompositeState<Integer> outerComposite;
	private static NonFinalState outerSuccess;

	private static StateGraph<Integer> innerGraph;
	private static NonFinalState innerAState;
	private static NonFinalState innerBState;
	private static NonFinalState innerCState;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Setup test graph
		// 
		//   1  __100_200__  2
		// S → |_A_→_B_→_C_| → S

		innerGraph = new StateGraph<Integer>();
		innerGraph.addStartState(innerAState = new DefaultState("A"));

		innerGraph.addState(innerBState = new DefaultState("B"));
		innerGraph.addTransition(new EqualityTransition<Integer>(innerAState, innerBState, 100));

		innerGraph.addState(innerCState = new DefaultFinalState<Integer>("C",2));
		innerGraph.addTransition(new EqualityTransition<Integer>(innerBState, innerCState, 200));

		outerGraph = new StateGraph<Integer>();
		outerGraph.addStartState(outerStart = new DefaultState("Start"));

		outerGraph.addState(outerComposite = new CompositeState<Integer>("Composite State", innerGraph));
		outerGraph.addTransition(new EqualityTransition<Integer>(outerStart, outerComposite, 1));

		outerGraph.addState(outerSuccess = new DefaultState("Success"));
		outerGraph.addTransition(new EqualityTransition<Integer>(outerComposite, outerSuccess, 2));
	}

	@Test
	public void testMachine() {
		StateMachine<Integer,Integer> stateMachine = new StateMachine<Integer, Integer>(outerGraph, new DefaultInputAdapter<Integer>());
		stateMachine.addEventListener(new ConsoleStateMachineEventListener<Integer>());

		stateMachine.start();
		assertTrue(outerStart.equals(stateMachine.getState()));

		stateMachine.evaluateInput(1);
		assertTrue(outerComposite.equals(stateMachine.getState()));
		assertTrue(innerAState.equals(stateMachine.getSubState()));

		// Test transitions within the inner graph
		stateMachine.evaluateInput(100);
		assertTrue(outerComposite.equals(stateMachine.getState()));
		assertTrue(innerBState.equals(stateMachine.getSubState()));

		// Test transitioning out of the inner graph
		stateMachine.evaluateInput(200);
		assertTrue(outerSuccess.equals(stateMachine.getState()));
	}
}


