package com.coalmine.jstately.graph.composite;

import static org.junit.Assert.*;

import org.junit.Test;

import com.coalmine.jstately.graph.composite.CompositeState;
import com.coalmine.jstately.graph.state.DefaultState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.Transition;
import com.coalmine.jstately.test.FixedValidityTransition;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class CompositeStateTest {
	@Test
	public void testAddComposite() {
		CompositeState<Object> parentComposite = new CompositeState<Object>();
		CompositeState<Object> childComposite = new CompositeState<Object>();

		parentComposite.addComposite(childComposite);

		assertEquals("When a child is added to a parent, that relationship is represented by the child maintaining a reference to its parent.",
				childComposite.getParent(),
				parentComposite);
	}

    @Test
    @SuppressWarnings("unchecked")
	public void testAddState() {
		State<Object> state = new DefaultState<Object>();
		CompositeState<Object> firstCompositeState = new CompositeState<Object>();
		CompositeState<Object> secondCompositeState = new CompositeState<Object>();

		firstCompositeState.addState(state);

		assertEquals("The state should have a reference to only the composite that the state was added to",
				Lists.newArrayList(firstCompositeState),
				state.getComposites());

		secondCompositeState.addState(state);

		assertEquals("The state should have a reference to both of the composites that the state was added to",
				Lists.newArrayList(firstCompositeState, secondCompositeState),
				state.getComposites());
	}

    @Test
    @SuppressWarnings("unchecked")
	public void testAddStates() {
		State<Object> firstState = new DefaultState<Object>();
		State<Object> secondState = new DefaultState<Object>();
		CompositeState<Object> compositeState = new CompositeState<Object>();

		compositeState.addStates(firstState, secondState);

		assertEquals("After calling addStates(), all of the given states should have a reference to the composite.",
				Lists.newArrayList(compositeState),
				firstState.getComposites());
		assertEquals("After calling addStates(), all of the given states should have a reference to the composite.",
				Lists.newArrayList(compositeState),
				secondState.getComposites());
	}

    @Test
    @SuppressWarnings("unchecked")
	public void testAddTransition() {
		CompositeState<Object> composite = new CompositeState<Object>();
		Transition<Object> firstTransition = new FixedValidityTransition<Object>();

		composite.addTransition(firstTransition);

		assertEquals("A composite should maintain a reference to any transition added to it.",
				Sets.newHashSet(firstTransition),
				composite.getTransitions());

		Transition<Object> secondTransition = new FixedValidityTransition<Object>();
		composite.addTransition(secondTransition);

		assertEquals("A composite should maintain a reference to any transition added to it.",
				Sets.newHashSet(firstTransition, secondTransition),
				composite.getTransitions());
	}

	@Test
	public void testFindFirstValidTransitionWithoutValidTransition() {
		CompositeState<Object> compositeState = new CompositeState<Object>();

		Transition<Object> invalidTransition = new FixedValidityTransition<Object>(false);
		compositeState.addTransition(invalidTransition);

		assertNull("No valid transitions should have been found",
				compositeState.findFirstValidTransition(null));
	}

	@Test
	public void testFindFirstValidTransitionWithValidTransition() {
		CompositeState<Object> compositeState = new CompositeState<Object>();

		Transition<Object> validTransition = new FixedValidityTransition<Object>(true);
		compositeState.addTransition(validTransition);

		assertEquals("The valid transition should have been returned",
				validTransition,
				compositeState.findFirstValidTransition(null));
	}

    @Test
    @SuppressWarnings("unchecked")
	public void testFindValidTransitions() {
		CompositeState<Object> compositeState = new CompositeState<Object>();

		Transition<Object> firstValidTransition = new FixedValidityTransition<Object>(true);
		compositeState.addTransition(firstValidTransition);

		Transition<Object> secondValidTransition = new FixedValidityTransition<Object>(true);
		compositeState.addTransition(secondValidTransition);
 
		Transition<Object> firstInvalidTransition = new FixedValidityTransition<Object>(false);
		compositeState.addTransition(firstInvalidTransition);

		Transition<Object> secondInvalidTransition = new FixedValidityTransition<Object>(false);
		compositeState.addTransition(secondInvalidTransition);

		assertEquals("The valid transition should have been returned",
				Sets.newHashSet(firstValidTransition, secondValidTransition),
				compositeState.findValidTransitions(null));
	}
}


