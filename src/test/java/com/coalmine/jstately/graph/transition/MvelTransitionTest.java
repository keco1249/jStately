package com.coalmine.jstately.graph.transition;

import static org.junit.Assert.*;

import org.junit.Test;

import com.coalmine.jstately.graph.transition.MvelTransition;



public class MvelTransitionTest {
	@Test
	public void testIsValid() {
		MvelTransition<TestInput> transition = new MvelTransition<TestInput>();
		transition.setValidityTestExpression("stringValue=='Testing' && intValue==15");

		assertTrue(transition.isValid(new TestInput("Testing", 15)));
		assertFalse(transition.isValid(new TestInput("Unexpected value", 15)));
	}


	public static class TestInput {
		private String	stringValue;
		private int		intValue;

		public TestInput(String	stringValue, int intValue) {
	        this.stringValue = stringValue;
	        this.intValue = intValue;
        }

		public String getStringValue() {
			return stringValue;
		}

		public int getIntValue() {
			return intValue;
		}
	}
}




