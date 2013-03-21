package com.coalmine.jstately.machine.listener;

import com.coalmine.jstately.graph.state.BaseState;
import com.coalmine.jstately.graph.state.NonFinalState;
import com.coalmine.jstately.graph.transition.Transition;
import com.coalmine.jstately.machine.StateMachine;

/** Listener than can be registered with a {@link StateMachine} to be notified of events that happen. */
public interface StateMachineEventListener<TransitionInput> {
	/** Called before a state machine enters a State. */
	void beforeStateEntered(BaseState state);

	/** Called after a state machine enters a State. */
	void afterStateEntered(BaseState state);

	/** Called before a state machine exits a State. */
	void beforeStateExited(NonFinalState state);

	/** Called after a state machine exits a State. */
	void afterStateExited(NonFinalState state);

	/** Called before a state machine transitions from one state to another. */
	void beforeTransition(Transition<TransitionInput> transition, TransitionInput input);

	/** Called after a state machine transitions from one state to another. */
	void afterTransition(Transition<TransitionInput> transition, TransitionInput input);

	/** Called when no valid transition is found for a given input. */
	void noValidTransition(TransitionInput input);
}


