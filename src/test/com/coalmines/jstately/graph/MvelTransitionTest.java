package com.coalmines.jstately.graph;

import static org.junit.Assert.*;

import org.junit.Test;

import com.coalmines.jstately.graph.MvelTransition;



public class MvelTransitionTest {
	@Test
	public void testIsValid() {
		MvelTransition<TestInput> transition = new MvelTransition<TestInput>();
		transition.setValidityTestExpression("stringValue=='Testing' && intValue==15");

		TestInput input = new TestInput();
		input.setStringValue("Testing");
		input.setIntValue(15);
		assertTrue(transition.isValid(input));

		input.setStringValue("Unexpected value");
		assertFalse(transition.isValid(input));
	}


	public static class TestInput {
		private String	stringValue;
		private int		intValue;

		public String getStringValue() {
			return stringValue;
		}
		public void setStringValue(String stringValue) {
			this.stringValue = stringValue;
		}

		public int getIntValue() {
			return intValue;
		}
		public void setIntValue(int intValue) {
			this.intValue = intValue;
		}
	}
}




