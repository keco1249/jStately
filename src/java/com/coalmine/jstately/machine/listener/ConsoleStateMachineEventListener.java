package com.coalmine.jstately.machine.listener;

import com.coalmine.jstately.graph.section.Section;
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
	public void beforeSectionEntered(Section section) {
		System.out.println("Before entering section ("+section+")");
	}

	@Override
	public void afterSectionEntered(Section section) {
		System.out.println("After entering section ("+section+")");
	}

	@Override
	public void beforeSectionExited(Section section) {
		System.out.println("Before exiting section ("+section+")");
	}

	@Override
	public void afterSectionExited(Section section) {
		System.out.println("After exiting section ("+section+")");
	}
}


