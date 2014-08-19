package com.coalmine.jstately.graph.transition;

import java.io.Serializable;

import org.mvel.MVEL;


/** A {@link Transition} implementation whose {@link #isValid(Object)} and {@link #onTransition(Object)} behavior is
 * determined by evaluating <a href="http://mvel.codehaus.org/">MVEL</a> expressions, allowing its behavior to be
 * specified in configuration, rather than code. */
public class MvelTransition<TransitionInput> extends AbstractTransition<TransitionInput> {
	private Serializable compiledValidityExpression;
	private Serializable compiledTransitionExpression;

	/** @param mvelExpression An MVEL expression string used to determine 
	 * if the transition is valid.  The expression must return a boolean. */
	public void setValidityTestExpression(String mvelExpression) {
		compiledValidityExpression = MVEL.compileExpression(mvelExpression);
	}

	/** @param mvelExpression An MVEL expression string used to determine 
	 * if the transition is valid.  The expression must return a boolean. */
	public void setTransitionExpression(String mvelExpression) {
		compiledTransitionExpression = MVEL.compileExpression(mvelExpression);
	}

	public boolean isValid(TransitionInput input) {
		return (Boolean)MVEL.executeExpression(compiledValidityExpression, input, Boolean.class);
	}

	@Override
	public void onTransition(TransitionInput input) {
		if(compiledTransitionExpression != null) {
			MVEL.executeExpression(compiledValidityExpression);
		}
	}
}


