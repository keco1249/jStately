package com.coalmine.jstately.machine.listener;

import com.coalmine.jstately.graph.state.BaseState;
import com.coalmine.jstately.graph.state.NonFinalState;
import com.coalmine.jstately.graph.transition.Transition;

/** Convenience implementation of the {@link StateMachineEventListener} interface with no-op methods that can be overridden as needed. */
public class DefaultStateMachineEventListener<TransitionInput> implements StateMachineEventListener<TransitionInput> {
	public void beforeStateEntered(BaseState state) { }
	public void afterStateEntered(BaseState state) { }

	public void beforeStateExited(NonFinalState state) { }
	public void afterStateExited(NonFinalState state) { }

	public void beforeTransition(Transition<TransitionInput> transition, TransitionInput input) { }
	public void afterTransition(Transition<TransitionInput> transition, TransitionInput input) { }

	public void noValidTransition(TransitionInput input) { }
}


