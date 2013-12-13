package com.coalmine.jstately.machine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	protected InputAdapter<MachineInput,TransitionInput>		inputAdapter;
	protected List<StateMachineEventListener<TransitionInput>>	eventListeners = new ArrayList<StateMachineEventListener<TransitionInput>>();

	private StateMachine<TransitionInput,TransitionInput>		submachine;

	public StateMachine() { }

	public StateMachine(StateGraph<TransitionInput> stateGraph, InputAdapter<MachineInput,TransitionInput> inputAdapter) {
		this.stateGraph		= stateGraph;
		this.inputAdapter	= inputAdapter;
	}

	/** Initialize the machine to its start state, calling its {@link NonFinalState#onEnter()} method.
	 * 
	 * @throws IllegalStateException thrown if no start state was specified or if the machine has already been started. */
	@SuppressWarnings("unchecked")
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

		stateGraph.onStart();

		enterState(stateGraph.getStartState());
	}

	/** @return Whether the machine has a current state. */
	public boolean hasStarted() {
		return currentState != null;
	}

	/** Provides the input parameter to the machine's InputAdapter and evaluating the resulting
	 * transition input(s).  For each transition input, the machine follows the first transition
	 * that is valid according to its {@link Transition#isValid(Object)} method.
	 * 
	 * @param input Machine input from which Transition inputs are generated to evaluate.
	 * @return Whether any of the machine input's subsequent transition inputs were ignored (i.e.,
	 * no valid transition was found) while evaluating.
	 * @throws IllegalStateException thrown if no {@link InputAdapter} has been set. */
	public boolean evaluateInput(MachineInput input) {
		if(inputAdapter==null) {
			throw new IllegalStateException("No InputAdapter specified prior to calling evaluateInput().");
		}

		boolean inputIgnored = false;
		inputAdapter.queueInput(input);
		while(inputAdapter.hasNext()) {
			TransitionInput transitionInput = inputAdapter.next();

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

	public Transition<TransitionInput> findFirstValidTransitionFromCurrentState(TransitionInput input) {
		if(!hasStarted()) {
			throw new IllegalStateException("Machine has not started.");
		}
		return stateGraph.findFirstValidTransitionFromState(currentState, input);
	}

	/**
	 * Follows the given transition without checking its validity.  Calls {@link State#onExit()} on
	 * the current state, followed by {@link Transition#onTransition()} on the given transition,
	 * followed by {@link State#onEnter()} on the machine's updated current state.
	 * @param transition State transition to follow.
	 * @throws IllegalArgumentException thrown if the StateMachine has not started or the given Transition does not
	 * originate at the machine's current state.
	 */
	@SuppressWarnings("unchecked")
    protected void transition(Transition<TransitionInput> transition, TransitionInput input) {
		if(!hasStarted()) {
			throw new IllegalStateException("Machine has not started.");
		}

		exitState(transition.getHead());

		for(StateMachineEventListener<TransitionInput> listener : eventListeners) {
			listener.beforeTransition(transition, input);
		}
		transition.onTransition(input);
		for(StateMachineEventListener<TransitionInput> listener : eventListeners) {
			listener.afterTransition(transition, input);
		}

		enterState(transition.getHead());
	}

	/** Exits the current state and enters the given state.  Explicitly setting the machine's state should generally be
	 * avoided.  However, this would be useful if, for example, your state machine corresponds to some external system
	 * that has changed and your application needs to get back in sync with it, without evaluating inputs to do so. */
	public void transition(State<TransitionInput> newState, State<TransitionInput>... submachineStates) {
		if(newState == null) {
			throw new IllegalArgumentException("New state cannot be null.");
		}

		exitState(newState);
		enterState(newState, submachineStates);
	}

	/** Enters the given state, using currentState to determine what CompositeStates (if any) are being enterred. */
	private void enterState(State<TransitionInput> newState, State<TransitionInput>... submachineStates) {
		for(StateMachineEventListener<TransitionInput> listener : eventListeners) {
			listener.beforeStateEntered(newState);
		}

		for(CompositeState<TransitionInput> composite : determinateCompositesBeingEntered(currentState,newState)) {
			enterCompositeState(composite);
		}

		newState.onEnter();

		if(newState instanceof SubmachineState) {
			SubmachineState<TransitionInput> submachineState = (SubmachineState<TransitionInput>)newState;
			initializeSubmachine(submachineState, submachineStates);
		}

		currentState = newState;

		for(StateMachineEventListener<TransitionInput> listener : eventListeners) {
			listener.afterStateEntered(newState);
		}
	}

	private void initializeSubmachine(SubmachineState<TransitionInput> submachineState, State<TransitionInput>[] submachineStates) {
		submachine = new StateMachine<TransitionInput,TransitionInput>(submachineState.getStateGraph(), new DefaultInputAdapter<TransitionInput>());
		submachine.eventListeners = eventListeners;

		if(submachineStates.length > 0) {
			submachine.enterState(submachineStates[0], Arrays.copyOfRange(submachineStates, 1, submachineStates.length-1));
		} else { // No states to initialize nested state machines to
			submachine.start();
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

	/** Transitions from currentState, using newState to determine which CompositeState's (if any) are being left. */
	private void exitState(State<TransitionInput> newState) {
		if(currentState == null) {
			return;
		}

		for(StateMachineEventListener<TransitionInput> listener : eventListeners) {
			listener.beforeStateExited(currentState);
		}

		if(currentState instanceof SubmachineState) {
			SubmachineState<TransitionInput> submachineState = (SubmachineState<TransitionInput>)currentState;
			// TODO Exit submachine's state
			submachine = null;
		}

		currentState.onExit();

		for(CompositeState<TransitionInput> composite : determinateCompositeStatesBeingExited(currentState,newState)) {
			exitCompositeState(composite);
		}

		for(StateMachineEventListener<TransitionInput> listener : eventListeners) {
			listener.afterStateExited(currentState);
		}
	}


	public StateGraph<TransitionInput> getStateGraph() {
		return stateGraph;
	}
	public void setStateGraph(StateGraph<TransitionInput> stateGraph) {
		this.stateGraph = stateGraph;
	}

	public InputAdapter<MachineInput,TransitionInput> getInputAdapter() {
		return inputAdapter;
	}
	public void setInputAdapter(InputAdapter<MachineInput,TransitionInput> inputAdapter) {
		this.inputAdapter = inputAdapter;
	}

	/** Returns the state of the machine, including the state of any submachines that may be running.  The first State
	 * returned is that of machine on which getState() is being called, followed by the state of nested machines.
	 * 
	 * @see #getState() Retrieves only the state of the machine on which it is being called. */
	public List<State<TransitionInput>> getStates() {
		List<State<TransitionInput>> states = new ArrayList<State<TransitionInput>>();
		return appendCurrentState(states);
	}

	/** Recursively adds the current state of the machine and any submachines to the given list. */
	private List<State<TransitionInput>> appendCurrentState(List<State<TransitionInput>> states) {
		states.add(currentState);

		return submachine==null?
				states :
				submachine.appendCurrentState(states);
	}

	/** Gets only the State of the machine, without the state of any SubmachineStates that may be running.
	 * @see #getStates() */
	public State<TransitionInput> getState() {
		return currentState;
	}

	/** Simply sets the machine's state, without calling event methods like {@link State#onEnter()}
	 * or {@link State#onExit()}.  It also does not setup the state machine for SubmachineStates. 
	 * This method was added for testing and should be avoided by API users, who will likely find
	 * {@link #transition(State, State...)} more useful anyway. */
	protected void overrideState(State<TransitionInput> newState) {
		currentState = newState;
	}

	public void addEventListener(StateMachineEventListener<TransitionInput> eventListener) {
		eventListeners.add(eventListener);
	}

	public void removeEventListener(StateMachineEventListener<TransitionInput> eventListener) {
		eventListeners.remove(eventListener);
	}
}




