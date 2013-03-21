package com.coalmine.jstately.graph.state;


public class DefaultFinalState<Result> extends DefaultState implements FinalState<Result> {
	Result result;

	public DefaultFinalState() {
		super();
	}

	public DefaultFinalState(String identifier, Result result) {
		super(identifier);
		this.result = result;
	}

	public DefaultFinalState(String identifier, Result result, String description) {
		super(identifier, description);
		this.result = result;
	}

	@Override
	public Result getOutput() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
}


