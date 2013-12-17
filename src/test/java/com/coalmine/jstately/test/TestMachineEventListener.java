package com.coalmine.jstately.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.coalmine.jstately.graph.composite.CompositeState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.Transition;
import com.coalmine.jstately.machine.StateMachine;
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
		assertEventsOccurred(true, expectedEvents);
	}

	/** Asserts that the given Events (and only the given Events) occurred in the given order.  Keep in
	 * mind that the observed Events are limited to the EventTypes given when constructing the listener. */
	public void assertEventsOccurred(boolean ignoreMachine, Event... expectedEvents) {
		List<Event> expectedEventList = Lists.newArrayList(expectedEvents);
		assertTrue("The exact sequence of expected events was not observed.  Expected: "+expectedEventList+" but was: "+events,
				testEventListEquality(ignoreMachine, expectedEventList, events));
	}

	private boolean testEventListEquality(boolean ignoreMachine, List<Event> firstEvents, List<Event> secondEvents) {
		if(firstEvents.size() != secondEvents.size()) {
			return false;
		}

		for(int i=0; i<firstEvents.size(); i++) {
			Event firstEvent = firstEvents.get(i);
			Event secondEvent = secondEvents.get(i);

			boolean typesEqual = firstEvent.getType().equals(secondEvent.getType());

			boolean valuesEqual = firstEvent.getValue()==null?
					secondEvent.getValue()==null :
					firstEvent.getValue().equals(secondEvent.getValue());

			boolean machinesEqual = ignoreMachine ||
					firstEvent.getMachine().equals(secondEvent.getMachine());

			if(!typesEqual || !valuesEqual || !machinesEqual) {
				return false;
			}
		}

		return true;
	}

	/** Clears the list of Events that have been observed. */
	public void clearObservedEvents() {
		events.clear();
	}

	private void logEvent(EventType type, Object value, StateMachine<?,TransitionInput> machine) {
	    if(allowedEventTypes==null || allowedEventTypes.contains(type)) {
	    	events.add(new Event(type, value, (StateMachine<?,Object>) machine));
	    }
    }

	@Override
	public void beforeStateEntered(State<TransitionInput> state, StateMachine<?,TransitionInput> machine) {
		logEvent(EventType.STATE_ENTERED, state, machine);
	}

	@Override
	public void beforeStateExited(State<TransitionInput> state, StateMachine<?,TransitionInput> machine) {
		logEvent(EventType.STATE_EXITED, state, machine);
	}

	@Override
	public void beforeCompositeStateEntered(CompositeState<TransitionInput> composite, StateMachine<?,TransitionInput> machine) {
		logEvent(EventType.COMPOSITE_STATE_ENTERED, composite, machine);
	}

	@Override
	public void beforeCompositeStateExited(CompositeState<TransitionInput> composite, StateMachine<?,TransitionInput> machine) {
		logEvent(EventType.COMPOSITE_STATE_EXITED, composite, machine);
	}

	@Override
	public void beforeTransition(Transition<TransitionInput> transition, TransitionInput input, StateMachine<?,TransitionInput> machine) {
		logEvent(EventType.TRANSITION_FOLLOWED, transition, machine);
	}

	@Override
	public void noValidTransition(TransitionInput input, StateMachine<?,TransitionInput> machine) {
		logEvent(EventType.NO_VALID_TRANSITION_FOUND, input, machine);
	}
}


