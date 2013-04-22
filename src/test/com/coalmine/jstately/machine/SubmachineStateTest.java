package com.coalmine.jstately.machine;

import static org.junit.Assert.*;

import java.util.List;

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
		innerGraph.setStartState(innerAState = new DefaultState<Integer>("A"));

		innerBState = new DefaultState<Integer>("B");
		innerGraph.addTransition(innerAState, new EqualityTransition<Integer>(innerBState, 100));

		innerCState = new DefaultFinalState<Integer>("C",2);
		innerGraph.addTransition(innerBState, new EqualityTransition<Integer>(innerCState, 200));

		outerGraph = new StateGraph<Integer>();
		outerGraph.setStartState(outerStart = new DefaultState<Integer>("Start"));

		outerSubmachineState = new DefaultSubmachineState<Integer>("Submachine State", innerGraph);
		outerGraph.addTransition(outerStart, new EqualityTransition<Integer>(outerSubmachineState, 1));

		outerSuccess = new DefaultState<Integer>("Success");
		outerGraph.addTransition(outerSubmachineState, new EqualityTransition<Integer>(outerSuccess, 2));
	}

	@Test
	public void testMachine() {
		StateMachine<Integer,Integer> stateMachine = new StateMachine<Integer, Integer>(outerGraph, new DefaultInputAdapter<Integer>());
		stateMachine.addEventListener(new ConsoleStateMachineEventListener<Integer>());

		stateMachine.start();
		assertEquals(outerStart, stateMachine.getState());

		stateMachine.evaluateInput(1);
		List<State<Integer>> machineState = stateMachine.getStates();
		assertEquals(2, machineState.size());
		assertEquals(outerSubmachineState, machineState.get(0));
		assertEquals(innerAState, machineState.get(1));

		// Test transitions within the inner graph
		stateMachine.evaluateInput(100);
		machineState = stateMachine.getStates();
		assertEquals(outerSubmachineState, machineState.get(0));
		assertEquals(innerBState, machineState.get(1));

		// Test transitioning out of the inner graph
		stateMachine.evaluateInput(200);
		assertEquals(outerSuccess, stateMachine.getState());
	}
}


