package com.coalmine.jstately.graph.transition;

import static org.junit.Assert.*;

import org.junit.Test;

public class EqualityTransitionTest {
	@Test
	public void testIsValidWithNonNullValidInput() {
		Transition<Integer> transition = new EqualityTransition<Integer>(null, 1);

		assertFalse("Transition should not be valid", transition.isValid(null));
		assertFalse("Transition should not be valid", transition.isValid(0));
		assertTrue("Transition should be valid", transition.isValid(1));
	}

	@Test
	public void testIsValidWithNullValidInput() {
		Transition<Integer> transition = new EqualityTransition<Integer>(null, null);

		assertTrue("Transition should be valid", transition.isValid(null));
		assertFalse("Transition should not be valid", transition.isValid(0));
		assertFalse("Transition should not be valid", transition.isValid(1));
	}
}


