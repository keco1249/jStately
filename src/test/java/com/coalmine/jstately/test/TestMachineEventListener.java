package com.coalmine.jstately.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.coalmine.jstately.graph.composite.CompositeState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.Transition;
import com.coalmine.jstately.machine.listener.DefaultStateMachineEventListener;
import com.coalmine.jstately.machine.listener.StateMachineEventListener;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


/** {@link StateMachineEventListener} implementation for use in test cases.  This listener stores events that occur,
 * allowing users to assert that a certain sequence of events occurred, using {@link #assertEventsOccurred(Event...)}. */
public class TestMachineEventListener<TransitionInput> extends DefaultStateMachineEventListener<TransitionInput> {
	private List<Event> events = new ArrayList<Event>();
	private Set<EventType> loggedEventTypes;


	/** Creates a listener that records only the given events, or all events if none are provided. */
	public TestMachineEventListener(EventType... types) {
		if(types.length == 0) {
			loggedEventTypes = null;
		} else {
			loggedEventTypes = Sets.newHashSet(types);
		}
    }

	/** Asserts that the given Events (and only the given Events) occurred in the given order.  Keep in
	 * mind that the observed Events are limited to the EventTypes given when constructing the listener. */
	public void assertEventsOccurred(Event... expectedEvents) {
		assertEquals("Expected events not found",
				Lists.newArrayList(expectedEvents),
				events);
	}

	/** Clears the list of Events that have been observed. */
	public void clearObservedEvents() {
		events.clear();
	}

	private void logEvent(EventType type, Object value) {
	    if(loggedEventTypes==null || loggedEventTypes.contains(type)) {
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


