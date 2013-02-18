package com.coalmines.jstately.graph;

import java.util.Arrays;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.coalmines.jstately.util.EqualityUtil;


/**
 * Provides a basic Transition implementation with a head, tail and description but no isValid() method.
 */
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


	@Override
	public boolean equals(Object obj) {
		try {
			Transition<?> transition = (Transition<?>)obj;
			return EqualityUtil.objectsAreEqual(tail, transition.getTail()) &&
					EqualityUtil.objectsAreEqual(head, transition.getHead());
		} catch(ClassCastException e) {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Arrays.asList(tail, head).hashCode();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}


