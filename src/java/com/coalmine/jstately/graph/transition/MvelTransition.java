package com.coalmine.jstately.graph.transition;

import java.io.Serializable;

import org.mvel2.MVEL;


public class MvelTransition<TransitionInput> extends AbstractTransition<TransitionInput> implements Transition<TransitionInput> {
	private Serializable compiledValidityExpression		= null;
	private Serializable compiledTransitionExpression	= null;

	/**
	 * @param mvelExpression A string of <a href="http://mvel.codehaus.org/">MVEL</a> code used
	 * to determine if the Transition is valid.  The expression must ALWAYS return a Boolean.
	 */
	public void setValidityTestExpression(String mvelExpression) {
		compiledValidityExpression = MVEL.compileExpression(mvelExpression);
	}

	/**
	 * @param mvelExpression A string of <a href="http://mvel.codehaus.org/">MVEL</a> code used
	 * to determine if the Transition is valid.  The expression must always return a Boolean.
	 */
	public void setTransitionExpression(String mvelExpression) {
		compiledTransitionExpression = MVEL.compileExpression(mvelExpression);
	}

	public boolean isValid(TransitionInput input) {
		return (Boolean)MVEL.executeExpression(compiledValidityExpression, input, Boolean.class);
	}

	@Override
	public void onTransition() {
		if(compiledTransitionExpression!=null) {
			MVEL.executeExpression(compiledValidityExpression);
		}
	}
}




