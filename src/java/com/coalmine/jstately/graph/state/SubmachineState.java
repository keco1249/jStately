package com.coalmine.jstately.graph.state;

import com.coalmine.jstately.graph.StateGraph;

public interface SubmachineState <InputType> extends State<InputType> {
	StateGraph<InputType> getStateGraph();
}


