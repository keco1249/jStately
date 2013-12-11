package com.coalmine.jstately.test;

import com.coalmine.jstately.graph.composite.CompositeState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.Transition;

public class Event {
	private EventType type;
	private Object value;

	Event(EventType type, Object value) { // Purposely package-protected
		this.type = type;
		this.value = value;
	}

	public static Event forStateEntry(State<?> state) {
		return new Event(EventType.STATE_ENTERED, state);
	}

	public static Event forStateExit(State<?> state) {
		return new Event(EventType.STATE_EXITED, state);
	}

	public static Event forCompositeStateEntry(CompositeState<?> composite) {
		return new Event(EventType.COMPOSITE_STATE_ENTERED, composite);
	}

	public static Event forCompositeStateExit(CompositeState<?> composite) {
		return new Event(EventType.COMPOSITE_STATE_EXITED, composite);
	}

	public static Event forTransitionFollowed(Transition<?> transition) {
		return new Event(EventType.TRANSITION_FOLLOWED, transition);
	}

	public static Event forNoTransitionFound() {
		return new Event(EventType.NO_VALID_TRANSITION_FOUND, null);
	}

	@Override
	public boolean equals(Object object) {
		if(!(object instanceof Event)) {
			return false;
		}

		Event otherEvent = (Event)object;
		return type.equals(otherEvent.type)
				&& value.equals(otherEvent.value);
	}

	// TODO It shouldn't come into play here but when a class overrides equals(), it should also override hashCode().  Just something to think about...

	@Override
	public String toString() {
	    return super.toString()+"[type="+type.name()+",value="+value+"]";
	}
}


