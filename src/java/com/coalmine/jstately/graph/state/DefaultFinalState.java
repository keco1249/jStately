package com.coalmine.jstately.graph.state;


public class DefaultFinalState<Result> extends DefaultState<Result> implements FinalState<Result> {
	Result result;

	public DefaultFinalState() {
		super();
	}

	public DefaultFinalState(String identifier, Result result) {
		super(identifier);
		this.result = result;
	}

	@Override
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
}


