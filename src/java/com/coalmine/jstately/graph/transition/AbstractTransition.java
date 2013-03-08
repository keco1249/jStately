package com.coalmine.jstately.graph.transition;

import com.coalmine.jstately.graph.state.State;



/** Provides a basic Transition implementation with a head, tail and description but no isValid() method. */
public abstract class AbstractTransition<TransitionInput> implements Transition<TransitionInput> {
	protected String	description;
	protected State		head;
	protected State		tail;


	public AbstractTransition() { }

	public AbstractTransition(State tail, State head) {
		this.tail = tail;
		this.head = head;
	}

	public AbstractTransition(State tail, State head, String description) {
		this(tail,head);
		this.description = description;
	}


	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public State getHead() {
		return head;
	}
	public void setHead(State head) {
		this.head = head;
	}

	public State getTail() {
		return tail;
	}
	public void setTail(State tail) {
		this.tail = tail;
	}

	public void onTransition() { }


	public String toString() {
		return getClass().getSimpleName()+" [description="+getDescription()+"]";
	}
}


