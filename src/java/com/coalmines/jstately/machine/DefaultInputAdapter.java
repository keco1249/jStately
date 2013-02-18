package com.coalmines.jstately.machine;

import java.util.LinkedList;
import java.util.Queue;


/**
 * Default input adapter for machines that use the same input type as their state graph's transitions.
 */
public class DefaultInputAdapter<InputType> implements InputAdapter<InputType,InputType> {
	private Queue<InputType> queue = new LinkedList<InputType>();

	public void queueInput(InputType input) {
		queue.add(input);
	}

	public boolean hasNext() {
		return !queue.isEmpty();
	}

	public InputType next() {
		return queue.poll();
	}

	public InputType peek() {
		return queue.peek();
	}
}




