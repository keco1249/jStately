package com.coalmine.jstately.integration;

import org.junit.BeforeClass;
import org.junit.Test;

import com.coalmine.jstately.graph.StateGraph;
import com.coalmine.jstately.graph.composite.CompositeState;
import com.coalmine.jstately.graph.state.DefaultState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.EqualityTransition;
import com.coalmine.jstately.graph.transition.Transition;
import com.coalmine.jstately.machine.DefaultInputAdapter;
import com.coalmine.jstately.machine.StateMachine;
import com.coalmine.jstately.test.Event;
import com.coalmine.jstately.test.EventType;
import com.coalmine.jstately.test.TestStateMachineEventListener;

public class IntegrationTest {
	private static State<Integer> stateA;
	private static State<Integer> stateB;
	private static State<Integer> stateC;
	private static State<Integer> stateD;

	private static Transition<Integer> transitionAB;
	private static Transition<Integer> transitionBC;
	private static Transition<Integer> transitionCD;

	private static CompositeState<Integer> compositeX;
	private static CompositeState<Integer> compositeX1;
	private static CompositeState<Integer> compositeX2;

	private static Transition<Integer> transitionX1A;
	private static Transition<Integer> transitionXA;

	private static CompositeState<Integer> compositeY;

	private static StateGraph<Integer> graph;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		graph = new StateGraph<Integer>();

		stateA = new DefaultState<Integer>("State A");
		graph.setStartState(stateA);

		stateB = new DefaultState<Integer>("State B");
		transitionAB = new EqualityTransition<Integer>(stateB, 1);
		graph.addTransition(stateA, transitionAB);

		stateC = new DefaultState<Integer>("State C");
		transitionBC = new EqualityTransition<Integer>(stateC, 2);
		graph.addTransition(stateB, transitionBC);

		stateD = new DefaultState<Integer>("State D");
		transitionCD = new EqualityTransition<Integer>(stateD, 3);
		graph.addTransition(stateC, transitionCD);

		// Fist set of (nested) composites
		compositeX1 = new CompositeState<Integer>("First inner composite");
		compositeX1.addState(stateB);
		transitionX1A = new EqualityTransition<Integer>(stateA, 100);
		compositeX1.addTransition(transitionX1A);

		compositeX2 = new CompositeState<Integer>("Second inner composite");
		compositeX2.addState(stateC);

		compositeX = new CompositeState<Integer>("Outer composite");
		compositeX.addComposite(compositeX1);
		compositeX.addComposite(compositeX2);
		transitionXA = new EqualityTransition<Integer>(stateA, 100); // The same expected input as transitionX1A, to ensure that transitionX1A takes priority
		compositeX.addTransition(transitionXA);

		// Second composite
		compositeY = new CompositeState<Integer>("Overlapping outer composite");
		compositeY.addState(stateB);
		compositeY.addTransition(new EqualityTransition<Integer>(stateD, 200));
	}

	@Test
	public void testStateMachineStateTransitioning() {
		StateMachine<Integer,Integer> machine = new StateMachine<Integer,Integer>(graph, new DefaultInputAdapter<Integer>());

		TestStateMachineEventListener<Integer> listener = new TestStateMachineEventListener<Integer>(EventType.ALL_TYPES_EXCEPT_INPUT_VALIDATION);
		machine.addEventListener(listener);

		machine.start();
		listener.assertEventsOccurred(
				Event.forStateEntry(stateA));

		machine.evaluateInput(0);
		listener.assertEventsOccurred(
				Event.forNoTransitionFound(0));

		machine.evaluateInput(1);
		listener.assertEventsOccurred(
				Event.forStateExit(stateA),
				Event.forTransitionFollowed(transitionAB),
				Event.forCompositeStateEntry(compositeX),
				Event.forCompositeStateEntry(compositeX1),
				Event.forCompositeStateEntry(compositeY),
				Event.forStateEntry(stateB));

		machine.evaluateInput(2);
		listener.assertEventsOccurred(
				Event.forStateExit(stateB),
				Event.forCompositeStateExit(compositeY),
				Event.forCompositeStateExit(compositeX1),
				Event.forTransitionFollowed(transitionBC),
				Event.forCompositeStateEntry(compositeX2),
				Event.forStateEntry(stateC));

		machine.evaluateInput(3);
		listener.assertEventsOccurred(
				Event.forStateExit(stateC),
				Event.forCompositeStateExit(compositeX2),
				Event.forCompositeStateExit(compositeX),
				Event.forTransitionFollowed(transitionCD),
				Event.forStateEntry(stateD));
	}

	@Test
	public void testStateMachineStateTransitionPrecedence() {
		StateMachine<Integer,Integer> machine = new StateMachine<Integer,Integer>(graph, new DefaultInputAdapter<Integer>());
		machine.transition(stateB);

		TestStateMachineEventListener<Integer> listener = new TestStateMachineEventListener<Integer>(EventType.ALL_TYPES_EXCEPT_INPUT_VALIDATION);
		machine.addEventListener(listener);

		machine.evaluateInput(100);
		listener.assertEventOccurred(Event.forTransitionFollowed(transitionX1A));

		machine.transition(stateC);
		listener.clearObservedEvents();
		machine.evaluateInput(100);
		listener.assertEventOccurred(Event.forTransitionFollowed(transitionXA));
	}
}


