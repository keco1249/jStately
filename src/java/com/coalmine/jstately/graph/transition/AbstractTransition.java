package com.coalmine.jstately.graph.transition;

import com.coalmine.jstately.graph.state.NonFinalState;



/** Provides a basic Transition implementation with a head, tail and description but no isValid() method. */
public abstract class AbstractTransition<TransitionInput> implements Transition<TransitionInput> {
	protected String	description;
	protected NonFinalState		head;
	protected NonFinalState		tail;


	public AbstractTransition() { }

	public AbstractTransition(NonFinalState tail, NonFinalState head) {
		this.tail = tail;
		this.head = head;
	}

	public AbstractTransition(NonFinalState tail, NonFinalState head, String description) {
		this(tail,head);
		this.description = description;
	}


	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public NonFinalState getHead() {
		return head;
	}
	public void setHead(NonFinalState head) {
		this.head = head;
	}

	public NonFinalState getTail() {
		return tail;
	}
	public void setTail(NonFinalState tail) {
		this.tail = tail;
	}

	public void onTransition(TransitionInput input) { }


	public String toString() {
		return getClass().getSimpleName()+"[description="+getDescription()+",tail="+tail.toString()+",head="+head.toString()+"]";
	}
}


