package com.coalmine.jstately.machine;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.coalmine.jstately.graph.StateGraph;
import com.coalmine.jstately.graph.state.DefaultState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.EqualityTransition;


public class StateMachineTest {
	@Test(expected=IllegalStateException.class)
	public void testStartWhenRunning() {
		StateMachine<Object,Object> machine = new StateMachine<Object, Object>();
		machine.overrideState(new DefaultState<Object>());

		machine.start();
	}

	@Test(expected=IllegalStateException.class)
	public void testStartWithoutGraph() {
		StateMachine<Object,Object> machine = new StateMachine<Object, Object>();

		machine.start();
	}

	@Test(expected=IllegalStateException.class)
	public void testStartWithoutGraphStartState() {
		StateMachine<Object,Object> machine = new StateMachine<Object, Object>();
		machine.setStateGraph(new StateGraph<Object>());

		machine.start();
	}

	@Test
	public void testHasStarted() {
		StateMachine<Object,Object> machine = new StateMachine<Object, Object>();
		assertFalse(machine.hasStarted());

		machine.overrideState(new DefaultState<Object>());
		assertTrue(machine.hasStarted());
	}

	@Test(expected=IllegalStateException.class)
	public void testEvaluateInputWithoutInputAdapter() {
		StateMachine<Object,Object> machine = new StateMachine<Object,Object>();
		machine.evaluateInput(null);
	}

	@Test
	public void testEvaluateInputWithoutValidTransition() {
		StateGraph<Integer> graph = new StateGraph<Integer>();

		State<Integer> startState = new DefaultState<Integer>();
		graph.setStartState(startState);

		State<Integer> intermediateState = new DefaultState<Integer>();
		graph.addTransition(startState, new EqualityTransition<Integer>(intermediateState, 1));

		StateMachine<Integer,Integer> machine = new StateMachine<Integer,Integer>(graph, new DefaultInputAdapter<Integer>());
		machine.start();

		assertTrue("Method should return false if no transitions are valid for any of the given input(s).",
				machine.evaluateInput(0));
	}

	@Test
	public void testEvaluateInputWhileInSubmachineState() {
		// Test scenario where the machine is in a submachine state when evaluateInput()
		// is called, in which case it will delegate the input to the submachine.

		fail("Not yet implemented");
	}

	@Test(expected=IllegalStateException.class)
	public void testFindFirstValidTransitionFromCurrentStatePriorToStarting() {
		StateMachine<Object,Object> machine = new StateMachine<Object,Object>();

		machine.findFirstValidTransitionFromCurrentState(null);
	}

	@Test
	public void testTransitionTransitionOfTransitionInputTransitionInput() {
		fail("Not yet implemented");
	}

	@Test
	public void testTransitionStateOfTransitionInputStateOfTransitionInputArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetStates() {
		fail("Not yet implemented");
	}
}


