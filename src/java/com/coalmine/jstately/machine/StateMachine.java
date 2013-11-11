package com.coalmine.jstately.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.coalmine.jstately.graph.StateGraph;
import com.coalmine.jstately.graph.composite.CompositeState;
import com.coalmine.jstately.graph.state.FinalState;
import com.coalmine.jstately.graph.state.State;
import com.coalmine.jstately.graph.state.SubmachineState;
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
		if(stateGraph == null) {
			throw new IllegalStateException("No state graph specified.");
		}
		if(stateGraph.getStartState()==null) {
			throw new IllegalStateException("No start state specified.");
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

			for(StateMachineEventListener<TransitionInput> listener : eventListeners) {
				listener.beforeEvaluatingInput(transitionInput);
			}

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
				Transition<TransitionInput> validTransition = findFirstValidTransitionFromCurrentState(inputToEvaluate);
				if(validTransition == null) {
					inputIgnored = true;
				} else {
					transition(validTransition,transitionInput);
				}
			}


			for(StateMachineEventListener<TransitionInput> listener : eventListeners) {
				listener.afterEvaluatingInput(transitionInput);
			}
		}

		return inputIgnored;
	}

	public Set<Transition<TransitionInput>> findAllValidTransitionsFromCurrentState(TransitionInput input) {
		if(!hasStarted()) {
			throw new IllegalStateException("Machine has not started.");
		}
		return stateGraph.findValidTransitionsFromState(currentState, input);
	}

	public Transition<TransitionInput> findFirstValidTransitionFromCurrentState(TransitionInput input) {
		if(!hasStarted()) {
			throw new IllegalStateException("Machine has not started.");
		}
		return stateGraph.findFirstValidTransitionFromState(currentState, input);
	}

	public Set<Transition<TransitionInput>> getTransitionsFromCurrentState() {
		if(!hasStarted()) {
			throw new IllegalStateException("Machine has not started.");
		}
		return stateGraph.findAllTransitionsFromState(currentState);
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

		for(CompositeState<TransitionInput> composite : determinateCompositesBeingEntered(currentState,newState)) {
			enterCompositeState(composite);
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

	/** Determines which composite states are being entered when entering a state.
	 * @return A list of CompositeStates being entered in order they are being entered (from the root CompositeState to nested ones.) */
	private List<CompositeState<TransitionInput>> determinateCompositesBeingEntered(State<TransitionInput> oldState, State<TransitionInput> newState) {
		List<CompositeState<TransitionInput>> newStateComposites = collectCompositeStates(newState);
		if(oldState==null) {
			return Lists.reverse(newStateComposites);
		}

		List<CompositeState<TransitionInput>> oldStateComposites = collectCompositeStates(oldState);
		newStateComposites.removeAll(oldStateComposites);
		return Lists.reverse(newStateComposites);
	}

	/** Determines which composite states are being exited when exiting a state.
	 * @return A list of CompositeStates being exited in order they are being exist (from the State's immediate CompositeState to its root CompositeState.) */
	private List<CompositeState<TransitionInput>> determinateCompositeStatesBeingExited(State<TransitionInput> oldState, State<TransitionInput> newState) {
		List<CompositeState<TransitionInput>> oldStateComposites = collectCompositeStates(oldState);
		if(newState==null) {
			return oldStateComposites;
		}

		List<CompositeState<TransitionInput>> newStateComposites = collectCompositeStates(newState);
		oldStateComposites.removeAll(newStateComposites);
		return oldStateComposites;
	}

	/** @return All of a State's CompositeStates, with nested composites ordered from the State's immediate parent composite to the root */
	private List<CompositeState<TransitionInput>> collectCompositeStates(State<TransitionInput> state) {
		List<CompositeState<TransitionInput>> composites = new ArrayList<CompositeState<TransitionInput>>();

		for(CompositeState<TransitionInput> composite : state.getComposites()) {
			while(composite != null) {
				composites.add(composite);
				composite = composite.getParent();
			}
		}

		return composites;
	}

	private void enterCompositeState(CompositeState<TransitionInput> composite) {
		for(StateMachineEventListener<TransitionInput> eventListener : eventListeners) {
			eventListener.beforeCompositeStateEntered(composite);
		}

		composite.onEnter();

		for(StateMachineEventListener<TransitionInput> eventListener : eventListeners) {
			eventListener.afterCompositeStateEntered(composite);
		}
	}

	private void exitCompositeState(CompositeState<TransitionInput> composite) {
		for(StateMachineEventListener<TransitionInput> eventListener : eventListeners) {
			eventListener.beforeCompositeStateExited(composite);
		}

		composite.onExit();

		for(StateMachineEventListener<TransitionInput> eventListener : eventListeners) {
			eventListener.afterCompositeStateExited(composite);
		}
	}

	private void exitState(State<TransitionInput> oldState, State<TransitionInput> newState) {
		for(StateMachineEventListener<TransitionInput> listener : eventListeners) {
			listener.beforeStateExited(oldState);
		}

		oldState.onExit();
		
		for(CompositeState<TransitionInput> composite : determinateCompositeStatesBeingExited(currentState,newState)) {
			exitCompositeState(composite);
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

	/** Returns the state of the machine, including the state of any submachines that may be running.  The first State
	 * returned is that of machine on which getState() is being called, followed by the state of nested machines. 
	 * @see #getState() Retrieves only the state of the machine on which it is being called. */
	public List<State<TransitionInput>> getStates() {
		List<State<TransitionInput>> states = new ArrayList<State<TransitionInput>>();
		return appendCurrentState(states);
	}

	/** Gets only the State of the machine, without the state of any SubmachineStates that may be running.
	 * @see #getStates() Retrieves the state of the current machine and nested submachines. */
	public State<TransitionInput> getState() {
		return currentState;
	}

	/** Restores the state of the machine and any submachines. */
	public void restoreState(List<State<TransitionInput>> states) {
		// FIXME Implement this.
	}

	private List<State<TransitionInput>> appendCurrentState(List<State<TransitionInput>> states) {
		states.add(currentState);
		return submachine==null? states : submachine.appendCurrentState(states);
	}

	/**
	 * Sets the machine's state without calling any event methods such as {@link State#onEnter()}
	 * or {@link State#onExit()}. This is mostly for testing.  API users should generally avoid
	 * setting a machine's state explicitly.
	 */
	public void overrideState(State<TransitionInput> newState) {
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




