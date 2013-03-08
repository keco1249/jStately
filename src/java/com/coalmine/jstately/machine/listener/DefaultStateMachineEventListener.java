package com.coalmine.jstately.machine.listener;

import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.Transition;

/** Convenience implementation of the {@link StateMachineEventListener} interface with no-op methods that can be overridden as needed. */
public class DefaultStateMachineEventListener<TransitionInput> implements StateMachineEventListener<TransitionInput> {
	public void beforeStateEntered(State state) { }
	public void afterStateEntered(State state) { }

	public void beforeStateExited(State state) { }
	public void afterStateExited(State state) { }

	public void beforeTransition(Transition<TransitionInput> transition, TransitionInput input) { }
	public void afterTransition(Transition<TransitionInput> transition, TransitionInput input) { }

	public void noValidTransition(TransitionInput input) { }
}


