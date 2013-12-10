package com.coalmine.jstately.machine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.coalmine.jstately.graph.StateGraph;
import com.coalmine.jstately.graph.composite.CompositeState;
import com.coalmine.jstately.graph.state.DefaultState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.EqualityTransition;
import com.google.common.collect.Lists;

public class CompositeStateTest {
	private static StateGraph<Integer> graph;

	private static State<Integer> stateA;
	private static State<Integer> stateB;
	private static State<Integer> stateC;
	private static State<Integer> stateD;
	private static State<Integer> stateE;
	private static State<Integer> stateF;

	private static List<Event> events = new ArrayList<Event>();

	private enum Event {
		OUTER_COMPOSITE_ENTERED,
		OUTER_COMPOSITE_EXITED,
		FIRST_INNER_COMPOSITE_ENTERED,
		FIRST_INNER_COMPOSITE_EXITED,
		SECOND_INNER_COMPOSITE_ENTERED,
		SECOND_INNER_COMPOSITE_EXITED
	}

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

		CompositeState<Integer> outerComposite = new CompositeState<Integer>() {
			@Override
			public void onEnter() {
				events.add(Event.OUTER_COMPOSITE_ENTERED);
			}
			@Override
			public void onExit() {
				events.add(Event.OUTER_COMPOSITE_EXITED);
			}
		};
		outerComposite.addState(stateB);

		CompositeState<Integer> firstInnerComposite = new CompositeState<Integer>() {
			@Override
			public void onEnter() {
				events.add(Event.FIRST_INNER_COMPOSITE_ENTERED);
			}
			@Override
			public void onExit() {
				events.add(Event.FIRST_INNER_COMPOSITE_EXITED);
			}
		};
		outerComposite.addComposite(firstInnerComposite);
		firstInnerComposite.addState(stateC);

		CompositeState<Integer> secondInnerComposite = new CompositeState<Integer>() {
			@Override
			public void onEnter() {
				events.add(Event.SECOND_INNER_COMPOSITE_ENTERED);
			}
			@Override
			public void onExit() {
				events.add(Event.SECOND_INNER_COMPOSITE_EXITED);
			}
		};
		outerComposite.addComposite(secondInnerComposite);
		secondInnerComposite.addState(stateD);
		secondInnerComposite.addState(stateE);
		secondInnerComposite.addTransition(new EqualityTransition<Integer>(stateC, 100));
	}

	@Before
	public void reset() {
		events.clear();
	}

	@Test
	public void testMachine() {
		StateMachine<Integer,Integer> machine = new StateMachine<Integer,Integer>(graph, new DefaultInputAdapter<Integer>());

		machine.start();
		assertTrue(events.isEmpty());

		machine.evaluateInput(1);
		assertEquals(Lists.newArrayList(Event.OUTER_COMPOSITE_ENTERED),
				events);

		events.clear();
		machine.evaluateInput(2);
		assertEquals(Lists.newArrayList(Event.FIRST_INNER_COMPOSITE_ENTERED),
				events);

		events.clear();
		machine.evaluateInput(3);
		assertEquals(Lists.newArrayList(Event.FIRST_INNER_COMPOSITE_EXITED, Event.SECOND_INNER_COMPOSITE_ENTERED),
				events);

		events.clear();
		machine.evaluateInput(4);
		assertTrue(events.isEmpty());

		events.clear();
		machine.evaluateInput(5);
		assertEquals(Lists.newArrayList(Event.SECOND_INNER_COMPOSITE_EXITED, Event.OUTER_COMPOSITE_EXITED),
				events);
	}

	@Test
	public void testTransition() {
		StateMachine<Integer,Integer> machine = new StateMachine<Integer,Integer>(graph, new DefaultInputAdapter<Integer>());

		machine.transition(stateD);
		assertEquals(Lists.newArrayList(Event.OUTER_COMPOSITE_ENTERED, Event.SECOND_INNER_COMPOSITE_ENTERED),
				events);

		reset();
		machine.transition(stateB);
		assertEquals(Lists.newArrayList(Event.SECOND_INNER_COMPOSITE_EXITED),
				events);
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


