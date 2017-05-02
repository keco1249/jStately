package com.coalminesoftware.jstately.validation;

public final class Validation {
    private Validation() { }

    public static void assertParameterNotNull(String message, Object parameter) {
        if(parameter == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertFieldNotNull(String message, Object parameter) {
        if(parameter == null) {
            throw new IllegalStateException(message);
        }
    }
}
