package com.coalmine.jstately.graph.state;

import com.coalmine.jstately.graph.StateGraph;

/** Interface for a state which runs a nested state machine.  When a SubmachineState is entered, the state graph
 * provided by {@link #getStateGraph()} is run in a new state machine.  Any inputs evaluated on the outer machine will
 * be delegated to the nest machine until the nested machines reaches a {@link FinalState}, at which point the
 * FinalState's result value will be evaluated as an input on the outer machine. */
public interface SubmachineState <InputType> extends State<InputType> {
	StateGraph<InputType> getStateGraph();
}


