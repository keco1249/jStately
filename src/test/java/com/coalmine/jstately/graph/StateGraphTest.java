package com.coalmine.jstately.graph;

import static org.junit.Assert.*;

import org.junit.Test;

import com.coalmine.jstately.graph.composite.CompositeState;
import com.coalmine.jstately.graph.state.DefaultFinalState;
import com.coalmine.jstately.graph.state.DefaultState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.Transition;
import com.coalmine.jstately.test.DefaultTransition;

public class StateGraphTest {
	@Test(expected=IllegalArgumentException.class)
	public void testSetStartStateToFinalState() {
		State<Object> startState = new DefaultFinalState<Object>();

		StateGraph<Object> graph = new StateGraph<Object>();
		graph.setStartState(startState);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddTransitionWithNullTail() {
		StateGraph<Object> graph = new StateGraph<Object>();
		graph.addTransition(null, new DefaultTransition<Object>());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddSelfTransitionWithNullHead() {
		StateGraph<Object> graph = new StateGraph<Object>();

		graph.addSelfTransition(new DefaultTransition<Object>(null));
	}

	@Test
	public void testFindFirstValidTransitionFromState_onlyGlobalValidTransition() {
		StateGraph<Object> graph = new StateGraph<Object>();
		State<Object> state = new DefaultState<Object>();

		// Wrap the state in nested composites
		CompositeState<Object> firstComposite = new CompositeState<Object>();
		firstComposite.addState(state);

		// Two more nested composites that overlap with the first two (share the state) but aren't nested
		CompositeState<Object> secondInnerComposite = new CompositeState<Object>();
		secondInnerComposite.addState(state);
		CompositeState<Object> secondOuterComposite = new CompositeState<Object>();
		secondOuterComposite.addComposite(secondInnerComposite);

		// Only an invalid global transition
		graph.addGlobalTransition(new DefaultTransition<Object>(false));
		assertNull("No valid transition should exist.",
				graph.findFirstValidTransitionFromState(state, null));

		// With a valid global transition
		Transition<Object> validGlobalTransition = new DefaultTransition<Object>(true);
		graph.addGlobalTransition(validGlobalTransition);
		assertEquals("The valid global transition should be returned.",
				validGlobalTransition,
				graph.findFirstValidTransitionFromState(state, null));

		// Matches in a state's composite ancestors should take precedence over global transitions... but still need to be valid.
		secondOuterComposite.addTransition(new DefaultTransition<Object>(false));
		assertEquals("The valid global transition should be returned.",
				validGlobalTransition,
				graph.findFirstValidTransitionFromState(state, null));

		Transition<Object> validSecondOuterCompositeTransition = new DefaultTransition<Object>(true);
		secondOuterComposite.addTransition(validSecondOuterCompositeTransition);
		assertEquals("Composite transitions should take precedence over global transitions.",
				validSecondOuterCompositeTransition,
				graph.findFirstValidTransitionFromState(state, null));

		// More immediate CompositeState ancestors should take precedence over more distant CompositeState ancestors
		secondInnerComposite.addTransition(new DefaultTransition<Object>(false));
		assertEquals("", // ...stil
				validSecondOuterCompositeTransition,
				graph.findFirstValidTransitionFromState(state, null));

		Transition<Object> validSecondInnerCompositeTransition = new DefaultTransition<Object>(true);
		secondInnerComposite.addTransition(validSecondInnerCompositeTransition);
		assertEquals("More immediate CompositeState ancestors should take precedence over more distant CompositeState ancestors",
				validSecondInnerCompositeTransition,
				graph.findFirstValidTransitionFromState(state, null));

		// When a state has multiple immediate CompositeStates, precedence should be given to Transitions from the CompositeState that the state was added to earliest. 
		firstComposite.addTransition(new DefaultTransition<Object>(false));
		assertEquals("", // ...stil
				validSecondInnerCompositeTransition,
				graph.findFirstValidTransitionFromState(state, null));

		Transition<Object> validFirstCompositeTransition = new DefaultTransition<Object>(true);
		firstComposite.addTransition(validFirstCompositeTransition);
		assertEquals("When a state has multiple immediate CompositeStates, precedence should be given to Transitions from the CompositeState that the state was added to earliest",
				validFirstCompositeTransition,
				graph.findFirstValidTransitionFromState(state, null));

		// Now check the transitions on the state itself
		graph.addTransition(state, new DefaultTransition<Object>(false));
		assertEquals("", // ...still
				validFirstCompositeTransition,
				graph.findFirstValidTransitionFromState(state, null));

		Transition<Object> validStateTransition = new DefaultTransition<Object>(true);
		graph.addTransition(state, validStateTransition);
		assertEquals("Valid transitions on the state itself should take precedence over global transitions and transitions on ancestor CompositeStates.",
				validStateTransition,
				graph.findFirstValidTransitionFromState(state, null));
	}
}


