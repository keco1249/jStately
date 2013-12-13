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
	private Set<EventType> allowedEventTypes;


	/** Creates a listener that records only the given events, or all events if none are provided. */
	public TestMachineEventListener(EventType... allowedEventTypes) {
		if(allowedEventTypes.length == 0) {
			this.allowedEventTypes = null;
		} else {
			this.allowedEventTypes = Sets.newHashSet(allowedEventTypes);
		}
    }

	/** Asserts that the given Events (and only the given Events) occurred in the given order.  Keep in
	 * mind that the observed Events are limited to the EventTypes given when constructing the listener. */
	public void assertEventsOccurred(Event... expectedEvents) {
		assertEquals("The exact sequence of expected events was not observed.",
				Lists.newArrayList(expectedEvents),
				events);
	}

	/** Clears the list of Events that have been observed. */
	public void clearObservedEvents() {
		events.clear();
	}

	private void logEvent(EventType type, Object value) {
	    if(allowedEventTypes==null || allowedEventTypes.contains(type)) {
	    	events.add(new Event(type, value));
	    }
    }

	@Override
	public void beforeStateEntered(State<TransitionInput> state) {
		logEvent(EventType.STATE_ENTERED, state);
	}

	@Override
	public void beforeStateExited(State<TransitionInput> state) {
		logEvent(EventType.STATE_EXITED, state);
	}

	@Override
	public void beforeCompositeStateEntered(CompositeState<TransitionInput> composite) {
		logEvent(EventType.COMPOSITE_STATE_ENTERED, composite);
	}

	@Override
	public void beforeCompositeStateExited(CompositeState<TransitionInput> composite) {
		logEvent(EventType.COMPOSITE_STATE_EXITED, composite);
	}

	@Override
	public void beforeTransition(Transition<TransitionInput> transition, TransitionInput input) {
		logEvent(EventType.TRANSITION_FOLLOWED, transition);
	}

	@Override
	public void noValidTransition(TransitionInput input) {
		logEvent(EventType.NO_VALID_TRANSITION_FOUND, input);
	}
}


