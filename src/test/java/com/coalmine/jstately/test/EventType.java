package com.coalmine.jstately.test;

import java.util.EnumSet;

public enum EventType {
	INPUT_EVALUATED,
	STATE_ENTERED,
	STATE_EXITED,
	COMPOSITE_STATE_ENTERED,
	COMPOSITE_STATE_EXITED,
	TRANSITION_FOLLOWED,
	NO_VALID_TRANSITION_FOUND;

	public static EventType[] ALL_TYPES_EXCEPT_INPUT_VALIDATION;

	static {
		EnumSet<EventType> allTypesExceptInputValidation = EnumSet.complementOf(EnumSet.of(INPUT_EVALUATED));
		ALL_TYPES_EXCEPT_INPUT_VALIDATION = allTypesExceptInputValidation.toArray(new EventType[allTypesExceptInputValidation.size()]);
	}
}


