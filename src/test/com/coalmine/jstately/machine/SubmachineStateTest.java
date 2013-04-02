package com.coalmine.jstately.machine;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.coalmine.jstately.graph.StateGraph;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.state.DefaultSubmachineState;
import com.coalmine.jstately.graph.state.DefaultFinalState;
import com.coalmine.jstately.graph.state.DefaultState;
import com.coalmine.jstately.graph.transition.EqualityTransition;
import com.coalmine.jstately.machine.listener.ConsoleStateMachineEventListener;

public class SubmachineStateTest {
	private static StateGraph<Integer> outerGraph;
	private static State<Integer> outerStart;
	private static DefaultSubmachineState<Integer> outerSubmachineState;
	private static State<Integer> outerSuccess;

	private static StateGraph<Integer> innerGraph;
	private static State<Integer> innerAState;
	private static State<Integer> innerBState;
	private static State<Integer> innerCState;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Setup test graph
		// 
		//   1  __100_200__  2
		// S → |_A_→_B_→_C_| → S

		innerGraph = new StateGraph<Integer>();
		innerGraph.addStartState(innerAState = new DefaultState<Integer>("A"));

		innerGraph.addState(innerBState = new DefaultState<Integer>("B"));
		innerGraph.addTransition(new EqualityTransition<Integer>(innerAState, innerBState, 100));

		innerGraph.addState(innerCState = new DefaultFinalState<Integer>("C",2));
		innerGraph.addTransition(new EqualityTransition<Integer>(innerBState, innerCState, 200));

		outerGraph = new StateGraph<Integer>();
		outerGraph.addStartState(outerStart = new DefaultState<Integer>("Start"));

		outerGraph.addState(outerSubmachineState = new DefaultSubmachineState<Integer>("Submachine State", innerGraph));
		outerGraph.addTransition(new EqualityTransition<Integer>(outerStart, outerSubmachineState, 1));

		outerGraph.addState(outerSuccess = new DefaultState<Integer>("Success"));
		outerGraph.addTransition(new EqualityTransition<Integer>(outerSubmachineState, outerSuccess, 2));
	}

	@Test
	public void testMachine() {
		StateMachine<Integer,Integer> stateMachine = new StateMachine<Integer, Integer>(outerGraph, new DefaultInputAdapter<Integer>());
		stateMachine.addEventListener(new ConsoleStateMachineEventListener<Integer>());

		stateMachine.start();
		assertTrue(outerStart.equals(stateMachine.getState()));

		stateMachine.evaluateInput(1);
		assertTrue(outerSubmachineState.equals(stateMachine.getState()));
		assertTrue(innerAState.equals(stateMachine.getSubState()));

		// Test transitions within the inner graph
		stateMachine.evaluateInput(100);
		assertTrue(outerSubmachineState.equals(stateMachine.getState()));
		assertTrue(innerBState.equals(stateMachine.getSubState()));

		// Test transitioning out of the inner graph
		stateMachine.evaluateInput(200);
		assertTrue(outerSuccess.equals(stateMachine.getState()));
	}
}


