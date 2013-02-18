package com.coalmines.jstately.machine;

import java.util.Set;

import com.coalmines.jstately.graph.State;
import com.coalmines.jstately.graph.StateGraph;
import com.coalmines.jstately.graph.Transition;



/**
 * Representation of a basic state machine.
 */
public class StateMachine<MachineInput,TransitionInput> {
	protected StateGraph<TransitionInput>					stateGraph;
	protected State											currentState;
	protected InputAdapter<MachineInput,TransitionInput>	inputProvider;


	/**
	 * Initialize the machine to its start state, calling its {@link State#onEnter()} method.
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

		currentState = stateGraph.getStartState();
		currentState.onEnter();
	}

	/**
	 * @return Whether the machine has a current state.
	 */
	public boolean hasStarted() {
		return currentState!=null;
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
	 * @throws IllegalStateException thrown if no InputAdapter has been set.
	 */
	public boolean evaluateInput(MachineInput input) {
		if(inputProvider==null) {
			throw new IllegalStateException("No InputAdapter specified prior to calling evaluateInput().");
		}

		boolean inputIgnored = false;
		inputProvider.queueInput(input);
		while(inputProvider.hasNext()) {
			TransitionInput transitionInput=inputProvider.next();
			Transition<TransitionInput> validTransition = getFirstValidTransitionFromCurrentState(transitionInput);
			if(validTransition==null) {
				inputIgnored = true;
			} else {
				transition(validTransition);
			}
		}

		return inputIgnored;
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
	public Set<State> getStatesFromCurrentState() {
		if(!hasStarted()) {
			throw new IllegalStateException("Machine has not started.");
		}
		return stateGraph.getStatesFromTail(currentState);
	}

	/** @return A collection of states that could be transitioned to given the provided input. */
	public Set<State> getValidStatesFromCurrentState(TransitionInput input) {
		if(!hasStarted()) {
			throw new IllegalStateException("Machine has not started.");
		}
		return stateGraph.getValidStatesFromTail(currentState,input);
	}

	/** @return Whether the machine is in a state marked as an "accept" state. */
	public boolean isInAcceptState() {
		return currentState!=null && currentState.isAcceptState();
	}

	/** @return Whether the machine is in a state where no transitions (valid or not) are available. */
	public boolean isInFinalState() {
		return getTransitionsFromCurrentState().size() == 0;
	}

	/**
	 * Follows the given transition without checking its validity.  Calls {@link State#onExit()} on
	 * the current state, followed by {@link Transition#onTransition()} on the given transition,
	 * followed by {@link State#onEnter()} on the machine's updated current state.
	 * @param transition State transition to follow.
	 * @throws IllegalArgumentException thrown if the StateMachine has not started or the given Transition does not
	 * originate at the machine's current state.
	 */
	protected void transition(Transition<TransitionInput> transition) {
		if(!hasStarted()) {
			throw new IllegalStateException("Machine has not started.");
		}
		if(transition==null || !transition.getTail().equals(currentState)) {
			throw new IllegalArgumentException("Transition not allowed from machine's current state.");
		}

		currentState.onExit();
		transition.onTransition();
		currentState = transition.getHead();
		currentState.onEnter();
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

	public State getCurrentState() {
		return currentState;
	}
	/**
	 * Sets the machine's state without calling any event methods like {@link State#onEnter()} or
	 * {@link State#onExit()}.  This is mostly for testing.  StateMachine users should generally not
	 * set the machine's state themselves.
	 */
	public void setCurrentState(State newState) {
		this.currentState = newState;
	}
}




