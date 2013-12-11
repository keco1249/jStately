package com.coalmine.jstately;

import java.util.ArrayList;
import java.util.List;

import com.coalmine.jstately.graph.composite.CompositeState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.Transition;
import com.coalmine.jstately.machine.listener.DefaultStateMachineEventListener;

public class TestEventListener<TransitionInput> extends DefaultStateMachineEventListener<TransitionInput> {
	private List<Event> events = new ArrayList<TestEventListener.Event>();

	@Override
	public void afterStateEntered(State<TransitionInput> state) {
		events.add(new Event(EventType.STATE_ENTERED, state));
	}

	@Override
	public void afterStateExited(State<TransitionInput> state) {
		events.add(new Event(EventType.STATE_EXITED, state));
	}

	@Override
	public void afterCompositeStateEntered(CompositeState<TransitionInput> composite) {
		events.add(new Event(EventType.COMPOSITE_STATE_ENTERED, composite));
	}

	@Override
	public void afterCompositeStateExited(CompositeState<TransitionInput> composite) {
		events.add(new Event(EventType.COMPOSITE_STATE_EXITED, composite));
	}

	@Override
	public void afterTransition(Transition<TransitionInput> transition, TransitionInput input) {
		events.add(new Event(EventType.TRANSITION_FOLLOWED, transition));
	}


	public static class Event {
		private EventType type;
		private Object value;

		public Event(EventType type, Object value) {
			this.type = type;
			this.value = value;
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
	}


	public enum EventType {
		STATE_ENTERED, STATE_EXITED,
		COMPOSITE_STATE_ENTERED, COMPOSITE_STATE_EXITED,
		TRANSITION_FOLLOWED
	}
}


