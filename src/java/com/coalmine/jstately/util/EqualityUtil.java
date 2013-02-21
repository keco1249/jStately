package com.coalmine.jstately.util;

public abstract class EqualityUtil {
	public static boolean objectsAreEqual(Object first, Object second) {
		if(first==null) {
			return second==null;
		} else {
			return second==null? false : first.equals(second);
		}
	}
}




