package com.coalmine.jstately.machine;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.coalmine.jstately.graph.StateGraph;
import com.coalmine.jstately.graph.composite.CompositeState;
import com.coalmine.jstately.graph.state.DefaultState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.EqualityTransition;

public class CompositeStateTest {
	private static StateGraph<Integer> graph;
	private static State<Integer> stateA;
	private static State<Integer> stateB;
	private static State<Integer> stateC;
	private static State<Integer> stateD;
	private static State<Integer> stateE;
	private static State<Integer> stateF;

	private static boolean outerCompositeEntered;
	private static boolean outerCompositeExited;
	private static boolean firstInnerCompositeEntered;
	private static boolean firstInnerCompositeExited;
	private static boolean secondInnerCompositeEntered;
	private static boolean secondInnerCompositeExited;

	@BeforeClass
	public static void setUpBeforeClass() {
		//     _______________________
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
				outerCompositeEntered = true;
			}
			@Override
			public void onExit() {
				outerCompositeExited = true;
			}
		};
		outerComposite.addState(stateB);

		CompositeState<Integer> firstInnerComposite = new CompositeState<Integer>() {
			@Override
			public void onEnter() {
				firstInnerCompositeEntered = true;
			}
			@Override
			public void onExit() {
				firstInnerCompositeExited = true;
			}
		};
		outerComposite.addComposite(firstInnerComposite);
		firstInnerComposite.addState(stateC);

		CompositeState<Integer> secondInnerComposite = new CompositeState<Integer>() {
			@Override
			public void onEnter() {
				secondInnerCompositeEntered = true;
			}
			@Override
			public void onExit() {
				secondInnerCompositeExited = true;
			}
		};
		outerComposite.addComposite(secondInnerComposite);
		secondInnerComposite.addState(stateD);
		secondInnerComposite.addState(stateE);
		secondInnerComposite.addTransition(new EqualityTransition<Integer>(stateC, 100));
	}


	@Before
	public void reset() {
		outerCompositeEntered = false;
		outerCompositeExited = false;
		firstInnerCompositeEntered = false;
		firstInnerCompositeExited = false;
		secondInnerCompositeEntered = false;
		secondInnerCompositeExited = false;
	}

	@Test
	public void testMachine() {
		StateMachine<Integer,Integer> machine = new StateMachine<Integer,Integer>(graph, new DefaultInputAdapter<Integer>());
		machine.start();

		assertFalse(outerCompositeEntered);
		assertFalse(outerCompositeExited);
		assertFalse(firstInnerCompositeEntered);
		assertFalse(firstInnerCompositeExited);
		assertFalse(secondInnerCompositeEntered);
		assertFalse(secondInnerCompositeExited);

		machine.evaluateInput(1);
		assertTrue(outerCompositeEntered);
		assertFalse(outerCompositeExited);
		assertFalse(firstInnerCompositeEntered);
		assertFalse(firstInnerCompositeExited);
		assertFalse(secondInnerCompositeEntered);
		assertFalse(secondInnerCompositeExited);

		machine.evaluateInput(2);
		assertTrue(outerCompositeEntered);
		assertFalse(outerCompositeExited);
		assertTrue(firstInnerCompositeEntered);
		assertFalse(firstInnerCompositeExited);
		assertFalse(secondInnerCompositeEntered);
		assertFalse(secondInnerCompositeExited);

		machine.evaluateInput(3);
		assertTrue(outerCompositeEntered);
		assertFalse(outerCompositeExited);
		assertTrue(firstInnerCompositeEntered);
		assertTrue(firstInnerCompositeExited);
		assertTrue(secondInnerCompositeEntered);
		assertFalse(secondInnerCompositeExited);

		machine.evaluateInput(4);
		assertTrue(outerCompositeEntered);
		assertFalse(outerCompositeExited);
		assertTrue(firstInnerCompositeEntered);
		assertTrue(firstInnerCompositeExited);
		assertTrue(secondInnerCompositeEntered);
		assertFalse(secondInnerCompositeExited);

		machine.evaluateInput(5);
		assertTrue(outerCompositeEntered);
		assertTrue(outerCompositeExited);
		assertTrue(firstInnerCompositeEntered);
		assertTrue(firstInnerCompositeExited);
		assertTrue(secondInnerCompositeEntered);
		assertTrue(secondInnerCompositeExited);
	}

	@Test
	public void testSettingState() {
		StateMachine<Integer,Integer> machine = new StateMachine<Integer,Integer>(graph, new DefaultInputAdapter<Integer>());
		assertFalse(outerCompositeEntered);
		assertFalse(outerCompositeExited);
		assertFalse(firstInnerCompositeEntered);
		assertFalse(firstInnerCompositeExited);
		assertFalse(secondInnerCompositeEntered);
		assertFalse(secondInnerCompositeExited);

		machine.transition(stateD);
		assertTrue(outerCompositeEntered);
		assertFalse(outerCompositeExited);
		assertFalse(firstInnerCompositeEntered);
		assertFalse(firstInnerCompositeExited);
		assertTrue(secondInnerCompositeEntered);
		assertFalse(secondInnerCompositeExited);

		reset();

		machine.transition(stateB);

		assertFalse(outerCompositeEntered);
		assertFalse(outerCompositeExited);
		assertFalse(firstInnerCompositeEntered);
		assertFalse(firstInnerCompositeExited);
		assertFalse(secondInnerCompositeEntered);
		assertTrue(secondInnerCompositeExited);
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


