package com.coalmine.jstately.graph.transition;

import com.coalmine.jstately.graph.state.State;


/** Provides a basic Transition implementation with a head, tail and description but no isValid() method. */
public abstract class AbstractTransition<TransitionInput> implements Transition<TransitionInput> {
	protected State<TransitionInput> head;


	public AbstractTransition() { }

	public AbstractTransition(State<TransitionInput> head) {
		this.head = head;
	}


	public State<TransitionInput> getHead() {
		return head;
	}
	public void setHead(State<TransitionInput> head) {
		this.head = head;
	}

	public void onTransition(TransitionInput input) { }


	public String toString() {
		return getClass().getName()+"[head="+head.toString()+"]";
	}
}


