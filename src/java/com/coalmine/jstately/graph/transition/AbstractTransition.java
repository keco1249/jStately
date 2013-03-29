package com.coalmine.jstately.graph.transition;

import com.coalmine.jstately.graph.state.State;



/** Provides a basic Transition implementation with a head, tail and description but no isValid() method. */
public abstract class AbstractTransition<TransitionInput> implements Transition<TransitionInput> {
	protected String					description;
	protected State<TransitionInput>	head;
	protected State<TransitionInput>	tail;


	public AbstractTransition() { }

	public AbstractTransition(State<TransitionInput> tail, State<TransitionInput> head) {
		this.tail = tail;
		this.head = head;
	}

	public AbstractTransition(State<TransitionInput> tail, State<TransitionInput> head, String description) {
		this(tail,head);
		this.description = description;
	}


	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public State<TransitionInput> getHead() {
		return head;
	}
	public void setHead(State<TransitionInput> head) {
		this.head = head;
	}

	public State<TransitionInput> getTail() {
		return tail;
	}
	public void setTail(State<TransitionInput> tail) {
		this.tail = tail;
	}

	public void onTransition(TransitionInput input) { }


	public String toString() {
		return getClass().getName()+"[description="+getDescription()+",tail="+tail.toString()+",head="+head.toString()+"]";
	}
}


