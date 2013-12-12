package com.coalmine.jstately.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.coalmine.jstately.graph.state.DefaultState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.DisjunctiveEqualityTransition;
import com.coalmine.jstately.graph.transition.EqualityTransition;
import com.coalmine.jstately.graph.transition.Transition;


public class OldStateGraphTest {
	private static StateGraph<Integer> graph;
	private static State<Integer> stateS,stateA,stateB,stateF;
	private static Transition<Integer> transitionSA, transitionAB, transitionBB, transitionBA, transitionAF, globalTransitionS;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Setup test graph with a global transition to F for an input of 100
		//
		// S → A ↔ B* (loop to self)
		//     ↓
		//     F

		stateS = new DefaultState<Integer>("S");
		stateA = new DefaultState<Integer>("A");
		stateB = new DefaultState<Integer>("B");
		stateF = new DefaultState<Integer>("F");

		transitionSA = new DisjunctiveEqualityTransition<Integer>(stateA,1,500);
		transitionAB = new EqualityTransition<Integer>(stateB,2);
		transitionBB = new EqualityTransition<Integer>(stateB,3);
		transitionBA = new EqualityTransition<Integer>(stateA,4);
		transitionAF = new EqualityTransition<Integer>(stateF,5);
		globalTransitionS = new EqualityTransition<Integer>(stateS, 100);

		graph = new StateGraph<Integer>();
		graph.setStartState(stateS);

		graph.addTransition(stateS, transitionSA);
		graph.addTransition(stateA, transitionAB);
		graph.addSelfTransition(transitionBB);
		graph.addTransition(stateB, transitionBA);
		graph.addTransition(stateA, transitionAF);

		graph.addGlobalTransition(globalTransitionS);
	}

	@Test
	public void testGetTransitionsFromState() {
		Set<Transition<Integer>> transitions = graph.findAllTransitionsFromState(stateS);
		assertNotNull(transitions);
		assertEquals(2, transitions.size());
		assertTrue(transitions.contains(transitionSA));

		transitions = graph.findAllTransitionsFromState(stateA);
		assertNotNull(transitions);
		assertEquals(3, transitions.size());
		assertTrue(transitions.contains(transitionAB));
		assertTrue(transitions.contains(transitionAF));

		transitions = graph.findAllTransitionsFromState(stateB);
		assertNotNull(transitions);
		assertEquals(3, transitions.size());
		assertTrue(transitions.contains(transitionBB));
		assertTrue(transitions.contains(transitionBA));

		transitions = graph.findAllTransitionsFromState(stateF);
		assertNotNull(transitions);
		assertEquals(1, transitions.size());
	}


	@Test
	public void testGetValidTransitionsFromState() {
		Set<Transition<Integer>> transitions = graph.findValidTransitionsFromState(stateS,1);
		assertNotNull(transitions);
		assertEquals(1, transitions.size());
		assertTrue(transitions.contains(transitionSA));

		// Transition S→A is disjunctive.  Make sure both valid inputs are considered valid.
		transitions = graph.findValidTransitionsFromState(stateS,500);
		assertNotNull(transitions);
		assertEquals(1, transitions.size());
		assertTrue(transitions.contains(transitionSA));

		transitions = graph.findValidTransitionsFromState(stateA,2);
		assertNotNull(transitions);
		assertEquals(1, transitions.size());
		assertTrue(transitions.contains(transitionAB));

		transitions = graph.findValidTransitionsFromState(stateA,5);
		assertNotNull(transitions);
		assertEquals(1, transitions.size());
		assertTrue(transitions.contains(transitionAF));

		transitions = graph.findValidTransitionsFromState(stateB,3);
		assertNotNull(transitions);
		assertEquals(1, transitions.size());
		assertTrue(transitions.contains(transitionBB));

		transitions = graph.findValidTransitionsFromState(stateB,4);
		assertNotNull(transitions);
		assertEquals(1, transitions.size());
		assertTrue(transitions.contains(transitionBA));

		transitions = graph.findValidTransitionsFromState(stateF,5);
		assertNotNull(transitions);
		assertEquals(0, transitions.size());

		// Make sure global transition is valid from all states (with the right input of course.)
		assertTrue(graph.findValidTransitionsFromState(stateS,100).contains(globalTransitionS));
		assertTrue(graph.findValidTransitionsFromState(stateA,100).contains(globalTransitionS));
		assertTrue(graph.findValidTransitionsFromState(stateB,100).contains(globalTransitionS));
		assertTrue(graph.findValidTransitionsFromState(stateF,100).contains(globalTransitionS));
	}

	@Test
	public void testGetFirstValidTransitionFromState() {
		assertEquals(transitionSA,
				graph.findFirstValidTransitionFromState(stateS,1));
		assertEquals(transitionSA,
				graph.findFirstValidTransitionFromState(stateS,500));

		assertEquals(transitionAB,
				graph.findFirstValidTransitionFromState(stateA,2));

		assertEquals(transitionAF,
				graph.findFirstValidTransitionFromState(stateA,5));

		assertEquals(transitionBB,
				graph.findFirstValidTransitionFromState(stateB,3));

		assertEquals(transitionBA,
				graph.findFirstValidTransitionFromState(stateB,4));

		assertNull(graph.findFirstValidTransitionFromState(stateF,5));
	}
}




