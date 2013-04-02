package com.coalmine.jstately.graph.state;

/** A final state that provides an output value that can indicate the result of running a state
 * machine (including machines embedded in submachine states, the output of which is used as an
 * input for the parent machine.)
 * 
 * @see NonFinalState */
public interface FinalState<Result> extends State<Result> {
	Result getResult();
}


