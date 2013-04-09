package com.coalmine.jstately.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.coalmine.jstately.graph.StateGraph;
import com.coalmine.jstately.graph.composite.CompositeState;
import com.coalmine.jstately.graph.state.SubmachineState;
import com.coalmine.jstately.graph.state.FinalState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.transition.Transition;
import com.coalmine.jstately.machine.listener.StateMachineEventListener;
import com.google.common.collect.Lists;


/** Representation of a basic state machine. */
public class StateMachine<MachineInput,TransitionInput> {
	protected StateGraph<TransitionInput>						stateGraph;
	protected State<TransitionInput>							currentState;
	protected InputAdapter<MachineInput,TransitionInput>		inputProvider;
	protected List<StateMachineEventListener<TransitionInput>>	eventListeners = new ArrayList<StateMachineEventListener<TransitionInput>>();

	private StateMachine<TransitionInput,TransitionInput>		submachine;

	public StateMachine() { }

	public StateMachine(StateGraph<TransitionInput> stateGraph, InputAdapter<MachineInput,TransitionInput> inputProvider) {
		this.stateGraph		= stateGraph;
		this.inputProvider	= inputProvider;
	}

	/**
	 * Initialize the machine to its start state, calling its {@link NonFinalState#onEnter()} method.
	 * @throws IllegalStateException thrown if no start state was specified or if the machine has already been started.
	 */
	public void start() {
		if(hasStarted()) {
			throw new IllegalStateException("Machine has already started.");
		}
		if(stateGraph.getStartState()==null) {
			throw new IllegalStateException("No start state specified.");
		}
		if(!stateGraph.getStates().contains(stateGraph.getStartState())) {
			throw new IllegalStateException("Start state not defined in graph.");
		}

		enterState(stateGraph.getStartState());
	}

	/** @return Whether the machine has a current state. */
	public boolean hasStarted() {
		return currentState != null;
	}

	/** Resets the machine's state to null without calling {@link State#onExit()} on the current state (if there is one.) */
	public void reset() {
		currentState = null;
	}

	/**
	 * Providing the machine's input to its InputAdapter, the resulting transition input(s) are
	 * iterated over.  For each input, the machine follows the first transition that is valid
	 * according to its {@link Transition#isValid(Object)} method.
	 * @param input Machine input from which Transition inputs are generated to evaluate and transition on.
	 * @return Whether any of input's subsequent Transition inputs were ignored (no valid transition was found) while evaluating.
	 * @throws IllegalStateException thrown if no {@link InputAdapter} has been set.
	 */
	public boolean evaluateInput(MachineInput input) {
		if(inputProvider==null) {
			throw new IllegalStateException("No InputAdapter specified prior to calling evaluateInput().");
		}

		boolean inputIgnored = false;
		inputProvider.queueInput(input);
		while(inputProvider.hasNext()) {
			TransitionInput transitionInput = inputProvider.next();

			TransitionInput inputToEvaluate = null;
			if(currentState instanceof SubmachineState) {
				// Delegate the evaluation of the input
				submachine.evaluateInput(transitionInput);

				// If the submachine reaches a final state, extract its output to evaluate on this machine
				if(submachine.getState() instanceof FinalState) {
					inputToEvaluate = ((FinalState<TransitionInput>)submachine.getState()).getResult();
				}
			} else {
				inputToEvaluate = transitionInput;
			}

			if(inputToEvaluate != null) {
				Transition<TransitionInput> validTransition = getFirstValidTransitionFromCurrentState(inputToEvaluate);
				if(validTransition==null) {
					// See if a containing CompositeState has a valid Transition
					validTransition = findValidParentCompositeTransition(currentState, inputToEvaluate);
					if(validTransition == null) {
						inputIgnored = true;
					} else {
						transition(validTransition, transitionInput);
					}
				} else {
					transition(validTransition,transitionInput);
				}
			}
		}

		return inputIgnored;
	}


	/** For a given State, traverses upward, to parent CompositeGroups, checking for a Transition that's valid for the given input. */
	private static <TransitionInput> Transition<TransitionInput> findValidParentCompositeTransition(State<TransitionInput> state, TransitionInput input) {
		CompositeState<TransitionInput> composite = state.getComposite();
		while(composite != null) {
			Transition<TransitionInput> transition = composite.getFirstValidTransition(input);
			if(transition != null) {
				return transition;
			}

			composite = composite.getParent();
		}

		return null;
	}

	public State<TransitionInput> getSubState() {
		return submachine==null? null : submachine.getState();
	}

	public Set<Transition<TransitionInput>> getValidTransitionsFromCurrentState(TransitionInput input) {
		if(!hasStarted()) {
			throw new IllegalStateException("Machine has not started.");
		}
		return stateGraph.getValidTransitionsFromTail(currentState, input);
	}

	public Transition<TransitionInput> getFirstValidTransitionFromCurrentState(TransitionInput input) {
		if(!hasStarted()) {
			throw new IllegalStateException("Machine has not started.");
		}
		return stateGraph.getFirstValidTransitionFromTail(currentState, input);
	}

	public Set<Transition<TransitionInput>> getTransitionsFromCurrentState() {
		if(!hasStarted()) {
			throw new IllegalStateException("Machine has not started.");
		}
		return stateGraph.getTransitionsFromTail(currentState);
	}

	/** @return A collection of states that could potentially be transitioned to from the current state, ignoring whether they are valid. */
	public Set<State<TransitionInput>> getStatesFromCurrentState() {
		if(!hasStarted()) {
			throw new IllegalStateException("Machine has not started.");
		}
		return stateGraph.getStatesFromTail(currentState);
	}

	/** @return A collection of states that could be transitioned to given the provided input. */
	public Set<State<TransitionInput>> getValidStatesFromCurrentState(TransitionInput input) {
		if(!hasStarted()) {
			throw new IllegalStateException("Machine has not started.");
		}
		return stateGraph.getValidStatesFromTail(currentState,input);
	}

	/**
	 * Follows the given transition without checking its validity.  Calls {@link State#onExit()} on
	 * the current state, followed by {@link Transition#onTransition()} on the given transition,
	 * followed by {@link State#onEnter()} on the machine's updated current state.
	 * @param transition State transition to follow.
	 * @throws IllegalArgumentException thrown if the StateMachine has not started or the given Transition does not
	 * originate at the machine's current state.
	 */
	protected void transition(Transition<TransitionInput> transition, TransitionInput input) {
		if(!hasStarted()) {
			throw new IllegalStateException("Machine has not started.");
		}

		exitState(currentState, transition.getHead());

		for(StateMachineEventListener<TransitionInput> listener : eventListeners) {
			listener.beforeTransition(transition, input);
		}
		transition.onTransition(input);
		for(StateMachineEventListener<TransitionInput> listener : eventListeners) {
			listener.afterTransition(transition, input);
		}

		enterState(transition.getHead());
	}

	//FIXME Document the hell out of this method since it's kinda hacky.
	protected void transition(State<TransitionInput> oldState, State<TransitionInput> newState) {
		if(oldState != null) {
			exitState(oldState, newState);
		}

		enterState(newState);
	}

	//FIXME Document the hell out of this method since it's kinda hacky.
	protected void transition(State<TransitionInput> newState) {
		transition(currentState, newState);
	}
	
	private void enterState(State<TransitionInput> newState) {
		for(StateMachineEventListener<TransitionInput> listener : eventListeners) {
			listener.beforeStateEntered(newState);
		}

		for(CompositeState<TransitionInput> section : determinateSectionBeingEntered(currentState,newState)) {
			enterSection(section);
		}

		currentState = newState;
		currentState.onEnter();

		if(currentState instanceof SubmachineState) {
			SubmachineState<TransitionInput> submachineState = (SubmachineState<TransitionInput>)currentState;
			submachine = new StateMachine<TransitionInput,TransitionInput>(submachineState.getStateGraph(), new DefaultInputAdapter<TransitionInput>());
			submachine.setEventListeners(eventListeners);
			submachine.start();
		}

		for(StateMachineEventListener<TransitionInput> listener : eventListeners) {
			listener.afterStateEntered(newState);
		}
	}

	/** Determines which sections are being entered when entering a state.
	 * @return A list of Sections being entered in order they are being entered (from the root Section to nested ones.) */
	private List<CompositeState<TransitionInput>> determinateSectionBeingEntered(State<TransitionInput> oldState, State<TransitionInput> newState) {
		List<CompositeState<TransitionInput>> newStateSections = getStateSections(newState);
		if(oldState==null) {
			return Lists.reverse(newStateSections);
		}

		List<CompositeState<TransitionInput>> oldStateSections = getStateSections(oldState);
		newStateSections.removeAll(oldStateSections);
		return Lists.reverse(newStateSections);
	}

	/** Determines which sections are being exited when exiting a state.
	 * @return A list of Sections being exited in order they are being exist (from the state's immediate Section to its root Section.) */
	private List<CompositeState<TransitionInput>> determinateSectionBeingExited(State<TransitionInput> oldState, State<TransitionInput> newState) {
		List<CompositeState<TransitionInput>> oldStateSections = getStateSections(oldState);
		if(newState==null) {
			return oldStateSections;
		}

		List<CompositeState<TransitionInput>> newStateSections = getStateSections(newState);
		oldStateSections.removeAll(newStateSections);
		return oldStateSections;
	}

	/** @return A State's parent Section and all parent Sectioned, ordered from the State's Section to the root */
	private List<CompositeState<TransitionInput>> getStateSections(State<TransitionInput> state) {
		List<CompositeState<TransitionInput>> sections = new ArrayList<CompositeState<TransitionInput>>();

		CompositeState<TransitionInput> section = state.getComposite();
		while(section != null) {
			sections.add(section);
			section = section.getParent();
		}

		return sections;
	}

	private void enterSection(CompositeState<TransitionInput> section) {
		for(StateMachineEventListener<TransitionInput> eventListener : eventListeners) {
			eventListener.beforeCompositeStateEntered(section);
		}

		section.onEnter();

		for(StateMachineEventListener<TransitionInput> eventListener : eventListeners) {
			eventListener.afterCompositeStateEntered(section);
		}
	}

	private void exitSection(CompositeState<TransitionInput> section) {
		for(StateMachineEventListener<TransitionInput> eventListener : eventListeners) {
			eventListener.beforeCompositeStateExited(section);
		}

		section.onExit();

		for(StateMachineEventListener<TransitionInput> eventListener : eventListeners) {
			eventListener.afterCompositeStateExited(section);
		}
	}

	private void exitState(State<TransitionInput> oldState, State<TransitionInput> newState) {
		for(StateMachineEventListener<TransitionInput> listener : eventListeners) {
			listener.beforeStateExited(oldState);
		}

		oldState.onExit();
		
		for(CompositeState<TransitionInput> section : determinateSectionBeingExited(currentState,newState)) {
			exitSection(section);
		}

		if(oldState instanceof SubmachineState) {
			submachine = null;
		}

		for(StateMachineEventListener<TransitionInput> listener : eventListeners) {
			listener.afterStateExited(oldState);
		}
	}


	public StateGraph<TransitionInput> getStateGraph() {
		return stateGraph;
	}
	public void setStateGraph(StateGraph<TransitionInput> stateGraph) {
		this.stateGraph = stateGraph;
	}

	public void setInputProvider(InputAdapter<MachineInput,TransitionInput> inputProvider) {
		this.inputProvider = inputProvider;
	}
	public InputAdapter<MachineInput,TransitionInput> getInputProvider() {
		return inputProvider;
	}

	public State<TransitionInput> getState() {
		return currentState;
	}

	/**
	 * Sets the machine's state without calling any event methods such as {@link State#onEnter()}
	 * or {@link State#onExit()}. This is mostly for testing.  API users should generally avoid
	 * setting a machine's state explicitly.
	 */
	public void setCurrentState(State<TransitionInput> newState) {
		this.currentState = newState;
	}

	public List<StateMachineEventListener<TransitionInput>> getEventListeners() {
		return eventListeners;
	}
	public void setEventListeners(List<StateMachineEventListener<TransitionInput>> eventListeners) {
		if(eventListeners==null) {
			throw new IllegalArgumentException("Provided EventListener list cannot be null.");
		}
		this.eventListeners = eventListeners;
	}
	public void addEventListener(StateMachineEventListener<TransitionInput> eventListener) {
		eventListeners.add(eventListener);
	}
}




