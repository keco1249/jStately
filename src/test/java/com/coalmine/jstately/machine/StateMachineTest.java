package com.coalmine.jstately.machine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.coalmine.jstately.graph.StateGraph;
import com.coalmine.jstately.graph.state.DefaultState;
import com.coalmine.jstately.graph.state.DefaultSubmachineState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.EqualityTransition;
import com.coalmine.jstately.test.Event;
import com.coalmine.jstately.test.EventType;
import com.coalmine.jstately.test.TestMachineEventListener;
import com.coalmine.jstately.test.TwoStateStateGraph;
import com.coalmine.jstately.test.TwoStateStateGraphWithSubmachineState;
import com.google.common.collect.Lists;


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
	@SuppressWarnings("unchecked")
	public void testEnterStateWithTopLevelSubmachineState() {
		// On a state graph with multiple levels of nested graphs, enter the top-level SubmachineState (without 
		// specifying submachine states) and ensure that submachines were initialized to their graph's start states.

		StateGraph<Object> innerGraph = createGraphWithSingleNonSubmachineState();
		StateGraph<Object> intermediateGraph = createStateGraphWithSubmachineState(innerGraph);
		StateGraph<Object> outerGraph = createStateGraphWithSubmachineState(intermediateGraph);

		StateMachine<Object,Object> machine = new StateMachine<Object,Object>(outerGraph, new DefaultInputAdapter<Object>());
		TestMachineEventListener<Object> listener = new TestMachineEventListener<Object>();
		machine.addEventListener(listener);

		machine.enterState(outerGraph.getStartState());

		assertEquals("Expected to see each graph's start state, ordered from outer to inner",
				Lists.newArrayList(outerGraph.getStartState(), intermediateGraph.getStartState(), innerGraph.getStartState()),
				machine.getStates());

		listener.assertEventsOccurred(
				Event.forStateEntry(outerGraph.getStartState()),
				Event.forStateEntry(intermediateGraph.getStartState()),
				Event.forStateEntry(innerGraph.getStartState()));
	}

	private StateGraph<Object> createGraphWithSingleNonSubmachineState() {
		StateGraph<Object> graph = new StateGraph<Object>();
		graph.setStartState(new DefaultState<Object>());

		return graph;
	}

	private StateGraph<Object> createStateGraphWithSubmachineState(StateGraph<Object> nestedStateGraph) {
		DefaultSubmachineState<Object> submachineState = new DefaultSubmachineState<Object>();
		submachineState.setStateGraph(nestedStateGraph);

		StateGraph<Object> graph = new StateGraph<Object>();
		graph.setStartState(submachineState);

		return graph;
	}

    @Test
    @SuppressWarnings("unchecked")
	public void testEnterStateWithSubmachineStates() {
		// On a state graph with multiple levels of nested graphs, enter the top-level SubmachineState
		// and ensure that the machine initializes to the start state of the nested graphs

		TwoStateStateGraph innerGraph = new TwoStateStateGraph("inner");
		TwoStateStateGraphWithSubmachineState intermediateGraph = new TwoStateStateGraphWithSubmachineState(innerGraph, "intermediate");
		TwoStateStateGraphWithSubmachineState outerGraph = new TwoStateStateGraphWithSubmachineState(intermediateGraph, "outer");

		StateMachine<Object,Object> machine = new StateMachine<Object,Object>(outerGraph, new DefaultInputAdapter<Object>());
		TestMachineEventListener<Object> listener = new TestMachineEventListener<Object>();
		machine.addEventListener(listener);

		machine.enterState(outerGraph.getSecondState(), intermediateGraph.getSecondState(), innerGraph.getSecondState());

		assertEquals("Expected the machine's states to match the states provided to enterState().",
				Lists.newArrayList(outerGraph.getSecondState(), intermediateGraph.getSecondState(), innerGraph.getSecondState()),
				machine.getStates());

		listener.assertEventsOccurred(
				Event.forStateEntry(outerGraph.getSecondState()),
				Event.forStateEntry(intermediateGraph.getSecondState()),
				Event.forStateEntry(innerGraph.getSecondState()));
    }

    @Test
    @SuppressWarnings("unchecked")
	public void testExitState() {
		StateGraph<Object> innerGraph = createGraphWithSingleNonSubmachineState();
		StateGraph<Object> intermediateGraph = createStateGraphWithSubmachineState(innerGraph);
		StateGraph<Object> outerGraph = createStateGraphWithSubmachineState(intermediateGraph);

		StateMachine<Object,Object> machine = new StateMachine<Object,Object>(outerGraph, new DefaultInputAdapter<Object>());

		machine.start();
		assertEquals("State machine could not be initialized for test.",
				Lists.newArrayList(outerGraph.getStartState(), intermediateGraph.getStartState(), innerGraph.getStartState()),
				machine.getStates());

		TestMachineEventListener<Object> listener = new TestMachineEventListener<Object>();
		machine.addEventListener(listener);

		machine.exitState(null);

		listener.assertEventsOccurred(
//				Event.forStateExit(innerGraph.getStartState()),
//				Event.forStateExit(intermediateGraph.getStartState()),
				Event.forStateExit(outerGraph.getStartState()));
	}
}


