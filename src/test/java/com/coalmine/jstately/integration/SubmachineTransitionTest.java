package com.coalmine.jstately.integration;

import static org.junit.Assert.*;

import org.junit.Test;

import com.coalmine.jstately.graph.StateGraph;
import com.coalmine.jstately.graph.state.DefaultState;
import com.coalmine.jstately.graph.state.DefaultSubmachineState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.machine.DefaultInputAdapter;
import com.coalmine.jstately.machine.StateMachine;
import com.coalmine.jstately.test.DefaultTransition;
import com.coalmine.jstately.test.Event;
import com.coalmine.jstately.test.EventType;
import com.coalmine.jstately.test.TestStateMachineEventListener;
import com.google.common.collect.Lists;

public class SubmachineTransitionTest {
	@Test
	@SuppressWarnings("unchecked")
	public void testTransitionToSameSubmachineState() {
		TestContext context = new TestContext();

		context.machine.transition(context.outerStateGraphStartState, context.firstInnerStateGraphStartState);

		context.listener.assertEventsOccurred(
				Event.forStateExit(context.firstInnerStateGraphStartState),
				Event.forStateEntry(context.firstInnerStateGraphStartState));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testTransitionToDifferentSubmachineState() {
		TestContext context = new TestContext();

		context.machine.transition(context.outerStateGraphStartState, context.firstInnerStateGraphSecondState);

		context.listener.assertEventsOccurred(
				Event.forStateExit(context.firstInnerStateGraphStartState),
				Event.forStateEntry(context.firstInnerStateGraphSecondState));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testTransitionToSameOuterMachineState() {
		TestContext context = new TestContext();

		context.machine.transition(context.outerStateGraphStartState);

		context.listener.assertEventsOccurred(
				Event.forStateExit(context.firstInnerStateGraphStartState),
				Event.forStateExit(context.outerStateGraphStartState),
				Event.forStateEntry(context.outerStateGraphStartState),
				Event.forStateEntry(context.firstInnerStateGraphStartState)); // Called implicitly when the outer machines submachine is started
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testTransitionToDifferentOuterMachineState() {
		TestContext context = new TestContext();

		context.machine.transition(context.outerStateGraphSecondState);

		context.listener.assertEventsOccurred(
				Event.forStateExit(context.firstInnerStateGraphStartState),
				Event.forStateExit(context.outerStateGraphStartState),
				Event.forStateEntry(context.outerStateGraphSecondState),
				Event.forStateEntry(context.secondInnerStateGraphStartState));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testTransitionToDifferentOuterMachineStateWithInnerMachineState() {
		TestContext context = new TestContext();

		context.machine.transition(context.outerStateGraphSecondState, context.secondInnerStateGraphStartState);

		context.listener.assertEventsOccurred(
				Event.forStateExit(context.firstInnerStateGraphStartState),
				Event.forStateExit(context.outerStateGraphStartState),
				Event.forStateEntry(context.outerStateGraphSecondState),
				Event.forStateEntry(context.secondInnerStateGraphStartState));
	}


	private static class TestContext {
		public StateGraph<Object> firstInnerStateGraph;
		public State<Object> firstInnerStateGraphStartState;
		public State<Object> firstInnerStateGraphSecondState;
		public StateGraph<Object> secondInnerStateGraph;
		public State<Object> secondInnerStateGraphStartState;
		public State<Object> secondInnerStateGraphSecondState;
		public StateGraph<Object> outerStateGraph;
		public State<Object> outerStateGraphStartState;
		public State<Object> outerStateGraphSecondState;
		public StateMachine<Object,Object> machine;
		public TestStateMachineEventListener<Object> listener;

		@SuppressWarnings("unchecked")
        public TestContext() {
			firstInnerStateGraph = new StateGraph<Object>();
			firstInnerStateGraphStartState = new DefaultState<Object>("First inner start");
			firstInnerStateGraph.setStartState(firstInnerStateGraphStartState);
			firstInnerStateGraphSecondState = new DefaultState<Object>("First inner second");
			firstInnerStateGraph.addTransition(firstInnerStateGraphStartState, new DefaultTransition<Object>(firstInnerStateGraphSecondState));

			secondInnerStateGraph = new StateGraph<Object>();
			secondInnerStateGraphStartState = new DefaultState<Object>("Second inner start");
			secondInnerStateGraph.setStartState(secondInnerStateGraphStartState);
			secondInnerStateGraphSecondState = new DefaultState<Object>("Second inner second");
			secondInnerStateGraph.addTransition(secondInnerStateGraphStartState, new DefaultTransition<Object>(secondInnerStateGraphSecondState));

			outerStateGraph = new StateGraph<Object>();
			outerStateGraphStartState = new DefaultSubmachineState<Object>("Outer start", firstInnerStateGraph);
			outerStateGraph.setStartState(outerStateGraphStartState);
			outerStateGraphSecondState = new DefaultSubmachineState<Object>("Outer second", secondInnerStateGraph);
			outerStateGraph.addTransition(outerStateGraphStartState, new DefaultTransition<Object>(outerStateGraphSecondState));

			machine = new StateMachine<Object,Object>(outerStateGraph, new DefaultInputAdapter<Object>());

			machine.start();
			assertEquals("Couldn't initialize test.  Please check for failures elsewhere.",
					Lists.newArrayList(outerStateGraphStartState, firstInnerStateGraphStartState),
					machine.getStates());

			listener = new TestStateMachineEventListener<Object>(EventType.STATE_ENTERED, EventType.STATE_EXITED);
			machine.addEventListener(listener);
		}
	}
}


