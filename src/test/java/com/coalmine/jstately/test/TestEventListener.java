package com.coalmine.jstately.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.coalmine.jstately.graph.composite.CompositeState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.Transition;
import com.coalmine.jstately.machine.listener.DefaultStateMachineEventListener;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


public class TestEventListener<TransitionInput> extends DefaultStateMachineEventListener<TransitionInput> {
	private List<Event> events = new ArrayList<Event>();
	private Set<EventType> typesToLog;


	public TestEventListener(EventType... types) {
		if(types.length == 0) {
			typesToLog = null;
		} else {
			typesToLog = Sets.newHashSet(types);
		}
    }

	public void assertEventsOccurred(Event... expectedEvents) {
		assertEquals("Expected events not found",
				Lists.newArrayList(expectedEvents),
				events);
	}

	public void clear() {
		events.clear();
	}

	private void logEvent(EventType type, Object value) {
	    if(typesToLog==null || typesToLog.contains(type)) {
	    	events.add(new Event(type, value));
	    }
    }

	@Override
	public void afterStateEntered(State<TransitionInput> state) {
		logEvent(EventType.STATE_ENTERED, state);
	}

	@Override
	public void afterStateExited(State<TransitionInput> state) {
		logEvent(EventType.STATE_EXITED, state);
	}

	@Override
	public void afterCompositeStateEntered(CompositeState<TransitionInput> composite) {
		logEvent(EventType.COMPOSITE_STATE_ENTERED, composite);
	}

	@Override
	public void afterCompositeStateExited(CompositeState<TransitionInput> composite) {
		logEvent(EventType.COMPOSITE_STATE_EXITED, composite);
	}

	@Override
	public void afterTransition(Transition<TransitionInput> transition, TransitionInput input) {
		logEvent(EventType.TRANSITION_FOLLOWED, transition);
	}

	@Override
	public void noValidTransition(TransitionInput input) {
		logEvent(EventType.NO_VALID_TRANSITION_FOUND, input);
	}
}


