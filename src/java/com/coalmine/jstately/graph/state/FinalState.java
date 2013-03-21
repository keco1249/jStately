package com.coalmine.jstately.graph.state;

/** A final state that provides an output value that can indicate the result of running a state
 * machine (including machines embedded in composite states, the output of which is used as an
 * input for the parent machine.)  Note that in this context, final states can never be exited
 * and therefore will not have an onExit() callback.
 * 
 * @see NonFinalState */
public interface FinalState<Result> extends BaseState {
	Result getOutput();
}


