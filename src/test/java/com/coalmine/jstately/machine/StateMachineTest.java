package com.coalmine.jstately.machine;

import static org.junit.Assert.*;

import org.junit.Test;

import com.coalmine.jstately.graph.StateGraph;
import com.coalmine.jstately.graph.state.DefaultState;
import com.coalmine.jstately.graph.state.DefaultSubmachineState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.EqualityTransition;
import com.coalmine.jstately.test.Event;
import com.coalmine.jstately.test.EventType;
import com.coalmine.jstately.test.TestStateMachineEventListener;
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
    @SuppressWarnings("unchecked")
	public void testEvaluateInputWhileInSubmachineState() {
		// Test scenario where the machine is in a submachine state when evaluateInput()
		// is called, in which case it should delegate the input to the submachine.

		State<Object> innerState = new DefaultState<Object>();
		StateGraph<Object> innerGraph = new StateGraph<Object>();
		innerGraph.setStartState(innerState);

		State<Object> intermediateState = new DefaultSubmachineState<Object>(null, innerGraph);
		StateGraph<Object> intermediateGraph = new StateGraph<Object>();
		intermediateGraph.setStartState(intermediateState);

		State<Object> outerState = new DefaultSubmachineState<Object>(null, intermediateGraph);
		StateGraph<Object> outerGraph = new StateGraph<Object>();
		outerGraph.setStartState(outerState);

		StateMachine<Object,Object> machine = new StateMachine<Object, Object>(outerGraph, new DefaultInputAdapter<Object>());
		machine.start();

		assertEquals("Machine could't be initialized as expected.",
				Lists.newArrayList(outerState, intermediateState, innerState),
				machine.getStates());

		TestStateMachineEventListener<Object> listener = new TestStateMachineEventListener<Object>(EventType.INPUT_EVALUATED);
		machine.addEventListener(listener);

		Object input = "";

		machine.evaluateInput(input);

		// TODO This assertion verifies that the input was evaluated three times - once per (sub)machine - but doesn't verify that they happened on different machines or the ordering.  Improve it.

		listener.assertEventsOccurred(
				Event.forInputEvaluated(input),
				Event.forInputEvaluated(input),
				Event.forInputEvaluated(input));
	}

	@Test(expected=IllegalStateException.class)
	public void testFindFirstValidTransitionFromCurrentStatePriorToStarting() {
		StateMachine<Object,Object> machine = new StateMachine<Object,Object>();

		machine.findFirstValidTransitionFromCurrentState(null);
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
		TestStateMachineEventListener<Object> listener = new TestStateMachineEventListener<Object>();
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
		TestStateMachineEventListener<Object> listener = new TestStateMachineEventListener<Object>();
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
	public void testExitCurrentState() {
		StateGraph<Object> innerGraph = createGraphWithSingleNonSubmachineState();
		StateGraph<Object> intermediateGraph = createStateGraphWithSubmachineState(innerGraph);
		StateGraph<Object> outerGraph = createStateGraphWithSubmachineState(intermediateGraph);

		StateMachine<Object,Object> machine = new StateMachine<Object,Object>(outerGraph, new DefaultInputAdapter<Object>());

		machine.start();
		assertEquals("State machine could not be initialized for test.",
				Lists.newArrayList(outerGraph.getStartState(), intermediateGraph.getStartState(), innerGraph.getStartState()),
				machine.getStates());

		TestStateMachineEventListener<Object> listener = new TestStateMachineEventListener<Object>();
		machine.addEventListener(listener);

		machine.exitCurrentState(null);

		listener.assertEventsOccurred(
				Event.forStateExit(innerGraph.getStartState()),
				Event.forStateExit(intermediateGraph.getStartState()),
				Event.forStateExit(outerGraph.getStartState()));
	}

	@Test
	public void testEvaluateInputWithNullInput() {
		StateGraph<Integer> graph = new StateGraph<Integer>();

		State<Integer> stateS = new DefaultState<Integer>("S");
		graph.setStartState(stateS);

		State<Integer> stateA = new DefaultState<Integer>("A");
		graph.addTransition(stateS, new EqualityTransition<Integer>(stateA, null));

		StateMachine<Integer, Integer> machine = new StateMachine<Integer, Integer>(graph, new DefaultInputAdapter<Integer>());

		machine.start();
		assertEquals("Machine expected to start in its graph's start state",
		        graph.getStartState(),
		        machine.getState());

		// Test input that should not cause a transition
		machine.evaluateInput(1);
		assertEquals("Machine expected to stay in ",
				graph.getStartState(),
				machine.getState());

		// Ensure that null input gets evaluated
		machine.evaluateInput(null);
		assertEquals("Machine expected to have transitioned",
				stateA,
				machine.getState());
	}
}


