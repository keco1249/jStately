package com.coalmine.jstately.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.coalmine.jstately.graph.DefaultState;
import com.coalmine.jstately.graph.EqualityTransition;
import com.coalmine.jstately.graph.State;
import com.coalmine.jstately.graph.StateGraph;
import com.coalmine.jstately.graph.Transition;



public class StateGraphTest {
	private static StateGraph<Integer> graph;
	private static State stateS,stateA,stateB,stateF;
	private static Transition<Integer> transitionSA,transitionAB,transitionBB,transitionBA,transitionAF;

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Setup test graph
		//
		// S → A ↔ B* (loop to self)
		//     ↓
		//     F

		stateS = new DefaultState("S");
		stateA = new DefaultState("A");
		stateB = new DefaultState("B");
		stateF = new DefaultState("F");

		transitionSA = new EqualityTransition<Integer>(stateS,stateA,1);
		transitionAB = new EqualityTransition<Integer>(stateA,stateB,2);
		transitionBB = EqualityTransition.selfTransition(stateB,3);
		transitionBA = new EqualityTransition<Integer>(stateB,stateA,4);
		transitionAF = new EqualityTransition<Integer>(stateA,stateF,5);

		graph = new StateGraph<Integer>();
		graph.setStates(stateS,stateA,stateB,stateF);
		graph.setTransitions(transitionSA,transitionAB,transitionBB,transitionBA,transitionAF);
	}

	@Test
	public void testGetTransitionsFromTail() {
		Set<Transition<Integer>> transitions = graph.getTransitionsFromTail(stateS);
		assertNotNull(transitions);
		assertEquals(1, transitions.size());
		assertTrue(transitions.contains(transitionSA));

		transitions = graph.getTransitionsFromTail(stateA);
		assertNotNull(transitions);
		assertEquals(2, transitions.size());
		assertTrue(transitions.contains(transitionAB));
		assertTrue(transitions.contains(transitionAF));

		transitions = graph.getTransitionsFromTail(stateB);
		assertNotNull(transitions);
		assertEquals(2, transitions.size());
		assertTrue(transitions.contains(transitionBB));
		assertTrue(transitions.contains(transitionBA));

		transitions = graph.getTransitionsFromTail(stateF);
		assertNotNull(transitions);
		assertEquals(0, transitions.size());
	}

	@Test
	public void testGetStatesFromTail() {
		Set<State> transitions = graph.getStatesFromTail(stateS);
		assertNotNull(transitions);
		assertEquals(1, transitions.size());
		assertTrue(transitions.contains(stateA));

		transitions = graph.getStatesFromTail(stateA);
		assertNotNull(transitions);
		assertEquals(2, transitions.size());
		assertTrue(transitions.contains(stateB));
		assertTrue(transitions.contains(stateF));

		transitions = graph.getStatesFromTail(stateB);
		assertNotNull(transitions);
		assertEquals(2, transitions.size());
		assertTrue(transitions.contains(stateB));
		assertTrue(transitions.contains(stateA));

		transitions = graph.getStatesFromTail(stateF);
		assertNotNull(transitions);
		assertEquals(0, transitions.size());
	}

	@Test
	public void testGetValidTransitionsFromTail() {
		Set<Transition<Integer>> transitions = graph.getValidTransitionsFromTail(stateS,1);
		assertNotNull(transitions);
		assertEquals(1, transitions.size());
		assertTrue(transitions.contains(transitionSA));

		transitions = graph.getValidTransitionsFromTail(stateA,2);
		assertNotNull(transitions);
		assertEquals(1, transitions.size());
		assertTrue(transitions.contains(transitionAB));

		transitions = graph.getValidTransitionsFromTail(stateA,5);
		assertNotNull(transitions);
		assertEquals(1, transitions.size());
		assertTrue(transitions.contains(transitionAF));

		transitions = graph.getValidTransitionsFromTail(stateB,3);
		assertNotNull(transitions);
		assertEquals(1, transitions.size());
		assertTrue(transitions.contains(transitionBB));

		transitions = graph.getValidTransitionsFromTail(stateB,4);
		assertNotNull(transitions);
		assertEquals(1, transitions.size());
		assertTrue(transitions.contains(transitionBA));

		transitions = graph.getValidTransitionsFromTail(stateF,5);
		assertNotNull(transitions);
		assertEquals(0, transitions.size());
	}

	@Test
	public void testGetValidStatesFromTail() {
		Set<State> transitions = graph.getValidStatesFromTail(stateS,1);
		assertNotNull(transitions);
		assertEquals(1, transitions.size());
		assertTrue(transitions.contains(stateA));

		transitions = graph.getValidStatesFromTail(stateA,2);
		assertNotNull(transitions);
		assertEquals(1, transitions.size());
		assertTrue(transitions.contains(stateB));

		transitions = graph.getValidStatesFromTail(stateA,5);
		assertNotNull(transitions);
		assertEquals(1, transitions.size());
		assertTrue(transitions.contains(stateF));

		transitions = graph.getValidStatesFromTail(stateB,3);
		assertNotNull(transitions);
		assertEquals(1, transitions.size());
		assertTrue(transitions.contains(stateB));

		transitions = graph.getValidStatesFromTail(stateB,4);
		assertNotNull(transitions);
		assertEquals(1, transitions.size());
		assertTrue(transitions.contains(stateA));

		transitions = graph.getValidStatesFromTail(stateF,4);
		assertNotNull(transitions);
		assertEquals(0, transitions.size());
	}

	@Test
	public void testGetFirstValidTransitionFromTail() {
		assertEquals(transitionSA,
				graph.getFirstValidTransitionFromTail(stateS,1));

		assertEquals(transitionAB,
				graph.getFirstValidTransitionFromTail(stateA,2));

		assertEquals(transitionAF,
				graph.getFirstValidTransitionFromTail(stateA,5));

		assertEquals(transitionBB,
				graph.getFirstValidTransitionFromTail(stateB,3));

		assertEquals(transitionBA,
				graph.getFirstValidTransitionFromTail(stateB,4));

		assertNull(graph.getFirstValidTransitionFromTail(stateF,5));
	}
}




