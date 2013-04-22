package com.coalmine.jstately.graph.state;


public class DefaultFinalState<Result> extends DefaultState<Result> implements FinalState<Result> {
	Result result;

	public DefaultFinalState() {
		super();
	}

	public DefaultFinalState(String description, Result result) {
		super(description);
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


