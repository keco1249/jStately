package com.coalmine.jstately.machine;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.coalmine.jstately.graph.StateGraph;
import com.coalmine.jstately.graph.section.Section;
import com.coalmine.jstately.graph.state.DefaultState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.EqualityTransition;

public class SectionTest {
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

		graph = new StateGraph<Integer>();

		graph.addStartState(stateA = new DefaultState<Integer>("A"));
		graph.addState(stateB = new DefaultState<Integer>("B"));
		graph.addState(stateC = new DefaultState<Integer>("C"));
		graph.addState(stateD = new DefaultState<Integer>("D"));
		graph.addState(stateE = new DefaultState<Integer>("E"));
		graph.addState(stateF = new DefaultState<Integer>("F"));

		graph.addTransition(new EqualityTransition<Integer>(stateA, stateB, 1));
		graph.addTransition(new EqualityTransition<Integer>(stateB, stateC, 2));
		graph.addTransition(new EqualityTransition<Integer>(stateC, stateD, 3));
		graph.addTransition(new EqualityTransition<Integer>(stateD, stateE, 4));
		graph.addTransition(new EqualityTransition<Integer>(stateE, stateF, 5));

		Section outerSection = new Section() {
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

		Section firstInnerSection = new Section() {
			@Override
			public void onEnter() {
				firstInnerSectionEntered = true;
			}
			@Override
			public void onExit() {
				firstInnerSectionExited = true;
			}
		};
		outerSection.addSection(firstInnerSection);
		firstInnerSection.addState(stateC);

		Section secondInnerSection = new Section() {
			@Override
			public void onEnter() {
				secondInnerSectionEntered = true;
			}
			@Override
			public void onExit() {
				secondInnerSectionExited = true;
			}
		};
		outerSection.addSection(secondInnerSection);
		secondInnerSection.addState(stateD);
		secondInnerSection.addState(stateE);
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
}


