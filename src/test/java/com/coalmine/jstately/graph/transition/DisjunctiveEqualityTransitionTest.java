package com.coalmine.jstately.graph.transition;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.Sets;

public class DisjunctiveEqualityTransitionTest {
	@Test
	public void testIsValid() {
		DisjunctiveEqualityTransition<Integer> transition = new DisjunctiveEqualityTransition<Integer>();
		transition.setValidInputs(Sets.newHashSet(1,2));

		assertFalse("Transition should not be valid for an input not in its set of valid inputs", transition.isValid(0));
		assertTrue("Transition should be valid for an input in its set of valid inputs", transition.isValid(1));
		assertTrue("Transition should be valid for an input in its set of valid inputs", transition.isValid(2));
		assertTrue("Transition should be valid for an input in its set of valid inputs", transition.isValid(null));
	}
}


