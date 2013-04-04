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

	private static boolean outerSectionEntered;
	private static boolean outerSectionExited;
	private static boolean firstInnerSectionEntered;
	private static boolean firstInnerSectionExited;
	private static boolean secondInnerSectionEntered;
	private static boolean secondInnerSectionExited;

	@BeforeClass
	public static void setUpBeforeClass() {
		//     _______________________
		//   1 |   2  ___  3  ___4___ | 5
		// A → | B → |_C_| → |_D_→_E_|| → F
		//     |______________________|
		// 
		// * Plus a transition from the CompositeState containing States D and E, to State C, valid for an input of 100

		graph = new StateGraph<Integer>();

		graph.addStartState(stateA = new DefaultState<Integer>("A"));
		graph.addState(stateB = new DefaultState<Integer>("B"));
		graph.addState(stateC = new DefaultState<Integer>("C"));
		graph.addState(stateD = new DefaultState<Integer>("D"));
		graph.addState(stateE = new DefaultState<Integer>("E"));
		graph.addState(stateF = new DefaultState<Integer>("F"));

		graph.addTransition(stateA, new EqualityTransition<Integer>(stateB, 1));
		graph.addTransition(stateB, new EqualityTransition<Integer>(stateC, 2));
		graph.addTransition(stateC, new EqualityTransition<Integer>(stateD, 3));
		graph.addTransition(stateD, new EqualityTransition<Integer>(stateE, 4));
		graph.addTransition(stateE, new EqualityTransition<Integer>(stateF, 5));

		CompositeState<Integer> outerSection = new CompositeState<Integer>() {
			@Override
			public void onEnter() {
				outerSectionEntered = true;
			}
			@Override
			public void onExit() {
				outerSectionExited = true;
			}
		};
		outerSection.addState(stateB);

		CompositeState<Integer> firstInnerSection = new CompositeState<Integer>() {
			@Override
			public void onEnter() {
				firstInnerSectionEntered = true;
			}
			@Override
			public void onExit() {
				firstInnerSectionExited = true;
			}
		};
		outerSection.addComposite(firstInnerSection);
		firstInnerSection.addState(stateC);

		CompositeState<Integer> secondInnerSection = new CompositeState<Integer>() {
			@Override
			public void onEnter() {
				secondInnerSectionEntered = true;
			}
			@Override
			public void onExit() {
				secondInnerSectionExited = true;
			}
		};
		outerSection.addComposite(secondInnerSection);
		secondInnerSection.addState(stateD);
		secondInnerSection.addState(stateE);
		secondInnerSection.addTransition(new EqualityTransition<Integer>(stateC, 100));
	}


	@Before
	public void reset() {
		outerSectionEntered = false;
		outerSectionExited = false;
		firstInnerSectionEntered = false;
		firstInnerSectionExited = false;
		secondInnerSectionEntered = false;
		secondInnerSectionExited = false;
	}

	@Test
	public void testMachine() {
		StateMachine<Integer,Integer> machine = new StateMachine<Integer,Integer>(graph, new DefaultInputAdapter<Integer>());
		machine.start();

		assertFalse(outerSectionEntered);
		assertFalse(outerSectionExited);
		assertFalse(firstInnerSectionEntered);
		assertFalse(firstInnerSectionExited);
		assertFalse(secondInnerSectionEntered);
		assertFalse(secondInnerSectionExited);

		machine.evaluateInput(1);
		assertTrue(outerSectionEntered);
		assertFalse(outerSectionExited);
		assertFalse(firstInnerSectionEntered);
		assertFalse(firstInnerSectionExited);
		assertFalse(secondInnerSectionEntered);
		assertFalse(secondInnerSectionExited);

		machine.evaluateInput(2);
		assertTrue(outerSectionEntered);
		assertFalse(outerSectionExited);
		assertTrue(firstInnerSectionEntered);
		assertFalse(firstInnerSectionExited);
		assertFalse(secondInnerSectionEntered);
		assertFalse(secondInnerSectionExited);

		machine.evaluateInput(3);
		assertTrue(outerSectionEntered);
		assertFalse(outerSectionExited);
		assertTrue(firstInnerSectionEntered);
		assertTrue(firstInnerSectionExited);
		assertTrue(secondInnerSectionEntered);
		assertFalse(secondInnerSectionExited);

		machine.evaluateInput(4);
		assertTrue(outerSectionEntered);
		assertFalse(outerSectionExited);
		assertTrue(firstInnerSectionEntered);
		assertTrue(firstInnerSectionExited);
		assertTrue(secondInnerSectionEntered);
		assertFalse(secondInnerSectionExited);

		machine.evaluateInput(5);
		assertTrue(outerSectionEntered);
		assertTrue(outerSectionExited);
		assertTrue(firstInnerSectionEntered);
		assertTrue(firstInnerSectionExited);
		assertTrue(secondInnerSectionEntered);
		assertTrue(secondInnerSectionExited);
	}

	@Test
	public void testSettingState() {
		StateMachine<Integer,Integer> machine = new StateMachine<Integer,Integer>(graph, new DefaultInputAdapter<Integer>());
		assertFalse(outerSectionEntered);
		assertFalse(outerSectionExited);
		assertFalse(firstInnerSectionEntered);
		assertFalse(firstInnerSectionExited);
		assertFalse(secondInnerSectionEntered);
		assertFalse(secondInnerSectionExited);

		machine.transition(stateD);
		assertTrue(outerSectionEntered);
		assertFalse(outerSectionExited);
		assertFalse(firstInnerSectionEntered);
		assertFalse(firstInnerSectionExited);
		assertTrue(secondInnerSectionEntered);
		assertFalse(secondInnerSectionExited);

		reset();

		machine.transition(stateB);

		assertFalse(outerSectionEntered);
		assertFalse(outerSectionExited);
		assertFalse(firstInnerSectionEntered);
		assertFalse(firstInnerSectionExited);
		assertFalse(secondInnerSectionEntered);
		assertTrue(secondInnerSectionExited);
	}

	@Test
	public void testTransitioningFromComposite() {
		StateMachine<Integer,Integer> machine = new StateMachine<Integer,Integer>(graph, new DefaultInputAdapter<Integer>());
		machine.setCurrentState(stateE);

		machine.evaluateInput(200);
		assertEquals(stateE, machine.getState());

		machine.evaluateInput(100);
		assertEquals(stateC, machine.getState());
	}
}


