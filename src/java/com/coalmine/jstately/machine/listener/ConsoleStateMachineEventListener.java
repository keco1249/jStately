package com.coalmine.jstately.machine.listener;

import com.coalmine.jstately.graph.composite.CompositeState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.Transition;


/** Event listener that logs events to System.out. */
public class ConsoleStateMachineEventListener<TransitionInput> implements StateMachineEventListener<TransitionInput> {
	public void beforeStateEntered(State<TransitionInput> state) {
		System.out.println("Before state ("+state+") entered");
	}

	public void afterStateEntered(State<TransitionInput> state) {
		System.out.println("After state ("+state+") entered");
	}

	public void beforeStateExited(State<TransitionInput> state) {
		System.out.println("Before state ("+state+") exited");
	}

	public void afterStateExited(State<TransitionInput> state) {
		System.out.println("After state ("+state+") exited");
	}

	public void beforeTransition(Transition<TransitionInput> transition, TransitionInput input) {
		System.out.println("Before following transition ("+transition+") for input ("+input+")");
	}

	public void afterTransition(Transition<TransitionInput> transition, TransitionInput input) {
		System.out.println("After following transition ("+transition+") for input ("+input+")");
	}

	public void noValidTransition(TransitionInput input) {
		System.out.println("No transition found for input ("+input+")");
	}

	@Override
	public void beforeCompositeStateEntered(CompositeState<TransitionInput> composite) {
		System.out.println("Before entering composite state ("+composite+")");
	}

	@Override
	public void afterCompositeStateEntered(CompositeState<TransitionInput> composite) {
		System.out.println("After entering composite state ("+composite+")");
	}

	@Override
	public void beforeCompositeStateExited(CompositeState<TransitionInput> composite) {
		System.out.println("Before exiting composite state ("+composite+")");
	}

	@Override
	public void afterCompositeStateExited(CompositeState<TransitionInput> composite) {
		System.out.println("After exiting composite state ("+composite+")");
	}
}


