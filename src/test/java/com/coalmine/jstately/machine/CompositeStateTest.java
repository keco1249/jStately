package com.coalmine.jstately.machine;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.coalmine.jstately.graph.StateGraph;
import com.coalmine.jstately.graph.composite.CompositeState;
import com.coalmine.jstately.graph.state.DefaultState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.EqualityTransition;
import com.coalmine.jstately.test.Event;
import com.coalmine.jstately.test.EventType;
import com.coalmine.jstately.test.TestEventListener;

public class CompositeStateTest {
	private static StateGraph<Integer> graph;

	private static State<Integer> stateA;
	private static State<Integer> stateB;
	private static State<Integer> stateC;
	private static State<Integer> stateD;
	private static State<Integer> stateE;
	private static State<Integer> stateF;

	private static CompositeState<Integer> outerComposite;
	private static CompositeState<Integer> firstInnerComposite;
	private static CompositeState<Integer> secondInnerComposite;


	@BeforeClass
	public static void setUpBeforeClass() {
		//      ______________________
		//   1 |   2  ___  3  ___4___ | 5
		// A → | B → |_C_| → |_D_→_E_|| → F
		//     |______________________|
		// 
		// * Plus a transition from the CompositeState containing States D and E, to State C, valid for an input of 100

		graph = new StateGraph<Integer>();

		stateA = new DefaultState<Integer>("A");
		stateB = new DefaultState<Integer>("B");
		stateC = new DefaultState<Integer>("C");
		stateD = new DefaultState<Integer>("D");
		stateE = new DefaultState<Integer>("E");
		stateF = new DefaultState<Integer>("F");

		graph.setStartState(stateA);
		graph.addTransition(stateA, new EqualityTransition<Integer>(stateB, 1));
		graph.addTransition(stateB, new EqualityTransition<Integer>(stateC, 2));
		graph.addTransition(stateC, new EqualityTransition<Integer>(stateD, 3));
		graph.addTransition(stateD, new EqualityTransition<Integer>(stateE, 4));
		graph.addTransition(stateE, new EqualityTransition<Integer>(stateF, 5));

		outerComposite = new CompositeState<Integer>("Outer Composite");
		outerComposite.addState(stateB);

		firstInnerComposite = new CompositeState<Integer>("First Inner Composite");
		outerComposite.addComposite(firstInnerComposite);
		firstInnerComposite.addState(stateC);

		secondInnerComposite = new CompositeState<Integer>("Second Inner Composite");
		outerComposite.addComposite(secondInnerComposite);
		secondInnerComposite.addState(stateD);
		secondInnerComposite.addState(stateE);
		secondInnerComposite.addTransition(new EqualityTransition<Integer>(stateC, 100));
	}

	@Test
	public void testMachine() {
		StateMachine<Integer,Integer> machine = new StateMachine<Integer,Integer>(graph, new DefaultInputAdapter<Integer>());

		TestEventListener<Integer> listener = new TestEventListener<Integer>(EventType.COMPOSITE_STATE_ENTERED, EventType.COMPOSITE_STATE_EXITED);
		machine.addEventListener(listener);

		machine.start();
		listener.assertEventsOccurred();

		machine.evaluateInput(1);
		listener.assertEventsOccurred(
				Event.forCompositeStateEntry(outerComposite));

		listener.clear();
		machine.evaluateInput(2);
		listener.assertEventsOccurred(
				Event.forCompositeStateEntry(firstInnerComposite));

		listener.clear();
		machine.evaluateInput(3);
		listener.assertEventsOccurred(
				Event.forCompositeStateExit(firstInnerComposite),
				Event.forCompositeStateEntry(secondInnerComposite));

		listener.clear();
		machine.evaluateInput(4);
		listener.assertEventsOccurred();

		listener.clear();
		machine.evaluateInput(5);
		listener.assertEventsOccurred(
				Event.forCompositeStateExit(secondInnerComposite),
				Event.forCompositeStateExit(outerComposite));
	}

    @Test
    @SuppressWarnings("unchecked")
	public void testTransition() {
		StateMachine<Integer,Integer> machine = new StateMachine<Integer,Integer>(graph, new DefaultInputAdapter<Integer>());

		TestEventListener<Integer> listener = new TestEventListener<Integer>(EventType.COMPOSITE_STATE_ENTERED, EventType.COMPOSITE_STATE_EXITED);
		machine.addEventListener(listener);

		machine.transition(stateD);
		listener.assertEventsOccurred(
				Event.forCompositeStateEntry(outerComposite),
				Event.forCompositeStateEntry(secondInnerComposite));

		listener.clear();
		machine.transition(stateB);
		listener.assertEventsOccurred(
				Event.forCompositeStateExit(secondInnerComposite));
	}

	@Test
	public void testTransitioningFromComposite() {
		StateMachine<Integer,Integer> machine = new StateMachine<Integer,Integer>(graph, new DefaultInputAdapter<Integer>());
		machine.overrideState(stateE);

		machine.evaluateInput(200);
		assertEquals(stateE, machine.getState());

		machine.evaluateInput(100);
		assertEquals(stateC, machine.getState());
	}
}


